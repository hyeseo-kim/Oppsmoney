package com.example.subscribe.controller;

import com.example.subscribe.entity.User;
import com.example.subscribe.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;

/**
 * ✅ 회원가입 / 로그인 / 로그아웃 / 정보 확인 / 계정 삭제 등의 웹 컨트롤러
 */
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserWebController {

    private final UserRepository userRepository;

    /** ✅ 회원가입 화면 진입 */
    @GetMapping("/register")
    public String showRegisterPage() {
        return "user-register";
    }

    /** ✅ 회원가입 처리 */
    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String nickname,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        // 비밀번호 일치 확인
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "user-register";
        }

        // 이메일 중복 확인
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "이미 가입된 이메일입니다.");
            return "user-register";
        }

        // 닉네임 중복 확인
        if (userRepository.findByNickname(nickname).isPresent()) {
            model.addAttribute("error", "이미 사용 중인 닉네임입니다.");
            return "user-register";
        }

        // 저장
        User user = User.builder()
                .name(name)
                .email(email)
                .nickname(nickname)
                .password(password)
                .created_at(LocalDateTime.now())
                .build();

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("registerSuccess", true);

        return "redirect:/users/login";
    }

    /** ✅ 로그인 화면 */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    /** ✅ 로그인 처리 */
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPassword().equals(password)) {
                session.setAttribute("userId", user.getId());
                session.setAttribute("userNickname", user.getNickname());
                session.setAttribute("userEmail", user.getEmail());

                // 👉 관리자 이메일인 경우 관리자 페이지로 이동
                if ("oppsmoney.manager@gmail.com".equals(user.getEmail())) {
                    return "redirect:/admin/dashboard";
                }

                // 👉 일반 사용자
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "비밀번호가 틀렸습니다.");
            }
        } else {
            model.addAttribute("error", "해당 이메일로 가입된 계정이 없습니다.");
        }

        return "login";
    }

    /** ✅ 로그아웃 처리 */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    /** ✅ 닉네임 중복확인 (AJAX 용) */
    @GetMapping("/check-nickname")
    @ResponseBody
    public Map<String, Boolean> checkNickname(@RequestParam String nickname) {
        boolean exists = userRepository.findByNickname(nickname).isPresent();
        return Map.of("available", !exists);
    }

    /** ✅ 이메일 중복확인 (AJAX 용) */
    @GetMapping("/check-email")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userRepository.findByEmail(email).isPresent();
        return Map.of("available", !exists);
    }

    /** ✅ 계정 삭제 처리 */
    @PostMapping("/delete")
    public String deleteUser(@RequestParam String password,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/users/login";
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
            return "redirect:/mypage";
        }

        User user = userOpt.get();

        if (user.getPassword().equals(password)) {
            userRepository.delete(user);
            session.invalidate();
            redirectAttributes.addFlashAttribute("successMessage", "계정이 삭제되었습니다.");
            return "redirect:/users/login";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 틀렸습니다.");
            return "redirect:/mypage";
        }
    }

    /** (선택) 협업 제안 페이지 */
    @GetMapping("/collaboration")
    public String collaborationPage() {
        return "collaboration";
    }

    /** (선택) 설정 페이지 */
    @GetMapping("/settings")
    public String settingsPage() {
        return "settings";
    }
}
