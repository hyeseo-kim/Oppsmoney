package com.example.subscribe.controller;

import com.example.subscribe.entity.SupportInquiry;
import com.example.subscribe.entity.User;
import com.example.subscribe.repository.SupportInquiryRepository;
import com.example.subscribe.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * ✅ 고객지원 (1:1 문의) 컨트롤러
 * - 문의 목록 조회
 * - 문의 등록
 * - 문의 삭제
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/users/support")
public class SupportWebController {

    private final SupportInquiryRepository supportRepository;
    private final UserRepository userRepository;

    /** ✅ 고객지원 페이지 진입 (문의 목록 출력) */
    @GetMapping
    public String showSupportPage(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            model.addAttribute("inquiries", supportRepository.findByUserId(userId));
        }

        return "support";
    }

    /** ✅ 문의 등록 */
    @PostMapping("/submit")
    public String submitQuestion(@RequestParam String title,
                                 @RequestParam String question,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return "redirect:/users/login";

        SupportInquiry inquiry = SupportInquiry.builder()
                .title(title)
                .question(question)
                .user(userOpt.get())
                .createdAt(LocalDateTime.now())
                .build();

        supportRepository.save(inquiry);
        redirectAttributes.addFlashAttribute("message", "문의가 등록되었습니다.");
        return "redirect:/users/support";
    }

    /** ✅ 문의 삭제 */
    @PostMapping("/delete/{id}")
    public String deleteInquiry(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        Optional<SupportInquiry> inquiryOpt = supportRepository.findById(id);
        if (inquiryOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "존재하지 않는 문의입니다.");
            return "redirect:/users/support";
        }

        SupportInquiry inquiry = inquiryOpt.get();

        // 🔐 본인 확인
        if (!inquiry.getUser().getId().equals(userId)) {
            redirectAttributes.addFlashAttribute("error", "본인의 문의만 삭제할 수 있습니다.");
            return "redirect:/users/support";
        }

        supportRepository.delete(inquiry);
        redirectAttributes.addFlashAttribute("message", "문의가 삭제되었습니다.");
        return "redirect:/users/support";
    }
}
