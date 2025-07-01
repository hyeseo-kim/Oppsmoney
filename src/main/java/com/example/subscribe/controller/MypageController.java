package com.example.subscribe.controller;

import com.example.subscribe.entity.User;
import com.example.subscribe.repository.SubscriptionRepository;
import com.example.subscribe.repository.UserRepository;
import com.example.subscribe.service.SubscriptionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MypageController {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;

    /** ✅ 마이페이지 메인 */
    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return "redirect:/users/login";

        int totalAmount = subscriptionService.getMonthlyTotal(userId);
        int subscriptionCount = subscriptionRepository.findByUser_UserId(userId).size();
        Map<String, Integer> categoryTotals = subscriptionService.getMonthlyCategoryTotals(userId);

        model.addAttribute("user", user);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("subscriptionCount", subscriptionCount);
        model.addAttribute("categoryTotals", categoryTotals);
        model.addAttribute("subscriptions", subscriptionRepository.findByUser_UserId(userId));

        return "mypage";
    }

    /** ✅ 닉네임/비밀번호 수정 폼 */
    @GetMapping("/mypage/edit")
    public String editForm() {
        return "mypage-edit";
    }

    /** ✅ 닉네임 변경 */
    @PostMapping("/mypage/update-nickname")
    public String updateNickname(@RequestParam String nickname,
                                 HttpSession session,
                                 Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return "redirect:/users/login";

        // 현재 닉네임과 다르고, 이미 사용 중인 닉네임이면 에러
        if (!user.getNickname().equals(nickname) &&
                userRepository.existsByNickname(nickname)) {
            model.addAttribute("error", "이미 사용 중인 닉네임입니다.");
            return "mypage-edit";
        }

        user.setNickname(nickname);
        userRepository.save(user);
        session.setAttribute("userNickname", nickname); // 세션에 반영

        model.addAttribute("message", "닉네임이 변경되었습니다.");
        return "mypage-edit";
    }

    /** ✅ 비밀번호 변경 */
    @PostMapping("/mypage/update-password")
    public String updatePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return "redirect:/users/login";

        if (!user.getPassword().equals(currentPassword)) {
            model.addAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
            return "mypage-edit";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "새 비밀번호가 일치하지 않습니다.");
            return "mypage-edit";
        }

        // 비밀번호 규칙: 8자 이상 + 대문자 + 숫자 + 특수문자
        String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*\\d).{8,}$";
        if (!newPassword.matches(regex)) {
            model.addAttribute("error", "비밀번호는 8자 이상, 대문자/숫자/특수문자를 포함해야 합니다.");
            return "mypage-edit";
        }

        user.setPassword(newPassword);
        userRepository.save(user);
        model.addAttribute("message", "비밀번호가 변경되었습니다.");
        return "mypage-edit";
    }
}
