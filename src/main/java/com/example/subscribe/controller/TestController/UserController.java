//package com.example.subscribe.controller;
//
//import com.example.subscribe.entity.User;
//import com.example.subscribe.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/users")
//public class UserController {
//
//    private final UserRepository userRepository;
//
//    // 🔸 테스트용 사용자 저장
//    @PostMapping("/add")
//    public String addUser(@RequestParam String email, @RequestParam String password){
//        User user = User.builder()
//                .email(email)
//                .password(password)
//                .created_at(LocalDateTime.now())
//                .build();
//        userRepository.save(user);
//        return "사용자 저장 완료";
//    }
//
//    // 🔸 테스트용 전체 조회
//    @GetMapping("/all")
//    public List<User> getAllUsers(){
//        return userRepository.findAll();
//    }
//
//    // ✅ 닉네임 중복 확인 API
//    @GetMapping("/check-nickname")
//    public Map<String, Boolean> checkNickname(@RequestParam String nickname) {
//        boolean isAvailable = userRepository.findByNickname(nickname).isEmpty();
//        return Collections.singletonMap("available", isAvailable);
//    }
//}
