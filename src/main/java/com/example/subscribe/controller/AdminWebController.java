package com.example.subscribe.controller;

import com.example.subscribe.entity.CollaborationProposal;
import com.example.subscribe.entity.SupportInquiry;
import com.example.subscribe.repository.CollaborationProposalRepository;
import com.example.subscribe.repository.SupportInquiryRepository;
import com.example.subscribe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ✅ 관리자 전용 웹 컨트롤러
 * - 가입자 통계 및 미답변 문의/협업 제안 관리
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminWebController {

    private final UserRepository userRepository;
    private final SupportInquiryRepository supportInquiryRepository;
    private final CollaborationProposalRepository collaborationProposalRepository;

    /** ✅ 관리자 대시보드: 오늘 가입자 수, 미답변 문의 수 표시 */
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        long todayJoinCount = userRepository.countByCreatedAtBetween(
                LocalDate.now().atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay()
        );
        long unansweredCount = supportInquiryRepository.countByAnswerIsNull();

        model.addAttribute("todayJoinCount", todayJoinCount);
        model.addAttribute("unansweredCount", unansweredCount);
        return "admin-dashboard";
    }

    /** ✅ 미답변 문의 목록 페이지 */
    @GetMapping("/questions")
    public String unansweredQuestions(Model model) {
        List<SupportInquiry> unansweredList = supportInquiryRepository.findByAnswerIsNull();
        model.addAttribute("unansweredList", unansweredList);
        return "admin-unanswered";
    }

    /** ✅ 문의 답변 등록 */
    @PostMapping("/questions/{id}/answer")
    public String answerQuestion(@PathVariable Long id, @RequestParam String answer) {
        if (answer == null || answer.trim().isEmpty()) {
            throw new IllegalArgumentException("답변 내용은 비어 있을 수 없습니다.");
        }

        supportInquiryRepository.findById(id).ifPresent(inquiry -> {
            inquiry.setAnswer(answer);
            inquiry.setAnsweredAt(LocalDateTime.now());
            supportInquiryRepository.save(inquiry);
            log.info("[문의 응답 완료] ID: {}", id);
        });

        return "redirect:/admin/questions";
    }

    /** ✅ 협업 제안 목록 조회 */
    @GetMapping("/proposals")
    public String showProposals(Model model) {
        List<CollaborationProposal> proposals = collaborationProposalRepository.findAll();
        model.addAttribute("proposals", proposals);
        return "admin-proposal-list";
    }

    /** ✅ 협업 제안에 응답 등록 */
    @PostMapping("/proposals/{id}/reply")
    public String replyToProposal(@PathVariable Long id, @RequestParam String answer) {
        CollaborationProposal proposal = collaborationProposalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제안이 존재하지 않습니다. ID=" + id));

        proposal.setAnswer(answer);
        proposal.setAnswered(true);
        collaborationProposalRepository.save(proposal);

        log.info("[협업 제안 응답 완료] ID: {}", id);
        return "redirect:/admin/proposals";
    }

    /** ✅ 협업 제안 삭제 */
    @PostMapping("/proposals/{id}/delete")
    public String deleteProposal(@PathVariable Long id) {
        collaborationProposalRepository.deleteById(id);
        log.info("[협업 제안 삭제 완료] ID: {}", id);
        return "redirect:/admin/proposals";
    }
}
