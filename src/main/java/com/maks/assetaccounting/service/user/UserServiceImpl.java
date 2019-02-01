package com.maks.assetaccounting.service.user;

import com.maks.assetaccounting.converter.UserConverter;
import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.entity.User;
import com.maks.assetaccounting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public UserDto create(final UserDto userDto, final String username) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        final User user = userRepository.save(userConverter.convertToCreateEntity(userDto));
        return userConverter.convertToDto(user);
    }

    @Override
    public UserDto get(final Long id, final Long authUserId) {
        return userConverter.convertToDto(userRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public UserDto update(final UserDto userDto, final Long id, final String username) {
        assureIdConsistent(userDto, id);
        final User user = userRepository.save(userConverter.convertToEntity(userDto));
        return userConverter.convertToDto(user);
    }

    @Override
    @Transactional
    @Secured("ROLE_ADMIN")
    public UserDto delete(final Long id, final Long authUserId) {
        final UserDto userToFrontDto = get(id, authUserId);
        userRepository.deleteById(id);
        return userToFrontDto;
    }

    @Override
    @Secured("ROLE_ADMIN")
    public List<UserDto> getAll(final Long authUserId) {
        return userConverter.convertListToDto(userRepository.findAll());
    }

    @Override
    @Transactional
    @Secured("ROLE_ADMIN")
    public void deleteAll(final List<UserDto> userDtoList, final Long authUserId) {
        userRepository.deleteAll(userConverter.convertListToEntity(userDtoList));
    }

    @Override
    public UserDto getByName(final String username) {
        return userConverter.convertToDto(userRepository.findByUsername(username));
    }

    @Override
    @Transactional
    public UserDto changeUserPassword(final UserDto userDto, final Long id, final String password) {
        assureIdConsistent(userDto, id);
        userDto.setPassword(passwordEncoder.encode(password));
        final User user = userRepository.save(userConverter.convertToCreateEntity(userDto));
        return userConverter.convertToDto(user);
    }

    @Override
    public Page<UserDto> findAnyMatching(final Optional<String> filter, final Pageable pageable, final Long authUserId) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return userConverter.convertPageToDto(userRepository.findByUsernameLikeIgnoreCase(repositoryFilter, pageable));
        } else {
            return userConverter.convertPageToDto(userRepository.findBy(pageable));
        }
    }

    @Override
    public long countAnyMatching(final Optional<String> filter, final Long authUserId) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return userRepository.countByUsernameLikeIgnoreCase(repositoryFilter);
        } else {
            return userRepository.count();
        }
    }
}
