package com.maks.assetaccounting.service.user;

import com.maks.assetaccounting.converter.UserConverter;
import com.maks.assetaccounting.dto.UserDto;
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

    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return getByName(username);
    }

    @Override
    public UserDto create(final UserDto userDto) {
        final User user = userRepository.save(userConverter.convertToSaveEntity(userDto));
        return userConverter.convertToDto(user);
    }

    @Override
    public UserDto get(final Long id) {
        return userConverter.convertToDto(userRepository.findById(id).orElse(null));
    }

    @Override
    public UserDto update(final UserDto userDto, final Long id) {
        assureIdConsistent(userDto, id);
        final User user = userRepository.save(userConverter.convertToEntity(userDto));
        return userConverter.convertToDto(user);
    }

    @Override
    public UserDto delete(final Long id) {
        final UserDto userToFrontDto = get(id);
        userRepository.deleteById(id);
        return userToFrontDto;
    }

    @Override
    public List<UserDto> getAll() {
        return userConverter.convertListToDto(userRepository.findAll());
    }

    @Override
    public User getByName(final String username) {
        return userRepository.findByUsername(username);
    }
}
