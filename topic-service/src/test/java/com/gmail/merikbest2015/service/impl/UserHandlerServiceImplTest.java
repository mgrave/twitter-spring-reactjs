package com.gmail.merikbest2015.service.impl;

import com.gmail.merikbest2015.commons.util.TestConstants;
import com.gmail.merikbest2015.service.AbstractServiceTest;
import com.gmail.merikbest2015.TopicTestHelper;
import com.gmail.merikbest2015.commons.event.BlockUserEvent;
import com.gmail.merikbest2015.commons.event.FollowUserEvent;
import com.gmail.merikbest2015.commons.event.UpdateUserEvent;
import com.gmail.merikbest2015.commons.event.UserEvent;
import com.gmail.merikbest2015.model.User;
import com.gmail.merikbest2015.service.UserHandlerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserHandlerServiceImplTest extends AbstractServiceTest {

    @Autowired
    private UserHandlerService userHandlerService;

    @Test
    public void handleUpdateUser_updateUser() {
        User authUser = TopicTestHelper.mockAuthUser();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        assertFalse(authUser.isPrivateProfile());
        userHandlerService.handleNewOrUpdateUser(mockUpdateUserEvent());
        assertTrue(authUser.isPrivateProfile());
    }

    @Test
    public void handleUpdateUser_createUser() {
        UpdateUserEvent updateUserEvent = mockUpdateUserEvent();
        User user = mockCreateUser(updateUserEvent);
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.empty());
        userHandlerService.handleNewOrUpdateUser(updateUserEvent);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void handleBlockUser_blockUser() {
        BlockUserEvent blockUserEvent = mockBlockUserEvent(true);
        User user = mockCreateUser(blockUserEvent);
        User authUser = TopicTestHelper.mockAuthUser();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        userHandlerService.handleBlockUser(blockUserEvent, TestConstants.USER_ID.toString());
        assertTrue(authUser.getUserBlockedList().contains(user));
    }

    @Test
    public void handleBlockUser_unblockUser() {
        BlockUserEvent blockUserEvent = mockBlockUserEvent(false);
        User user = mockCreateUser(blockUserEvent);
        User authUser = TopicTestHelper.mockAuthUser();
        authUser.setUserBlockedList(new HashSet<>(Set.of(user)));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        userHandlerService.handleBlockUser(blockUserEvent, TestConstants.USER_ID.toString());
        assertTrue(authUser.getUserBlockedList().isEmpty());
    }

    @Test
    public void handleFollowUser_followUser() {
        FollowUserEvent followUserEvent = mockFollowUserEvent(true);
        User user = mockCreateUser(followUserEvent);
        User authUser = TopicTestHelper.mockAuthUser();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        userHandlerService.handleFollowUser(followUserEvent, TestConstants.USER_ID.toString());
        assertTrue(authUser.getFollowers().contains(user));
    }

    @Test
    public void handleFollowUser_unfollowUser() {
        FollowUserEvent followUserEvent = mockFollowUserEvent(false);
        User user = mockCreateUser(followUserEvent);
        User authUser = TopicTestHelper.mockAuthUser();
        authUser.setFollowers(new HashSet<>(Set.of(user)));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        userHandlerService.handleFollowUser(followUserEvent, TestConstants.USER_ID.toString());
        assertTrue(authUser.getFollowers().isEmpty());
    }

    private static UpdateUserEvent mockUpdateUserEvent() {
        return UpdateUserEvent.builder()
                .id(TestConstants.USER_ID)
                .fullName(TestConstants.FULL_NAME)
                .username(TestConstants.USERNAME)
                .privateProfile(true)
                .build();
    }

    private static BlockUserEvent mockBlockUserEvent(boolean userBlocked) {
        return BlockUserEvent.builder()
                .id(TestConstants.USER_ID)
                .fullName(TestConstants.FULL_NAME)
                .username(TestConstants.USERNAME)
                .privateProfile(false)
                .userBlocked(userBlocked)
                .build();
    }

    private static FollowUserEvent mockFollowUserEvent(boolean userFollow) {
        return FollowUserEvent.builder()
                .id(TestConstants.USER_ID)
                .fullName(TestConstants.FULL_NAME)
                .username(TestConstants.USERNAME)
                .privateProfile(false)
                .userFollow(userFollow)
                .build();
    }

    private static User mockCreateUser(UserEvent userEvent) {
        User newUser = new User();
        newUser.setId(userEvent.getId());
        newUser.setUsername(userEvent.getUsername());
        newUser.setFullName(userEvent.getFullName());
        newUser.setPrivateProfile(userEvent.isPrivateProfile());
        return newUser;
    }
}
