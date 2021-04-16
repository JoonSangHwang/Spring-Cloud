package com.example.userservice.user;

import com.example.userservice.user.dto.ResponseOrder;
import com.example.userservice.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {

        // 아이디는 랜덤으로 지정
        userDto.setUserId(UUID.randomUUID().toString());

        // ModelMapper 설정 변경
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 저장을 위한 변환
        User user = modelMapper.map(userDto, User.class);
        user.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

        // 유저 생성 !!!
        userRepository.save(user);

        // 반환을 위한 변환
        UserDto retUserDto = modelMapper.map(user, UserDto.class);
        return retUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        User user = userRepository.findByUserId(userId);
        UserDto userDto = modelMapper.map(user, UserDto.class);

        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);
        return null;
    }

    @Override
    public Iterable<User> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null)
            throw new UsernameNotFoundException(email);

        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    /**
     * 로그인 요청
     * - DB 에서 유저 조회 후, 존재하는 유저일 경우 반환
     * - User 객체를 사용하여, UserDetails 객체를 반환 하도록 함
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if (user == null)
            throw new UsernameNotFoundException(username);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getEncryptedPwd(),true,true,true,true,new ArrayList<>());
    }
}
