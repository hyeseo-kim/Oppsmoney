package com.example.subscribe.controller;

import com.example.subscribe.entity.Subscription;
import com.example.subscribe.entity.User;
import com.example.subscribe.repository.SubscriptionRepository;
import com.example.subscribe.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionWebController {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    /** 📄 구독 등록 폼 페이지 */
    @GetMapping("/register")
    public String showSubscriptionForm() {
        return "subscription-register";
    }

    /** ✅ 구독 등록 처리 */
    @PostMapping("/register")
    public String registerSubscription(@RequestParam String service_name,
                                       @RequestParam String category,
                                       @RequestParam int amount,
                                       @RequestParam LocalDate payment_day,
                                       @RequestParam(required = false) Integer alarm_days_before,
                                       @RequestParam(required = false, defaultValue = "false") boolean alarmEnabled,
                                       @RequestParam(required = false) Boolean next_month_use,
                                       @RequestParam(required = false) String memo,
                                       HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        User user = userRepository.findById(userId).orElseThrow();

        Subscription subscription = Subscription.builder()
                .user(user)
                .service_name(service_name)
                .category(category)
                .amount(amount)
                .paymentDay(payment_day)
                .alarm_days_before(alarm_days_before != null ? alarm_days_before : 0)
                .alarmEnabled(alarmEnabled)
                .next_month_use(next_month_use != null && next_month_use)
                .memo(memo)
                .created_at(LocalDateTime.now())
                .build();

        subscriptionRepository.save(subscription);
        return "redirect:/dashboard";
    }

    /** ❌ 단일 구독 삭제 */
    @PostMapping("/delete/{id}")
    public String deleteSubscription(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        Subscription sub = subscriptionRepository.findById(id)
                .filter(s -> s.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("해당 구독이 없거나 권한이 없습니다."));

        subscriptionRepository.delete(sub);
        return "redirect:/dashboard";
    }

    /** ❌ 여러 구독 일괄 삭제 */
    @PostMapping("/delete-many")
    public String deleteMany(@RequestParam("ids") List<Long> ids, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        List<Subscription> deletable = subscriptionRepository.findAllById(ids).stream()
                .filter(sub -> sub.getUser().getId().equals(userId))
                .toList();

        subscriptionRepository.deleteAll(deletable);
        return "redirect:/dashboard";
    }

    /** 🛠️ 구독 관리 페이지 (여러 구독 수정용) */
    @GetMapping("/manage")
    public String showEditPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        List<Subscription> subscriptions = subscriptionRepository.findByUser_UserId(userId);
        model.addAttribute("subscriptions", subscriptions);
        return "subscription-edit";
    }

    /** 🛠️ 여러 구독 수정 처리 */
    @PostMapping("/manage")
    public String updateSubscriptions(@RequestParam("subscriptionIds") List<Long> ids,
                                      @RequestParam("payment_days") List<LocalDate> paymentDays,
                                      @RequestParam("alarm_days_befores") List<Integer> alarmDays,
                                      @RequestParam("amounts") List<Integer> amounts,
                                      @RequestParam("next_month_uses") List<Boolean> nextMonthUses,
                                      @RequestParam("alarmEnableds") List<Boolean> alarmEnableds,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/users/login";
        }

        for (int i = 0; i < ids.size(); i++) {
            Optional<Subscription> opt = subscriptionRepository.findById(ids.get(i));
            if (opt.isPresent() && opt.get().getUser().getId().equals(userId)) {
                Subscription sub = opt.get();
                sub.setPaymentDay(paymentDays.get(i));
                sub.setAlarm_days_before(alarmDays.get(i));
                sub.setAmount(amounts.get(i));
                sub.setNext_month_use(nextMonthUses.get(i));
                sub.setAlarmEnabled(alarmEnableds.get(i));
                subscriptionRepository.save(sub);
            }
        }

        redirectAttributes.addFlashAttribute("message", "구독 정보가 성공적으로 수정되었습니다.");
        return "redirect:/subscriptions/manage";
    }

    /** ✏️ 단일 구독 수정 */
    @PostMapping("/edit/{id}")
    public String updateSubscription(@PathVariable Long id,
                                     @RequestParam String serviceName,
                                     @RequestParam String category,
                                     @RequestParam int amount,
                                     @RequestParam String paymentDay,
                                     @RequestParam(defaultValue = "3") int alarmDays,
                                     @RequestParam(defaultValue = "true") boolean nextMonthUse,
                                     @RequestParam(required = false) String memo,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("userId");

        Subscription subscription = subscriptionRepository.findById(id)
                .filter(s -> s.getUser().getId().equals(userId))
                .orElse(null);

        if (subscription == null) {
            redirectAttributes.addFlashAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/subscriptions";
        }

        subscription.setService_name(serviceName);
        subscription.setCategory(category);
        subscription.setAmount(amount);
        subscription.setPaymentDay(LocalDate.parse(paymentDay));
        subscription.setAlarm_days_before(alarmDays);
        subscription.setNext_month_use(nextMonthUse);
        subscription.setMemo(memo);

        subscriptionRepository.save(subscription);

        redirectAttributes.addFlashAttribute("message", "구독 정보가 수정되었습니다.");
        return "redirect:/subscriptions";
    }

}
