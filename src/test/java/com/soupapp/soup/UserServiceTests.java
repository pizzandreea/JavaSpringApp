package com.soupapp.soup;

import com.soupapp.soup.dtos.user.LoginMessage;
import com.soupapp.soup.dtos.user.UserLoginDto;
import com.soupapp.soup.dtos.user.UserRegisterDto;
import com.soupapp.soup.dtos.user.UserResponseDto;
import com.soupapp.soup.models.User;
import com.soupapp.soup.repositories.UserRepository;
import com.soupapp.soup.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTests {
    private UserRepository userMockRepository;

    private UserService userService;

    @BeforeEach
    void init() {
        userMockRepository = mock(UserRepository.class);
        userService = new UserService(userMockRepository);
    }

    @Test
    void createNewUser() {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setEmail("test@example.com");

        List<User> existingUsers = new ArrayList<>();
        existingUsers.add(new User() {{
            setEmail("test@example.com");
        }});

        when(userMockRepository.findAll()).thenReturn(existingUsers);
        when(userMockRepository.save(any(User.class))).thenReturn(new User());

        userService.create(userRegisterDto);

        verify(userMockRepository, times(1)).findAll();
        verify(userMockRepository, times(1)).save(any(User.class));

        List<User> savedUsers = getAllSavedUsers();
        assertEquals(1, savedUsers.size());
    }
    private List<User> getAllSavedUsers() {
        return userMockRepository.findAll();
    }


    @Test
    void loginUserSuccess() {
        UserLoginDto request = new UserLoginDto();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userMockRepository.findByEmail(request.getEmail())).thenReturn(user);
        when(userMockRepository.findOneByEmailAndPassword(request.getEmail(), user.getPassword()))
                .thenReturn(Optional.of(user));

        LoginMessage loginMessage = userService.login(request);

        assertNotNull(loginMessage);
        assertEquals(true, loginMessage.getStatus());
        assertEquals("Login Success", loginMessage.getMessage());
    }

    @Test
    void loginUserWrongPassword() {
        UserLoginDto request = new UserLoginDto();
        request.setEmail("test@example.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userMockRepository.findByEmail(request.getEmail())).thenReturn(user);

        LoginMessage loginMessage = userService.login(request);

        assertNotNull(loginMessage);
        assertFalse(loginMessage.getStatus());
        assertEquals("password Not Match", loginMessage.getMessage());
    }

    @Test
    void loginUserEmailNotFound() {
        UserLoginDto request = new UserLoginDto();
        request.setEmail("nonexistent@example.com");
        request.setPassword("password");

        when(userMockRepository.findByEmail(request.getEmail())).thenReturn(null);

        LoginMessage loginMessage = userService.login(request);

        assertNotNull(loginMessage);
        assertFalse(loginMessage.getStatus());
        assertEquals("Email not exits", loginMessage.getMessage());
    }

    @Test
    void getAllUsers() {
        List<User> users = List.of(new User(), new User());

        when(userMockRepository.findAll()).thenReturn(users);

        List<UserResponseDto> response = userService.getAll();

        assertEquals(users.size(), response.size());

        verify(userMockRepository, times(1)).findAll();
    }
}
