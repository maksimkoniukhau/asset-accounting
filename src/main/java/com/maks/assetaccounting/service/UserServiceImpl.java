package com.maks.assetaccounting.service;

import com.maks.assetaccounting.converter.user.UserToBackConverter;
import com.maks.assetaccounting.converter.user.UserToFrontConverter;
import com.maks.assetaccounting.converter.user.UserToSaveConverter;
import com.maks.assetaccounting.dto.user.UserToBackDto;
import com.maks.assetaccounting.dto.user.UserToFrontDto;
import com.maks.assetaccounting.entity.User;
import com.maks.assetaccounting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.maks.assetaccounting.util.ValidationUtil.assureIdConsistent;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserToBackConverter userToBackConverter;

    private final UserToFrontConverter userToFrontConverter;

    private final UserToSaveConverter userToSaveConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserToBackConverter userToBackConverter, UserToFrontConverter userToFrontConverter, UserToSaveConverter userToSaveConverter) {
        this.userRepository = userRepository;
        this.userToBackConverter = userToBackConverter;
        this.userToFrontConverter = userToFrontConverter;
        this.userToSaveConverter = userToSaveConverter;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getByName(username);
    }

    @Override
    public UserToFrontDto create(UserToBackDto userToBackDto) {
        User user = userRepository.save(userToSaveConverter.convertToEntity(userToBackDto));
        return userToFrontConverter.convertToDto(user);
    }

    @Override
    public UserToFrontDto get(Long id) {
        return userToFrontConverter.convertToDto(userRepository.findById(id).orElse(null));
    }

    @Override
    public UserToFrontDto update(UserToBackDto userToBackDto, Long id) {
        assureIdConsistent(userToBackDto, id);
        User user = userRepository.save(userToBackConverter.convertToEntity(userToBackDto));
        return userToFrontConverter.convertToDto(user);
    }

    @Override
    public UserToFrontDto delete(Long id) {
        UserToFrontDto userToFrontDto = get(id);
        userRepository.deleteById(id);
        return userToFrontDto;
    }

    @Override
    public List<UserToFrontDto> getAll() {
        return userToFrontConverter.convertListToDto(userRepository.findAll());
    }

    @Override
    public User getByName(String username) {
        return userRepository.findByUsername(username);
    }
}
