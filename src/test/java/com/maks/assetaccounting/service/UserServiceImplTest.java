package com.maks.assetaccounting.service;

import com.maks.assetaccounting.converter.user.UserToBackConverter;
import com.maks.assetaccounting.converter.user.UserToFrontConverter;
import com.maks.assetaccounting.converter.user.UserToSaveConverter;
import com.maks.assetaccounting.dto.user.UserToBackDto;
import com.maks.assetaccounting.dto.user.UserToFrontDto;
import com.maks.assetaccounting.entity.Role;
import com.maks.assetaccounting.entity.User;
import com.maks.assetaccounting.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

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
    @Spy
    private UserToBackConverter userToBackConverter;
    @Spy
    private UserToFrontConverter userToFrontConverter;
    @Spy
    private UserToSaveConverter userToSaveConverter;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;

    @Before
    public void init() {
        user = new User();
        user.setId(5L);
        user.setUsername("Vasia");
        user.setPassword("12345");
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
    }

    @Test
    public void testCreate() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertEquals(userServiceImpl.create(new UserToBackDto()), getUserToFrontDto());

        verify(userRepository, times(1)).save(any(User.class));
        verify(userToSaveConverter, times(1)).convertToEntity(any(UserToBackDto.class));
        verify(userToFrontConverter, times(1)).convertToDto(any(User.class));
    }

    @Test
    public void testGet() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertEquals(userServiceImpl.get(55L), getUserToFrontDto());

        verify(userRepository, times(1)).findById(anyLong());
        verify(userToFrontConverter, times(1)).convertToDto(any(User.class));
    }

    @Test
    public void testUpdate() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertEquals(userServiceImpl.update(getUserToBackDto(), 5L), getUserToFrontDto());

        verify(userRepository, times(1)).save(any(User.class));
        verify(userToBackConverter, times(1)).convertToEntity(any(UserToBackDto.class));
        verify(userToFrontConverter, times(1)).convertToDto(any(User.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithIllegalArgument() {
        userServiceImpl.update(getUserToBackDto(), 3L);
    }

    @Test
    public void testDelete() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertEquals(userServiceImpl.delete(anyLong()), getUserToFrontDto());

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).deleteById(anyLong());
        verify(userToFrontConverter, times(1)).convertToDto(any(User.class));
    }

    @Test
    public void testGetAll() {
        final List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user);

        final List<UserToFrontDto> dtoList = new ArrayList<>();
        dtoList.add(getUserToFrontDto());
        dtoList.add(getUserToFrontDto());

        when(userRepository.findAll()).thenReturn(userList);

        assertEquals(userServiceImpl.getAll(), dtoList);

        verify(userRepository, times(1)).findAll();
        verify(userToFrontConverter, times(1)).convertListToDto(anyList());
    }

    @Test
    public void testGetByName() {
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        userServiceImpl.getByName(anyString());

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    private UserToFrontDto getUserToFrontDto() {
        final UserToFrontDto userToFrontDto = new UserToFrontDto();
        userToFrontDto.setId(5L);
        userToFrontDto.setUsername("Vasia");
        userToFrontDto.setActive(true);
        userToFrontDto.setRoles(Collections.singleton(Role.USER));
        return userToFrontDto;
    }

    private UserToBackDto getUserToBackDto() {
        final UserToBackDto userToBackDto = new UserToBackDto();
        userToBackDto.setId(5L);
        userToBackDto.setUsername("Vasia");
        userToBackDto.setPassword("12345");
        userToBackDto.setActive(false);
        return userToBackDto;
    }
}