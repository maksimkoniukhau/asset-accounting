package com.maks.assetaccounting.service;

import com.maks.assetaccounting.converter.UserConverter;
import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.entity.Role;
import com.maks.assetaccounting.entity.User;
import com.maks.assetaccounting.repository.UserRepository;
import com.maks.assetaccounting.service.user.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;

    @Before
    public void init() {
        user = new User();
        user.setId(5L);
        user.setUsername("Vasia");
        user.setPassword("12345");
        user.setFirstName("Vasiliy");
        user.setLastName("Ivanov");
        user.setEmail("vasia@mail.ru");
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.ROLE_ADMIN));
    }

    @Test
    public void testCreate() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userConverter.convertToCreateEntity(any(UserDto.class))).thenReturn(user);
        when(userConverter.convertToDto(any(User.class))).thenReturn(getUserDto());
        when(passwordEncoder.encode(anyString())).thenReturn("12345");

        assertEquals(userServiceImpl.create(getUserDto()), getUserDto());

        verify(userRepository, times(1)).save(any(User.class));
        verify(userConverter, times(1)).convertToCreateEntity(any(UserDto.class));
        verify(userConverter, times(1)).convertToDto(any(User.class));
    }

    @Test
    public void testGet() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userConverter.convertToDto(any(User.class))).thenReturn(getUserDto());

        assertEquals(userServiceImpl.get(5L), getUserDto());

        verify(userRepository, times(1)).findById(anyLong());
        verify(userConverter, times(1)).convertToDto(any(User.class));
    }

    @Test
    public void testUpdate() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userConverter.convertToEntity(any(UserDto.class))).thenReturn(user);
        when(userConverter.convertToDto(any(User.class))).thenReturn(getUserDto());

        assertEquals(userServiceImpl.update(getUserDto(), 5L), getUserDto());

        verify(userRepository, times(1)).save(any(User.class));
        verify(userConverter, times(1)).convertToEntity(any(UserDto.class));
        verify(userConverter, times(1)).convertToDto(any(User.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithIllegalArgument() {
        userServiceImpl.update(getUserDto(), 3L);
    }

    @Test
    public void testDelete() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userConverter.convertToDto(any(User.class))).thenReturn(getUserDto());

        assertEquals(userServiceImpl.delete(anyLong()), getUserDto());

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).deleteById(anyLong());
        verify(userConverter, times(1)).convertToDto(any(User.class));
    }

    @Test
    public void testGetAll() {
        final List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user);

        final List<UserDto> dtoList = new ArrayList<>();
        dtoList.add(getUserDto());
        dtoList.add(getUserDto());

        when(userRepository.findAll()).thenReturn(userList);
        when(userConverter.convertListToDto(anyList())).thenReturn(dtoList);

        assertEquals(userServiceImpl.getAll(), dtoList);

        verify(userRepository, times(1)).findAll();
        verify(userConverter, times(1)).convertListToDto(anyList());
    }

    @Test
    public void testGetByName() {
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userConverter.convertToDto(any(User.class))).thenReturn(getUserDto());

        assertEquals(userServiceImpl.getByName(anyString()), getUserDto());

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userConverter, times(1)).convertToDto(any(User.class));
    }

    private UserDto getUserDto() {
        final UserDto userDto = new UserDto();
        userDto.setId(5L);
        userDto.setUsername("Vasia");
        userDto.setPassword("12345");
        userDto.setFirstName("Vasiliy");
        userDto.setLastName("Ivanov");
        userDto.setEmail("vasia@mail.ru");
        userDto.setActive(true);
        userDto.setRoles(Collections.singleton(Role.ROLE_ADMIN));
        return userDto;
    }
}