package com.gmail.merikbest2015;

import com.gmail.merikbest2015.repository.projection.*;
import com.gmail.merikbest2015.commons.util.TestConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceTestHelper {

    private static final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
    private static final PageRequest pageable = PageRequest.of(0, 20);

    public static Page<BlockedUserProjection> createBlockedUserProjections() {
        BlockedUserProjection blockedUserProjection1 = factory.createProjection(
                BlockedUserProjection.class,
                Map.of(
                        "id", 1L,
                        "fullName", TestConstants.FULL_NAME,
                        "username", TestConstants.USERNAME,
                        "about", TestConstants.ABOUT,
                        "avatar", TestConstants.AVATAR_SRC_1,
                        "privateProfile", false,
                        "isUserBlocked", true
                ));
        BlockedUserProjection blockedUserProjection2 = factory.createProjection(
                BlockedUserProjection.class,
                Map.of(
                        "id", 2L,
                        "fullName", TestConstants.FULL_NAME,
                        "username", TestConstants.USERNAME,
                        "about", TestConstants.ABOUT,
                        "avatar", TestConstants.AVATAR_SRC_1,
                        "privateProfile", false,
                        "isUserBlocked", true
                ));
        return new PageImpl<>(Arrays.asList(blockedUserProjection1, blockedUserProjection2), pageable, 20);
    }

    public static Page<MutedUserProjection> createMutedUserProjections() {
        MutedUserProjection mutedUserProjection1 = factory.createProjection(
                MutedUserProjection.class,
                Map.of(
                        "id", 1L,
                        "fullName", TestConstants.FULL_NAME,
                        "username", TestConstants.USERNAME,
                        "about", TestConstants.ABOUT,
                        "avatar", TestConstants.AVATAR_SRC_1,
                        "privateProfile", false,
                        "isUserMuted", true
                ));
        MutedUserProjection mutedUserProjection2 = factory.createProjection(
                MutedUserProjection.class,
                Map.of(
                        "id", 2L,
                        "fullName", TestConstants.FULL_NAME,
                        "username", TestConstants.USERNAME,
                        "about", TestConstants.ABOUT,
                        "avatar", TestConstants.AVATAR_SRC_1,
                        "privateProfile", false,
                        "isUserMuted", true
                ));
        return new PageImpl<>(Arrays.asList(mutedUserProjection1, mutedUserProjection2), pageable, 20);
    }

    public static Page<UserProjection> createUserProjections() {
        UserProjection userProjection1 = factory.createProjection(
                UserProjection.class,
                new HashMap<>() {{
                    put("id", 1L);
                    put("fullName", TestConstants.FULL_NAME);
                    put("username", TestConstants.USERNAME);
                    put("about", TestConstants.ABOUT);
                    put("avatar", TestConstants.AVATAR_SRC_1);
                    put("privateProfile", false);
                    put("mutedDirectMessages", false);
                    put("isUserBlocked", false);
                    put("isMyProfileBlocked", false);
                    put("isWaitingForApprove", false);
                    put("isFollower", false);
                }});
        UserProjection userProjection2 = factory.createProjection(
                UserProjection.class,
                new HashMap<>() {{
                    put("id", 1L);
                    put("fullName", TestConstants.FULL_NAME);
                    put("username", TestConstants.USERNAME);
                    put("about", TestConstants.ABOUT);
                    put("avatar", TestConstants.AVATAR_SRC_1);
                    put("privateProfile", false);
                    put("mutedDirectMessages", false);
                    put("isUserBlocked", false);
                    put("isMyProfileBlocked", false);
                    put("isWaitingForApprove", false);
                    put("isFollower", false);
                }});
        return new PageImpl<>(Arrays.asList(userProjection1, userProjection2), pageable, 20);
    }

    public static Page<FollowerUserProjection> createFollowerUserProjections() {
        FollowerUserProjection followerUserProjection1 = factory.createProjection(
                FollowerUserProjection.class,
                new HashMap<>() {{
                    put("id", 1L);
                    put("fullName", TestConstants.FULL_NAME);
                    put("username", TestConstants.USERNAME);
                    put("about", TestConstants.ABOUT);
                    put("avatar", TestConstants.AVATAR_SRC_1);
                }});
        FollowerUserProjection followerUserProjection2 = factory.createProjection(
                FollowerUserProjection.class,
                new HashMap<>() {{
                    put("id", 1L);
                    put("fullName", TestConstants.FULL_NAME);
                    put("username", TestConstants.USERNAME);
                    put("about", TestConstants.ABOUT);
                    put("avatar", TestConstants.AVATAR_SRC_1);
                }});
        return new PageImpl<>(Arrays.asList(followerUserProjection1, followerUserProjection2), pageable, 20);
    }

    public static List<BaseUserProjection> createBaseUserProjections() {
        BaseUserProjection baseUserProjection1 = factory.createProjection(
                BaseUserProjection.class,
                new HashMap<>() {{
                    put("id", 1L);
                    put("fullName", TestConstants.FULL_NAME);
                    put("username", TestConstants.USERNAME);
                    put("about", TestConstants.ABOUT);
                    put("avatar", TestConstants.AVATAR_SRC_1);
                    put("privateProfile", false);
                    put("isUserBlocked", false);
                    put("isMyProfileBlocked", false);
                    put("isWaitingForApprove", false);
                    put("isFollower", false);
                }});
        BaseUserProjection baseUserProjection2 = factory.createProjection(
                BaseUserProjection.class,
                new HashMap<>() {{
                    put("id", 1L);
                    put("fullName", TestConstants.FULL_NAME);
                    put("username", TestConstants.USERNAME);
                    put("about", TestConstants.ABOUT);
                    put("avatar", TestConstants.AVATAR_SRC_1);
                    put("privateProfile", false);
                    put("isUserBlocked", false);
                    put("isMyProfileBlocked", false);
                    put("isWaitingForApprove", false);
                    put("isFollower", false);
                }});

        return Arrays.asList(baseUserProjection1, baseUserProjection2);
    }

    public static UserProfileProjection createUserProfileProjection() {
        return factory.createProjection(
                UserProfileProjection.class,
                new HashMap<>() {{
                    put("id", 1L);
                    put("fullName", TestConstants.FULL_NAME);
                    put("username", TestConstants.USERNAME);
                    put("location", TestConstants.LOCATION);
                    put("about", TestConstants.ABOUT);
                    put("website", TestConstants.WEBSITE);
                    put("country", TestConstants.COUNTRY);
                    put("birthday", TestConstants.BIRTHDAY);
                    put("registrationDate", TestConstants.REGISTRATION_DATE);
                    put("tweetCount", TestConstants.TWEET_COUNT);
                    put("mediaTweetCount", TestConstants.MEDIA_TWEET_COUNT);
                    put("likeCount", TestConstants.LIKE_TWEET_COUNT);
                    put("isMutedDirectMessages", false);
                    put("isPrivateProfile", false);
                    put("avatar", TestConstants.AVATAR_SRC_1);
                    put("wallpaper", TestConstants.WALLPAPER_SRC);
                    put("pinnedTweetId", TestConstants.PINNED_TWEET_ID);
                    put("followersSize", 11L);
                    put("followingSize", 11L);
                    put("isUserMuted", false);
                    put("isUserBlocked", false);
                    put("isMyProfileBlocked", false);
                    put("isWaitingForApprove", false);
                    put("isFollower", false);
                    put("isSubscriber", false);
                }});
    }

    public static CommonUserProjection createCommonUserProjection() {
        return factory.createProjection(
                CommonUserProjection.class,
                Map.of(
                        "id", 1L,
                        "fullName", TestConstants.FULL_NAME,
                        "username", TestConstants.USERNAME,
                        "avatar", TestConstants.AVATAR_SRC_1,
                        "privateProfile", false
                ));
    }

    public static NotificationUserProjection createNotificationUserProjection() {
        return factory.createProjection(
                NotificationUserProjection.class,
                Map.of(
                        "id", 1L,
                        "fullName", TestConstants.FULL_NAME,
                        "username", TestConstants.USERNAME
                ));
    }

    public static AuthUserProjection createAuthUserProjection() {
        return factory.createProjection(
                AuthUserProjection.class,
                new HashMap<>() {{
                    put("id", 1L);
                    put("email", TestConstants.USER_EMAIL);
                    put("fullName", TestConstants.FULL_NAME);
                    put("username", TestConstants.USERNAME);
                    put("location", TestConstants.LOCATION);
                    put("about", TestConstants.ABOUT);
                    put("website", TestConstants.WEBSITE);
                    put("avatar", TestConstants.AVATAR_SRC_1);
                }});
    }

    public static UserDetailProjection createUserDetailProjection() {
        return factory.createProjection(
                UserDetailProjection.class,
                new HashMap<>() {{
                    put("id", 1L);
                    put("fullName", TestConstants.FULL_NAME);
                    put("username", TestConstants.USERNAME);
                    put("about", TestConstants.ABOUT);
                    put("avatar", TestConstants.AVATAR_SRC_1);
                    put("privateProfile", TestConstants.PRIVATE_PROFILE);
                }});
    }

    public static UserCommonProjection createUserCommonProjection() {
        return factory.createProjection(
                UserCommonProjection.class,
                Map.of(
                        "id", TestConstants.USER_ID,
                        "email", TestConstants.USER_EMAIL,
                        "fullName", TestConstants.FULL_NAME,
                        "activationCode", TestConstants.ACTIVATION_CODE,
                        "passwordResetCode", TestConstants.PASSWORD_RESET_CODE
                ));
    }
}
