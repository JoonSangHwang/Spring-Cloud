package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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
        
        UserDto retUserDto = modelMapper.map(user, UserDto.class);

        return retUserDto;
    }
}
