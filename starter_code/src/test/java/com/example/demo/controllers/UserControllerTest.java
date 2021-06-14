package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepo;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;


    private final UserRepo userRepo = mock(UserRepo.class);
    private final CartRepository cartRepo = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode("password1")).thenReturn("password1hash");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("user1");
        r.setPassword("password1");
        r.setConfirmPassword("password1");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("user1", u.getUsername());
        assertEquals("password1hash", u.getPassword());
    }

    @Test
    public void find_user_by_id() {
        when(encoder.encode("password1")).thenReturn("password1hash");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("user1");
        r.setPassword("password1");
        r.setConfirmPassword("password1");
        final ResponseEntity<User> createdUserResponse = userController.createUser(r);
        assertNotNull(createdUserResponse);
        assertEquals(200, createdUserResponse.getStatusCodeValue());
        User createdUser = createdUserResponse.getBody();
        when(userRepo.findById(0L)).thenReturn(java.util.Optional.ofNullable(createdUser));
        final ResponseEntity<User> foundUserResponse = userController.findById(0L);
        assertNotNull(foundUserResponse);
        assertEquals(200, foundUserResponse.getStatusCodeValue());
        User foundUser = foundUserResponse.getBody();
        assertNotNull(foundUser);
        assertEquals(0, foundUser.getId());
        assertEquals("user1", foundUser.getUsername());
        assertEquals("password1hash", foundUser.getPassword());
    }

    @Test
    public void find_user_by_name() {
        when(encoder.encode("password1")).thenReturn("password1hash");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("user1");
        r.setPassword("password1");
        r.setConfirmPassword("password1");
        final ResponseEntity<User> createdUserResponse = userController.createUser(r);
        assertNotNull(createdUserResponse);
        assertEquals(200, createdUserResponse.getStatusCodeValue());
        User createdUser = createdUserResponse.getBody();
        when(userRepo.findByUsername("user1")).thenReturn(createdUser);
        final ResponseEntity<User> foundUserResponse = userController.findByUserName("user1");
        assertNotNull(foundUserResponse);
        assertEquals(200, foundUserResponse.getStatusCodeValue());
        User foundUser = foundUserResponse.getBody();
        assertNotNull(foundUser);
        assertEquals(0, foundUser.getId());
        assertEquals("user1", foundUser.getUsername());
        assertEquals("password1hash", foundUser.getPassword());
    }
}
