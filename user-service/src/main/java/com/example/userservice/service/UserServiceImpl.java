package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.netflix.discovery.converters.Auto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {

        // 아이디는 랜덤으로 지정
        userDto.setUserId(UUID.randomUUID().toString());

        // ModelMapper 설정 변경
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 변환
        User user = modelMapper.map(userDto, User.class);
        user.setEncryptedPwd("encrypted_password");
//        user.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

        userRepository.save(user);

        return null;
    }
}
