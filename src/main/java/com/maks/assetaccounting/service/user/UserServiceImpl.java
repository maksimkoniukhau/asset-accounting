package com.maks.assetaccounting.service.user;

import com.maks.assetaccounting.converter.UserConverter;
import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.entity.User;
import com.maks.assetaccounting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.maks.assetaccounting.util.ValidationUtil.assureIdConsistent;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserConverter userConverter;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final UserConverter userConverter, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDto create(final UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        final User user = userRepository.save(userConverter.convertToCreateEntity(userDto));
        return userConverter.convertToDto(user);
    }

    @Override
    public UserDto get(final Long id) {
        return userConverter.convertToDto(userRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public UserDto update(final UserDto userDto, final Long id) {
        assureIdConsistent(userDto, id);
        final User user = userRepository.save(userConverter.convertToEntity(userDto));
        return userConverter.convertToDto(user);
    }

    @Override
    @Transactional
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public UserDto delete(final Long id) {
        final UserDto userToFrontDto = get(id);
        userRepository.deleteById(id);
        return userToFrontDto;
    }

    @Override
    @Secured("ROLE_ADMIN")
    public List<UserDto> getAll() {
        return userConverter.convertListToDto(userRepository.findAll());
    }

    @Override
    public UserDto getByName(final String username) {
        return userConverter.convertToDto(userRepository.findByUsername(username));
    }

    @Override
    @Transactional
    public void deleteAll(final List<UserDto> userDtoList) {
        userRepository.deleteAll(userConverter.convertListToEntity(userDtoList));
    }

    @Override
    @Transactional
    public UserDto changeUserPassword(final UserDto userDto, final Long id, final String password) {
        assureIdConsistent(userDto, id);
        userDto.setPassword(passwordEncoder.encode(password));
        final User user = userRepository.save(userConverter.convertToCreateEntity(userDto));
        return userConverter.convertToDto(user);
    }
}
