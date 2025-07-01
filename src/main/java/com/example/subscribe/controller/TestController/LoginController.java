//package com.example.subscribe.controller;
//
//import com.example.subscribe.entity.User;
//import com.example.subscribe.repository.UserRepository;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@RestController
//@RequiredArgsConstructor
//public class LoginController {
//
//    private final UserRepository userRepository;
//
//    @PostMapping("/login")
//    public String login(@RequestParam String email,
//                        @RequestParam String password,
//                        HttpSession session) {
//
//        Optional<User> userOpt = userRepository.findByEmail(email);
//
//        if (userOpt.isPresent()) {
//            User user = userOpt.get(); // 🔹 변수로 꺼냄
//            if (user.getPassword().equals(password)) {
//                session.setAttribute("userId", user.getId());
//                session.setAttribute("userNickname", user.getNickname());
//                return "✅ 로그인 성공! 닉네임: " + user.getNickname();
//            }
//        }
//
//        return "❌ 로그인 실패: 이메일 또는 비밀번호 오류";
//    }
//
//    @PostMapping("/users/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "✅ 로그아웃 완료 (세션 삭제됨)";
//    }
//}
