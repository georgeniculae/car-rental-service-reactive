package com.carrental.customer.mapper;

import com.carrental.document.dto.CurrentUserDto;
import com.carrental.customer.util.AssertionUtils;
import com.carrental.customer.util.TestUtils;
import com.carrental.document.model.User;
import com.carrental.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    private final CustomerMapper customerMapper = new CustomerMapperImpl();

    @Test
    void mapUserToCurrentUserDtoTest_success() {
        User user = TestUtils.getResourceAsJson("/data/User.json", User.class);

        CurrentUserDto currentUserDto = assertDoesNotThrow(() -> customerMapper.mapUserToCurrentUserDto(user));

        AssertionUtils.assertCurrentUser(user, currentUserDto);
    }

    @Test
    void mapUserToCurrentUserDtoTest_null() {
        CurrentUserDto currentUserDto = assertDoesNotThrow(() -> customerMapper.mapUserToCurrentUserDto(null));

        assertNull(currentUserDto);
    }

    @Test
    void mapEntityToDtoTest_success() {
        User user = TestUtils.getResourceAsJson("/data/User.json", User.class);

        UserDto userDto = assertDoesNotThrow(() -> customerMapper.mapEntityToDto(user));

        AssertionUtils.assertUser(user, userDto);
    }

    @Test
    void mapEntityToDtoTest_null() {
        UserDto userDto = assertDoesNotThrow(() -> customerMapper.mapEntityToDto(null));

        assertNull(userDto);
    }

}
