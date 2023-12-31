package com.carrental.lib.util;

import com.carrental.document.model.User;
import com.carrental.dto.RegisterRequest;
import com.carrental.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssertionUtils {

    public static void assertUser(RegisterRequest registerRequest, User user) {
        assertEquals(registerRequest.getUsername(), user.getUsername());
        assertEquals(registerRequest.getPassword(), user.getPassword());
        assertEquals(registerRequest.getFirstName(), user.getFirstName());
        assertEquals(registerRequest.getLastName(), user.getLastName());
        assertEquals(registerRequest.getEmail(), user.getEmail());
    }

    public static void assertUser(User customer, UserDto customerDto) {
        assertEquals(customer.getFirstName(), customerDto.getFirstName());
        assertEquals(customer.getLastName(), customerDto.getLastName());
        assertEquals(customer.getEmail(), customerDto.getEmail());
    }

}
