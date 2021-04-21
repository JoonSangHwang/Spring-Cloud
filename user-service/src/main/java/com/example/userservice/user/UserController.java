package com.example.userservice.user;

import com.example.userservice.user.dto.RequestUser;
import com.example.userservice.user.dto.ResponseUser;
import com.example.userservice.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("user-service")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final Environment environment;

    @Value("${greeting.message}")
    private String greetingMessage;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service"
                + ", port(local.server.port)=" + environment.getProperty("local.server.port")
                + ", port(server.port)=" + environment.getProperty("server.port")
                + ", token secret=" + environment.getProperty("token.secret")
                + ", token expiration time=" + environment.getProperty("token.expiration_time"));
    }

    @GetMapping("/welcome")
    public String welcome() {
        return greetingMessage;
    }


    /**
     * 유저 생성
     */
    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {

        // ModelMapper 설정 변경
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // [S] 저장 Service 호출
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userService.createUser(userDto);
        // [E] 저장 Service 호출

        // 결과값 반환
        ResponseUser responseUser = modelMapper.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }


    /**
     * 유저 전체 조회
     */
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        Iterable<User> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(modelMapper.map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    /**
     * 유저 조회
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserByUserId(userId);

        ResponseUser returnValue = modelMapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
