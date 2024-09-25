package com.gmail.merikbest2015.service;

import com.gmail.merikbest2015.UserServiceTestHelper;
import com.gmail.merikbest2015.commons.enums.BackgroundColorType;
import com.gmail.merikbest2015.commons.enums.ColorSchemeType;
import com.gmail.merikbest2015.commons.exception.ApiRequestException;
import com.gmail.merikbest2015.model.User;
import com.gmail.merikbest2015.model.UserRole;
import com.gmail.merikbest2015.repository.projection.AuthUserProjection;
import com.gmail.merikbest2015.commons.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;

import static com.gmail.merikbest2015.commons.constants.ErrorMessage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserSettingsServiceImplTest extends AbstractServiceTest {

    @Autowired
    private UserSettingsService userSettingsService;

    private User authUser;

    @Before
    public void setUp() {
        super.setUp();
        authUser = new User();
        authUser.setId(TestConstants.USER_ID);
    }

    @Test
    public void updateUsername_ShouldReturnUpdatedUsername() {
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        assertEquals(TestConstants.USERNAME, userSettingsService.updateUsername(TestConstants.USERNAME));
        assertEquals(TestConstants.USERNAME, authUser.getUsername());
        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
        verify(updateUserProducer, times(1)).sendUpdateUserEvent(authUser);
    }

    @Test
    public void updateUsername_ShouldThrowUsernameLengthException() {
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSettingsService.updateUsername(""));
        assertEquals(INCORRECT_USERNAME_LENGTH, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void updateEmail_ShouldReturnUpdatedUser() {
        AuthUserProjection authUserProjection = UserServiceTestHelper.createAuthUserProjection();
        when(userSettingsRepository.isEmailExist(TestConstants.USER_ID, TestConstants.USER_EMAIL)).thenReturn(false);
        when(jwtProvider.createToken(TestConstants.USER_EMAIL, UserRole.USER.name())).thenReturn(TestConstants.AUTH_TOKEN);
        when(userRepository.getUserById(TestConstants.USER_ID, AuthUserProjection.class)).thenReturn(Optional.of(authUserProjection));
        assertEquals(Map.of("user", authUserProjection, "token", TestConstants.AUTH_TOKEN),
                userSettingsService.updateEmail(TestConstants.USER_EMAIL));
        verify(userSettingsRepository, times(1)).isEmailExist(TestConstants.USER_ID, TestConstants.USER_EMAIL);
        verify(userSettingsRepository, times(1)).updateEmail(TestConstants.USER_EMAIL, TestConstants.USER_ID);
        verify(jwtProvider, times(1)).createToken(TestConstants.USER_EMAIL, UserRole.USER.name());
        verify(userRepository, times(1)).getUserById(TestConstants.USER_ID, AuthUserProjection.class);
    }

    @Test
    public void updateEmail_ShouldThrowEmailException() {
        when(userSettingsRepository.isEmailExist(TestConstants.USER_ID, TestConstants.USER_EMAIL)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSettingsService.updateEmail(TestConstants.USER_EMAIL));
        assertEquals(EMAIL_HAS_ALREADY_BEEN_TAKEN, exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    public void updatePhoneNumber_ShouldReturnUpdatedPhone() {
        when(countryCodeRepository.isPhoneCodeExists(TestConstants.PHONE_CODE)).thenReturn(true);
        assertEquals(Map.of("phoneCode", TestConstants.PHONE_CODE, "phoneNumber", TestConstants.PHONE_NUMBER),
                userSettingsService.updatePhoneNumber(TestConstants.PHONE_CODE, TestConstants.PHONE_NUMBER));
        verify(userSettingsRepository, times(1))
                .updatePhoneNumber(TestConstants.PHONE_CODE, TestConstants.PHONE_NUMBER, TestConstants.USER_ID);
    }

    @Test
    public void updatePhoneNumber_ShouldPhoneCodeNotFound() {
        when(countryCodeRepository.isPhoneCodeExists(TestConstants.PHONE_CODE)).thenReturn(false);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSettingsService.updatePhoneNumber(TestConstants.COUNTRY_CODE, TestConstants.PHONE_NUMBER));
        assertEquals(PHONE_CODE_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void updatePhoneNumber_ShouldThrowInvalidPhoneNumberException() {
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSettingsService.updatePhoneNumber(TestConstants.COUNTRY_CODE, 1L));
        assertEquals(INVALID_PHONE_NUMBER, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void deletePhoneNumber_ShouldDeletePhoneNumber() {
        assertEquals("Phone number deleted", userSettingsService.deletePhoneNumber());
        verify(userSettingsRepository, times(1)).updatePhoneNumber(null, null, TestConstants.USER_ID);
    }

    @Test
    public void updateCountry_ShouldReturnUpdatedCountry() {
        assertEquals(TestConstants.COUNTRY, userSettingsService.updateCountry(TestConstants.COUNTRY));
        verify(userSettingsRepository, times(1)).updateCountry(TestConstants.COUNTRY, TestConstants.USER_ID);
    }

    @Test
    public void updateGender_ShouldReturnUpdatedGender() {
        assertEquals(TestConstants.GENDER, userSettingsService.updateGender(TestConstants.GENDER));
        verify(userSettingsRepository, times(1)).updateGender(TestConstants.GENDER, TestConstants.USER_ID);
    }

    @Test
    public void updateGender_ShouldThrowInvalidGenderLengthException() {
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSettingsService.updateGender(""));
        assertEquals(INVALID_GENDER_LENGTH, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void updateLanguage_ShouldReturnUpdatedLanguage() {
        assertEquals(TestConstants.LANGUAGE, userSettingsService.updateLanguage(TestConstants.LANGUAGE));
        verify(userSettingsRepository, times(1)).updateLanguage(TestConstants.LANGUAGE, TestConstants.USER_ID);
    }

    @Test
    public void updateDirectMessageRequests_ShouldReturnUpdatedDirectMessage() {
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        assertTrue(userSettingsService.updateDirectMessageRequests(true));
        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
        verify(updateUserProducer, times(1)).sendUpdateUserEvent(authUser);
    }

    @Test
    public void updatePrivateProfile_ShouldReturnUpdatedPrivateProfile() {
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        assertTrue(userSettingsService.updatePrivateProfile(true));
        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
        verify(updateUserProducer, times(1)).sendUpdateUserEvent(authUser);
    }

    @Test
    public void updateColorScheme_ShouldReturnUpdatedColorScheme() {
        assertEquals(ColorSchemeType.BLUE, userSettingsService.updateColorScheme(ColorSchemeType.BLUE));
        verify(userSettingsRepository, times(1)).updateColorScheme(ColorSchemeType.BLUE, TestConstants.USER_ID);
    }

    @Test
    public void updateBackgroundColor_ShouldReturnUpdatedBackgroundColor() {
        assertEquals(BackgroundColorType.DIM, userSettingsService.updateBackgroundColor(BackgroundColorType.DIM));
        verify(userSettingsRepository, times(1)).updateBackgroundColor(BackgroundColorType.DIM, TestConstants.USER_ID);
    }
}
