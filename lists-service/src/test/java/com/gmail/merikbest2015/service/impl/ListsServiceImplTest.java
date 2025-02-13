package com.gmail.merikbest2015.service.impl;

import com.gmail.merikbest2015.ListsServiceTestHelper;
import com.gmail.merikbest2015.commons.constants.ErrorMessage;
import com.gmail.merikbest2015.commons.dto.HeaderResponse;
import com.gmail.merikbest2015.commons.dto.request.IdsRequest;
import com.gmail.merikbest2015.constants.ListsErrorMessage;
import com.gmail.merikbest2015.constants.ListsSuccessMessage;
import com.gmail.merikbest2015.dto.request.ListsRequest;
import com.gmail.merikbest2015.dto.request.UserToListsRequest;
import com.gmail.merikbest2015.commons.dto.response.tweet.TweetResponse;
import com.gmail.merikbest2015.commons.exception.ApiRequestException;
import com.gmail.merikbest2015.model.Lists;
import com.gmail.merikbest2015.model.PinnedList;
import com.gmail.merikbest2015.model.User;
import com.gmail.merikbest2015.repository.projection.BaseListProjection;
import com.gmail.merikbest2015.repository.projection.ListProjection;
import com.gmail.merikbest2015.repository.projection.ListUserProjection;
import com.gmail.merikbest2015.repository.projection.PinnedListProjection;
import com.gmail.merikbest2015.service.AbstractServiceTest;
import com.gmail.merikbest2015.service.ListsService;
import com.gmail.merikbest2015.commons.util.TestConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.gmail.merikbest2015.ListsServiceTestHelper.mockUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListsServiceImplTest extends AbstractServiceTest {

    @Autowired
    private ListsService listsService;

    @Test
    public void getAllTweetLists() {
        List<ListProjection> mockListProjectionList = ListsServiceTestHelper.createMockListProjectionList();
        when(listsRepository.getAllTweetLists(TestConstants.USER_ID)).thenReturn(mockListProjectionList);
        assertEquals(2, listsService.getAllTweetLists().size());
        verify(listsRepository, times(1)).getAllTweetLists(TestConstants.USER_ID);
    }

    @Test
    public void getUserTweetLists() {
        when(listsRepository.getUserTweetLists(TestConstants.USER_ID))
                .thenReturn(ListsServiceTestHelper.createMockListUserProjectionList());
        assertEquals(2, listsService.getUserTweetLists().size());
        verify(listsRepository, times(1)).getUserTweetLists(TestConstants.USER_ID);
    }

    @Test
    public void getUserPinnedLists() {
        when(listsRepository.getUserPinnedLists(TestConstants.USER_ID))
                .thenReturn(ListsServiceTestHelper.createMockPinnedListProjectionList());
        assertEquals(2, listsService.getUserPinnedLists().size());
        verify(listsRepository, times(1)).getUserPinnedLists(TestConstants.USER_ID);
    }

    @Test
    public void getListById() {
        BaseListProjection baseListProjection = ListsServiceTestHelper.createMockBaseListProjection(TestConstants.LIST_USER_ID);
        when(listsRepository.getListById(TestConstants.LIST_ID, TestConstants.USER_ID, BaseListProjection.class))
                .thenReturn(Optional.of(baseListProjection));
        assertEquals(baseListProjection, listsService.getListById(TestConstants.LIST_ID));
        verify(listsRepository, times(1)).getListById(TestConstants.LIST_ID, TestConstants.USER_ID, BaseListProjection.class);
        verify(userRepository, never()).isUserHavePrivateProfile(TestConstants.LIST_USER_ID, TestConstants.USER_ID);
        verify(userRepository, times(1)).isUserBlocked(TestConstants.LIST_USER_ID, TestConstants.USER_ID);
    }

    @Test
    public void getListById_shouldCheckIsPrivateUserProfile() {
        BaseListProjection baseListProjection = ListsServiceTestHelper.createMockBaseListProjection(3L);
        when(listsRepository.getListById(TestConstants.LIST_ID, TestConstants.USER_ID, BaseListProjection.class))
                .thenReturn(Optional.of(baseListProjection));
        when(userRepository.isUserHavePrivateProfile(3L, TestConstants.USER_ID)).thenReturn(true);
        assertEquals(baseListProjection, listsService.getListById(TestConstants.LIST_ID));
        verify(listsRepository, times(1)).getListById(TestConstants.LIST_ID, TestConstants.USER_ID, BaseListProjection.class);
        verify(userRepository, times(1)).isUserHavePrivateProfile(3L, TestConstants.USER_ID);
        verify(userRepository, times(1)).isUserBlocked(3L, TestConstants.USER_ID);
    }

    @Test
    public void getListById_shouldCheckUserPrivateProfileAndReturnUserNotFound() {
        when(listsRepository.getListById(TestConstants.LIST_ID, TestConstants.USER_ID, BaseListProjection.class))
                .thenReturn(Optional.of(ListsServiceTestHelper.createMockBaseListProjection(3L)));
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.getListById(TestConstants.LIST_ID));
        assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getListById_shouldCheckUserIsBlockedAndReturnBlockedUser() {
        when(listsRepository.getListById(TestConstants.LIST_ID, TestConstants.USER_ID, BaseListProjection.class))
                .thenReturn(Optional.of(ListsServiceTestHelper.createMockBaseListProjection(3L)));
        when(userRepository.isUserHavePrivateProfile(3L, TestConstants.USER_ID)).thenReturn(true);
        when(userRepository.isUserBlocked(3L, TestConstants.USER_ID)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.getListById(TestConstants.LIST_ID));
        assertEquals(String.format(ListsErrorMessage.USER_ID_BLOCKED, 2L), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void getListById_shouldReturnListNotFound() {
        when(listsRepository.getListById(TestConstants.LIST_USER_ID, TestConstants.USER_ID, BaseListProjection.class))
                .thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.getListById(TestConstants.LIST_USER_ID));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void createTweetList() {
        User user = mockUser(TestConstants.USER_ID);
        ListsRequest listsRequest = new ListsRequest();
        listsRequest.setListName(TestConstants.LIST_NAME);
        listsRequest.setIsPrivate(false);
        listsRequest.setDescription(TestConstants.LIST_DESCRIPTION);
        listsRequest.setAltWallpaper(TestConstants.LIST_ALT_WALLPAPER);
        listsRequest.setWallpaper("");
        ListUserProjection listUser = ListsServiceTestHelper.createMockListUserProjectionList().get(0);
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        when(listsRepository.getListById(null, ListUserProjection.class)).thenReturn(listUser);
        listsService.createTweetList(listsRequest);
        verify(listsRepository, times(1)).getListById(null, ListUserProjection.class);
    }

    @Test
    public void createTweetList_shouldEmptyListNameAndReturnException() {
        ListsRequest listsRequest = new ListsRequest();
        listsRequest.setListName("");
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.createTweetList(listsRequest));
        assertEquals(ListsErrorMessage.INCORRECT_LIST_NAME_LENGTH, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void createTweetList_shouldLargeListNameAndReturnException() {
        ListsRequest lists = new ListsRequest();
        lists.setId(1L);
        lists.setListName("**************************");
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.createTweetList(lists));
        assertEquals(ListsErrorMessage.INCORRECT_LIST_NAME_LENGTH, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void getUserTweetListsById() {
        when(listsRepository.getUserTweetListsById(TestConstants.USER_ID))
                .thenReturn(ListsServiceTestHelper.createMockListProjectionList());
        assertEquals(2, listsService.getUserTweetListsById(TestConstants.USER_ID).size());
        verify(listsRepository, times(1)).getUserTweetListsById(TestConstants.USER_ID);
    }

    @Test
    public void getUserTweetListsById_shouldUserBlockedAndReturnEmptyList() {
        when(userRepository.isUserBlocked(TestConstants.USER_ID, 3L)).thenReturn(true);
        when(userRepository.isUserHavePrivateProfile(3L, TestConstants.USER_ID)).thenReturn(false);
        assertEquals(0, listsService.getUserTweetListsById(3L).size());
        verify(listsRepository, never()).getUserTweetListsById(3L);
    }

    @Test
    public void getUserTweetListsById_shouldUserPrivateAndReturnEmptyList() {
        when(userRepository.isUserBlocked(TestConstants.USER_ID, 3L)).thenReturn(false);
        when(userRepository.isUserHavePrivateProfile(3L, TestConstants.USER_ID)).thenReturn(true);
        assertEquals(0, listsService.getUserTweetListsById(3L).size());
        verify(userRepository, times(1)).isUserBlocked(TestConstants.USER_ID, 3L);
        verify(userRepository, times(1)).isUserHavePrivateProfile(3L, TestConstants.USER_ID);
    }

    @Test
    public void getUserTweetListsById_shouldReturnUserTweetLists() {
        when(listsRepository.getUserTweetListsById(3L))
                .thenReturn(ListsServiceTestHelper.createMockListProjectionList());
        when(userRepository.isUserBlocked(TestConstants.USER_ID, 3L)).thenReturn(false);
        when(userRepository.isUserHavePrivateProfile(3L, TestConstants.USER_ID)).thenReturn(true);
        assertEquals(2, listsService.getUserTweetListsById(3L).size());
        verify(listsRepository, times(1)).getUserTweetListsById(3L);
        verify(userRepository, times(1)).isUserBlocked(TestConstants.USER_ID, 3L);
        verify(userRepository, times(1)).isUserHavePrivateProfile(3L, TestConstants.USER_ID);
    }

    @Test
    public void getTweetListsWhichUserIn() {
        when(listsRepository.getTweetListsWhichUserIn(TestConstants.USER_ID))
                .thenReturn(ListsServiceTestHelper.createMockListProjectionList());
        assertEquals(2, listsService.getTweetListsWhichUserIn().size());
        verify(listsRepository, times(1)).getTweetListsWhichUserIn(TestConstants.USER_ID);
    }

    @Test
    public void editTweetList() {
        ListsRequest lists = new ListsRequest();
        lists.setId(1L);
        lists.setListName(TestConstants.LIST_NAME);
        lists.setDescription(TestConstants.LIST_DESCRIPTION);
        lists.setWallpaper("");
        lists.setIsPrivate(false);
        BaseListProjection baseListProjection = ListsServiceTestHelper.createMockBaseListProjection(TestConstants.USER_ID);
        when(listsRepository.getListByIdAndUserId(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(Optional.of(ListsServiceTestHelper.createMockLists()));
        when(listsRepository.getListById(TestConstants.LIST_ID, TestConstants.USER_ID, BaseListProjection.class)).thenReturn(Optional.of(baseListProjection));
        BaseListProjection baseList = listsService.editTweetList(lists);
        assertEquals(baseListProjection, baseList);
        assertEquals(TestConstants.LIST_NAME, baseList.getListName());
        assertEquals(TestConstants.LIST_DESCRIPTION, baseList.getDescription());
        assertEquals("", baseList.getWallpaper());
        assertFalse(baseList.getIsPrivate());
        verify(listsRepository, times(1)).getListById(TestConstants.LIST_ID, TestConstants.USER_ID, BaseListProjection.class);
    }

    @Test
    public void editTweetList_shouldReturnNotFound() {
        when(listsRepository.getListByIdAndUserId(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> listsService.editTweetList(new ListsRequest()));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void editTweetList_shouldEmptyListNameAndReturnException() {
        ListsRequest lists = new ListsRequest();
        lists.setId(1L);
        lists.setListName("");
        when(listsRepository.getListByIdAndUserId(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(Optional.of(ListsServiceTestHelper.createMockLists()));
        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> listsService.editTweetList(lists));
        assertEquals(ListsErrorMessage.INCORRECT_LIST_NAME_LENGTH, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void editTweetList_shouldLargeListNameAndReturnException() {
        ListsRequest lists = new ListsRequest();
        lists.setId(1L);
        lists.setListName("**************************");
        when(listsRepository.getListByIdAndUserId(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(Optional.of(ListsServiceTestHelper.createMockLists()));
        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> listsService.editTweetList(lists));
        assertEquals(ListsErrorMessage.INCORRECT_LIST_NAME_LENGTH, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void deleteList() {
        Lists list = ListsServiceTestHelper.createMockLists();
        when(listsRepository.getListByIdAndUserId(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(Optional.of(list));
        assertEquals(String.format(ListsSuccessMessage.LIST_DELETED, TestConstants.LIST_ID), listsService.deleteList(TestConstants.LIST_ID));
        verify(listsRepository, times(1)).getListByIdAndUserId(TestConstants.LIST_ID, TestConstants.USER_ID);
        verify(listsRepository, times(1)).delete(list);
    }

    @Test
    public void deleteList_shouldListNotFound() {
        when(listsRepository.getListByIdAndUserId(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.deleteList(TestConstants.LIST_ID));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void followList_followSuccess() {
        ListUserProjection listUser = ListsServiceTestHelper.createMockListUserProjectionList().get(0);
        Lists lists = ListsServiceTestHelper.createMockLists();
        User user = mockUser(TestConstants.USER_ID);
        when(listsRepository.getListByIdAndIsPrivateFalse(TestConstants.LIST_ID)).thenReturn(Optional.of(lists));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        when(listsRepository.getListById(TestConstants.LIST_ID, ListUserProjection.class)).thenReturn(listUser);
        assertEquals(listUser, listsService.followList(TestConstants.LIST_ID));
        assertEquals(1, lists.getListsFollowers().size());
        verify(listsRepository, times(1)).getListByIdAndIsPrivateFalse(TestConstants.LIST_ID);
        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
        verify(listsRepository, times(1)).getListById(TestConstants.LIST_ID, ListUserProjection.class);
    }

    @Test
    public void followList_unfollowSuccess() {
        ListUserProjection listUser = ListsServiceTestHelper.createMockListUserProjectionList().get(0);
        Lists lists = ListsServiceTestHelper.createMockLists();
        User user = mockUser(TestConstants.USER_ID);
        lists.setListsFollowers(new HashSet<>(Set.of(user)));
        when(listsRepository.getListByIdAndIsPrivateFalse(TestConstants.LIST_ID)).thenReturn(Optional.of(lists));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        when(listsRepository.getListById(TestConstants.LIST_ID, ListUserProjection.class)).thenReturn(listUser);
        assertEquals(listUser, listsService.followList(TestConstants.LIST_ID));
        assertEquals(0, lists.getListsFollowers().size());
        verify(listsRepository, times(1)).getListByIdAndIsPrivateFalse(TestConstants.LIST_ID);
        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
        verify(listsRepository, times(1)).getListById(TestConstants.LIST_ID, ListUserProjection.class);
    }

    @Test
    public void followList_shoutReturnListNotFoundException() {
        when(listsRepository.getListByIdAndIsPrivateFalse(TestConstants.LIST_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.followList(TestConstants.LIST_ID));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void pinList_pinSuccess() {
        PinnedListProjection pinnedList = ListsServiceTestHelper.createMockPinnedListProjectionList().get(0);
        Lists mockLists = ListsServiceTestHelper.createMockLists();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(mockUser(TestConstants.USER_ID)));
        when(listsRepository.getListWhereUserConsist(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(Optional.of(mockLists));
        when(listsRepository.getListById(TestConstants.LIST_ID, PinnedListProjection.class)).thenReturn(pinnedList);
        assertEquals(pinnedList, listsService.pinList(TestConstants.LIST_ID));
        assertEquals(1, mockLists.getPinnedLists().size());
    }

    @Test
    public void pinList_unpinSuccess() {
        PinnedListProjection pinnedList = ListsServiceTestHelper.createMockPinnedListProjectionList().get(0);
        User user = mockUser(TestConstants.USER_ID);
        Lists mockLists = ListsServiceTestHelper.createMockLists();
        mockLists.getPinnedLists().add(new PinnedList(mockLists, user));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        when(listsRepository.getListWhereUserConsist(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(Optional.of(mockLists));
        when(listsRepository.getListById(TestConstants.LIST_ID, PinnedListProjection.class)).thenReturn(pinnedList);
        assertEquals(pinnedList, listsService.pinList(TestConstants.LIST_ID));
        assertEquals(0, mockLists.getPinnedLists().size());
    }

    @Test
    public void pinList_shouldReturnListNotFound() {
        User user = mockUser(TestConstants.USER_ID);
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        when(listsRepository.getListWhereUserConsist(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.pinList(TestConstants.LIST_ID));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getListsToAddUser() {
        when(listsRepository.getUserOwnerLists(TestConstants.USER_ID))
                .thenReturn(ListsServiceTestHelper.createMockSimpleListProjectionList());
        assertNotNull(listsService.getListsToAddUser(1L));
        verify(listsRepository, times(1)).getUserOwnerLists(TestConstants.USER_ID);
        verify(listsRepository, times(1)).isListIncludeUser(1L, TestConstants.USER_ID, 1L);
        verify(listsRepository, times(1)).isListIncludeUser(2L, TestConstants.USER_ID, 1L);
    }

    @Test
    public void addUserToLists() {
        UserToListsRequest listsRequest = ListsServiceTestHelper.mockUserToListsRequest();
        Lists mockLists1 = ListsServiceTestHelper.createMockLists();
        Lists mockLists2 = ListsServiceTestHelper.createMockLists();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(mockUser(TestConstants.USER_ID)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser(1L)));
        when(userRepository.isUserBlocked(TestConstants.USER_ID, 1L)).thenReturn(false);
        when(userRepository.isUserBlocked(1L, TestConstants.USER_ID)).thenReturn(false);
        when(userRepository.isUserHavePrivateProfile(1L, TestConstants.USER_ID)).thenReturn(true);
        when(listsRepository.getListWhereUserConsist(listsRequest.getLists().get(0).getListId(), TestConstants.USER_ID)).thenReturn(Optional.of(mockLists1));
        when(listsRepository.getListWhereUserConsist(listsRequest.getLists().get(1).getListId(), TestConstants.USER_ID)).thenReturn(Optional.of(mockLists2));
        assertEquals(ListsSuccessMessage.USER_ADDED_TO_LISTS, listsService.addUserToLists(listsRequest));
        verify(listsRepository, times(1)).getListWhereUserConsist(listsRequest.getLists().get(0).getListId(), TestConstants.USER_ID);
        verify(listsRepository, times(1)).getListWhereUserConsist(listsRequest.getLists().get(1).getListId(), TestConstants.USER_ID);
        assertEquals(1, mockLists1.getListsMembers().size());
        assertEquals(1, mockLists2.getListsMembers().size());
//        verify(notificationClient, times(2)).sendNotification(any());
    }

    @Test
    public void addUserToLists_shouldCheckUserIsBlockedAndReturnBlockedUser() {
        UserToListsRequest listsRequest = ListsServiceTestHelper.mockUserToListsRequest();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(mockUser(TestConstants.USER_ID)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser(1L)));
        when(userRepository.isUserBlocked(TestConstants.USER_ID, listsRequest.getUserId())).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.addUserToLists(listsRequest));
        assertEquals(String.format(ListsErrorMessage.USER_ID_BLOCKED, listsRequest.getUserId()), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void addUserToLists_shouldCheckUserIsBlockedMyProfileAndReturnBlockedUser() {
        UserToListsRequest listsRequest = ListsServiceTestHelper.mockUserToListsRequest();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(mockUser(TestConstants.USER_ID)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser(1L)));
        when(userRepository.isUserBlocked(listsRequest.getUserId(), TestConstants.USER_ID)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.addUserToLists(listsRequest));
        assertEquals(String.format(ListsErrorMessage.USER_ID_BLOCKED, TestConstants.USER_ID), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void addUserToLists_shouldCheckUserPrivateProfileAndReturnUserNotFound() {
        UserToListsRequest listsRequest = ListsServiceTestHelper.mockUserToListsRequest();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(mockUser(TestConstants.USER_ID)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser(1L)));
        when(userRepository.isUserHavePrivateProfile(1L, TestConstants.USER_ID)).thenReturn(false);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.addUserToLists(listsRequest));
        assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void addUserToLists_shouldCheckIsListExistsAndReturnListNotFound() {
        UserToListsRequest listsRequest = ListsServiceTestHelper.mockUserToListsRequest();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(mockUser(TestConstants.USER_ID)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser(1L)));
        when(userRepository.isUserHavePrivateProfile(1L, TestConstants.USER_ID)).thenReturn(true);
        when(listsRepository.getListWhereUserConsist(1L, TestConstants.USER_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.addUserToLists(listsRequest));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void addUserToList_addUser() {
        Lists mockLists = ListsServiceTestHelper.createMockLists();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(mockUser(TestConstants.USER_ID)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser(1L)));
        when(userRepository.isUserBlocked(TestConstants.USER_ID, 1L)).thenReturn(false);
        when(userRepository.isUserBlocked(1L, TestConstants.USER_ID)).thenReturn(false);
        when(userRepository.isUserHavePrivateProfile(1L, TestConstants.USER_ID)).thenReturn(true);
        when(listsRepository.getListWhereUserConsist(1L, TestConstants.USER_ID)).thenReturn(Optional.of(mockLists));
        assertTrue(listsService.addUserToList(1L, TestConstants.LIST_ID));
        assertEquals(1, mockLists.getListsMembers().size());
//        verify(notificationClient, times(1)).sendNotification(any());
    }

    @Test
    public void addUserToList_removeUser() {
        Lists mockLists = ListsServiceTestHelper.createMockLists();
        User user = mockUser(1L);
        mockLists.getListsMembers().add(user);
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(mockUser(TestConstants.USER_ID)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser(1L)));
        when(userRepository.isUserBlocked(TestConstants.USER_ID, 1L)).thenReturn(false);
        when(userRepository.isUserBlocked(1L, TestConstants.USER_ID)).thenReturn(false);
        when(userRepository.isUserHavePrivateProfile(1L, TestConstants.USER_ID)).thenReturn(true);
        when(listsRepository.getListWhereUserConsist(1L, TestConstants.USER_ID)).thenReturn(Optional.of(mockLists));
        assertFalse(listsService.addUserToList(1L, TestConstants.LIST_ID));
        assertEquals(0, mockLists.getListsMembers().size());
    }

    @Test
    public void addUserToList_shouldCheckUserIsBlockedAndReturnBlockedUser() {
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(mockUser(TestConstants.USER_ID)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser(1L)));
        when(userRepository.isUserBlocked(TestConstants.USER_ID, 1L)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.addUserToList(1L, TestConstants.LIST_ID));
        assertEquals(String.format(ListsErrorMessage.USER_ID_BLOCKED, 1L), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void addUserToList_shouldCheckUserIsBlockedMyProfileAndReturnBlockedUser() {
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(mockUser(TestConstants.USER_ID)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser(1L)));
        when(userRepository.isUserBlocked(1L, TestConstants.USER_ID)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.addUserToList(1L, TestConstants.LIST_ID));
        assertEquals(String.format(ListsErrorMessage.USER_ID_BLOCKED, TestConstants.USER_ID), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void addUserToList_shouldCheckUserPrivateProfileAndReturnUserNotFound() {
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(mockUser(TestConstants.USER_ID)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser(1L)));
        when(userRepository.isUserHavePrivateProfile(1L, TestConstants.USER_ID)).thenReturn(false);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.addUserToList(1L, TestConstants.LIST_ID));
        assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void addUserToList_shouldCheckIsListExistsAndReturnListNotFound() {
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(mockUser(TestConstants.USER_ID)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser(1L)));
        when(userRepository.isUserHavePrivateProfile(1L, TestConstants.USER_ID)).thenReturn(true);
        when(listsRepository.getListWhereUserConsist(1L, TestConstants.USER_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.addUserToList(1L, TestConstants.LIST_ID));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getTweetsByListId() {
        PageRequest pageable = PageRequest.of(0, 20);
        List<Long> membersIds = List.of(1L, 2L, 3L);
        Lists mockLists = ListsServiceTestHelper.createMockLists();
        mockLists.getListsMembers().add(mockUser(1L));
        mockLists.getListsMembers().add(mockUser(2L));
        mockLists.getListsMembers().add(mockUser(3L));
        HeaderResponse<TweetResponse> headerResponse = new HeaderResponse<>(
                List.of(new TweetResponse(), new TweetResponse()), new HttpHeaders());
        when(listsRepository.getNotPrivateList(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(Optional.of(mockLists));
        when(tweetClient.getTweetsByUserIds(new IdsRequest(membersIds), pageable)).thenReturn(headerResponse);
        assertEquals(headerResponse, listsService.getTweetsByListId(TestConstants.LIST_ID, pageable));
        verify(listsRepository, times(1)).getNotPrivateList(TestConstants.LIST_ID, TestConstants.USER_ID);
        verify(tweetClient, times(1)).getTweetsByUserIds(new IdsRequest(membersIds), pageable);
    }

    @Test
    public void getTweetsByListId_shouldListNotFound() {
        when(listsRepository.getNotPrivateList(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.getTweetsByListId(TestConstants.LIST_ID, PageRequest.of(0, 20)));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getListDetails() {
        BaseListProjection baseListProjection = ListsServiceTestHelper.createMockBaseListProjection(TestConstants.USER_ID);
        when(listsRepository.getListDetails(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(Optional.of(baseListProjection));
        assertEquals(baseListProjection, listsService.getListDetails(TestConstants.LIST_ID));
        verify(listsRepository, times(1)).getListDetails(TestConstants.LIST_ID, TestConstants.USER_ID);
    }

    @Test
    public void getListDetails_shouldReturnListNotFound() {
        when(listsRepository.getListDetails(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.getListDetails(TestConstants.LIST_ID));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getListFollowers_shouldGetAuthsUserFollowersList() {
        List<User> users = List.of(mockUser(1L), mockUser(2L));
        when(listsRepository.isListExist(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(true);
        when(listsRepository.getFollowersByListId(TestConstants.LIST_ID)).thenReturn(users);
        assertEquals(users, listsService.getListFollowers(TestConstants.LIST_ID, TestConstants.USER_ID));
        verify(listsRepository, times(1)).isListExist(TestConstants.LIST_ID, TestConstants.USER_ID);
        verify(listsRepository, times(1)).getFollowersByListId(TestConstants.LIST_ID);
    }

    @Test
    public void getListFollowers_shouldGetAuthsUserAndReturnListNotFound() {
        when(listsRepository.isListExist(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(false);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.getListFollowers(TestConstants.LIST_ID, TestConstants.USER_ID));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getListFollowers_shouldGetUserFollowersList() {
        List<User> users = List.of(mockUser(1L), mockUser(2L));
        when(userRepository.isUserBlocked(3L, TestConstants.USER_ID)).thenReturn(false);
        when(listsRepository.isListExist(TestConstants.LIST_ID, 3L)).thenReturn(true);
        when(listsRepository.isListPrivate(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(false);
        when(listsRepository.isListFollowed(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(false);
        when(listsRepository.getFollowersByListId(TestConstants.LIST_ID)).thenReturn(users);
        assertEquals(users, listsService.getListFollowers(TestConstants.LIST_ID, 3L));
        verify(userRepository, times(1)).isUserBlocked(3L, TestConstants.USER_ID);
        verify(listsRepository, times(1)).isListExist(TestConstants.LIST_ID, 3L);
        verify(listsRepository, times(1)).isListPrivate(TestConstants.LIST_ID, TestConstants.USER_ID);
        verify(listsRepository, times(1)).getFollowersByListId(TestConstants.LIST_ID);
    }

    @Test
    public void getListFollowers_shouldGetUserAndReturnUserBlocked() {
        when(userRepository.isUserBlocked(3L, TestConstants.USER_ID)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.getListFollowers(TestConstants.LIST_ID, 3L));
        assertEquals(String.format(ListsErrorMessage.USER_ID_BLOCKED, TestConstants.USER_ID), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void getListFollowers_shouldGetUserAndReturnListNotFound() {
        when(userRepository.isUserBlocked(3L, TestConstants.USER_ID)).thenReturn(false);
        when(listsRepository.isListExist(TestConstants.LIST_ID, 3L)).thenReturn(false);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.getListFollowers(TestConstants.LIST_ID, 3L));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getListFollowers_shouldGetUserAndReturnListPrivate() {
        when(userRepository.isUserBlocked(3L, TestConstants.USER_ID)).thenReturn(false);
        when(listsRepository.isListExist(TestConstants.LIST_ID, 3L)).thenReturn(true);
        when(listsRepository.isListPrivate(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.getListFollowers(TestConstants.LIST_ID, 3L));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getListMembers_shouldGetAuthsUserFollowersList() {
        when(listsRepository.isListExist(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(true);
        assertNotNull(listsService.getListMembers(TestConstants.LIST_ID, TestConstants.USER_ID));
        verify(listsRepository, times(1)).isListExist(TestConstants.LIST_ID, TestConstants.USER_ID);
    }

    @Test
    public void getListMembers_shouldGetAuthsUserAndReturnListNotFound() {
        when(listsRepository.isListExist(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(false);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.getListMembers(TestConstants.LIST_ID, TestConstants.USER_ID));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getListMembers_shouldGetUserFollowersList() {
        when(userRepository.isUserBlocked(3L, TestConstants.USER_ID)).thenReturn(false);
        when(listsRepository.isListExist(TestConstants.LIST_ID, 3L)).thenReturn(true);
        when(listsRepository.isListPrivate(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(false);
        when(listsRepository.isListFollowed(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(false);
        when(listsRepository.getMembersByListId(TestConstants.LIST_ID)).thenReturn(List.of());
        assertNotNull(listsService.getListMembers(TestConstants.LIST_ID, 3L));
        verify(listsRepository, times(1)).isListExist(TestConstants.LIST_ID, 3L);

    }

    @Test
    public void getListMembers_shouldGetUserAndReturnUserBlocked() {
        when(userRepository.isUserBlocked(3L, TestConstants.USER_ID)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.getListMembers(TestConstants.LIST_ID, 3L));
        assertEquals(String.format(ListsErrorMessage.USER_ID_BLOCKED, TestConstants.USER_ID), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void getListMembers_shouldGetUserAndReturnListNotFound() {
        when(userRepository.isUserBlocked(3L, TestConstants.USER_ID)).thenReturn(false);
        when(listsRepository.isListExist(TestConstants.LIST_ID, 3L)).thenReturn(false);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.getListMembers(TestConstants.LIST_ID, 3L));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getListMembers_shouldGetUserAndReturnListPrivate() {
        when(userRepository.isUserBlocked(3L, TestConstants.USER_ID)).thenReturn(false);
        when(listsRepository.isListExist(TestConstants.LIST_ID, 3L)).thenReturn(true);
        when(listsRepository.isListPrivate(TestConstants.LIST_ID, TestConstants.USER_ID)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> listsService.getListMembers(TestConstants.LIST_ID, 3L));
        assertEquals(ListsErrorMessage.LIST_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void searchListMembersByUsername() {
        List<User> users = List.of(mockUser(1L), mockUser(2L));
        when(userRepository.searchListMembersByUsername(TestConstants.USERNAME)).thenReturn(users);
        assertNotNull(listsService.searchListMembersByUsername(TestConstants.LIST_ID, TestConstants.USERNAME));
        verify(userRepository, times(1)).searchListMembersByUsername(TestConstants.USERNAME);
    }
}
