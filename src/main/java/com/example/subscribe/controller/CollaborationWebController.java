package com.example.subscribe.controller;

import com.example.subscribe.entity.CollaborationProposal;
import com.example.subscribe.repository.CollaborationProposalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

/**
 * ✅ 협업 제안 폼 컨트롤러
 * 사용자가 협업 제안을 제출하면 DB에 저장하고 관리자에게 메일로 전달
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class CollaborationWebController {

    private final JavaMailSender mailSender;
    private final CollaborationProposalRepository proposalRepository;

    /** ✅ 협업 제안 폼 페이지 진입 */
    @GetMapping("/collaboration")
    public String showCollaborationForm() {
        return "collaboration"; // templates/collaboration.html
    }

    /** ✅ 협업 제안 제출 처리 (DB 저장 + 이메일 전송) */
    @PostMapping("/collaboration")
    public String sendCollaborationMail(@RequestParam String name,
                                        @RequestParam String email,
                                        @RequestParam String title,
                                        @RequestParam String content,
                                        RedirectAttributes redirectAttributes) {
        try {
            // 📌 DB 저장
            proposalRepository.save(CollaborationProposal.builder()
                    .name(name)
                    .email(email)
                    .title(title)
                    .content(content)
                    .createdAt(LocalDateTime.now())
                    .build());

            // 📬 관리자 메일 전송
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("info@oops.com"); // 관리자 이메일 주소
            message.setSubject("[협업 제안] " + title);
            message.setText("제안자: " + name + "\n이메일: " + email + "\n\n" + content);
            mailSender.send(message);

            redirectAttributes.addFlashAttribute("message", "성공적으로 전송되었습니다!");
        } catch (Exception e) {
            log.error("❌ 협업 제안 메일 전송 실패 - name: {}, email: {}, error: {}", name, email, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "메일 전송 중 오류가 발생했습니다.");
        }

        return "redirect:/collaboration";
    }
}
