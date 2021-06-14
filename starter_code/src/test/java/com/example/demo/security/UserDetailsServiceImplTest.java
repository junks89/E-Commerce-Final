package com.example.demo.security;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {

    private final UserRepo userRepo = mock(UserRepo.class);
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Before
    public void setUp() {
        userDetailsServiceImpl = new UserDetailsServiceImpl();
        com.example.demo.TestUtils.injectObjects(userDetailsServiceImpl, "userRepository", userRepo);
    }

    @Test
    public void load_user_by_loadUserByUsername_method() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("password1234");
        user.setId(0L);
        when(userRepo.findByUsername("user1")).thenReturn(user);

        UserDetails userData = userDetailsServiceImpl.loadUserByUsername("user1");
        assertNotNull(userData);
        /*
        https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/userdetails/UserDetails.html
        * */
        Collection<? extends GrantedAuthority> authorityCollection = userData.getAuthorities();
        assertNotNull(authorityCollection);
        assertEquals("user1", userData.getUsername());
        assertEquals("password1234", userData.getPassword());

        assertEquals(0, authorityCollection.size());
    }
}
