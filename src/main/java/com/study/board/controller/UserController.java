package com.study.board.controller;

import com.study.board.dto.request.ReqLoginDto;
import com.study.board.dto.request.ReqUserDto;
import com.study.board.dto.response.ResUserDto;
import com.study.board.entity.User;
import com.study.board.repository.UserRepository;
import com.study.board.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody ReqUserDto reqUserDto) {
        try {
            // 이메일 중복 체크
            if (userRepository.existsByEmail(reqUserDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용 중인 이메일입니다.");
            }

            // User 객체 생성
            User user = User.builder()
                    .email(reqUserDto.getEmail())
                    .password(passwordEncoder.encode(reqUserDto.getPassword())) // ✅ 비밀번호 암호화
                    .username(reqUserDto.getUsername())
                    .height(reqUserDto.getHeight())
                    .weight(reqUserDto.getWeight())
                    .benchMax(reqUserDto.getBenchMax())
                    .deadliftMax(reqUserDto.getDeadliftMax())
                    .squatMax(reqUserDto.getSquatMax())
                    .build();

            // 점수 및 계급 계산
            user.calculateScoreAndRank();

            // DB 저장
            userRepository.save(user);

            // 응답 DTO 생성
            ResUserDto responseDTO = ResUserDto.builder()
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .height(user.getHeight())
                    .weight(user.getWeight())
                    .score((int) user.getScore())
                    .rank(user.getRank())
                    .build();

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<?> checkEmailDuplicate(@RequestParam(name = "email") String email) {
        boolean exists = userRepository.existsByEmail(email);
        return ResponseEntity.ok(exists);  // ✅ 200 OK + true/false 반환
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody ReqLoginDto reqLoginDto) {
        Optional<User> optionalUser = userRepository.findByEmail(reqLoginDto.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("존재하지 않는 이메일입니다.");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(reqLoginDto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        }

        // ✅ JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", user.getEmail(),
                "username", user.getUsername(),
                "rank", user.getRank()
        ));
    }


}
