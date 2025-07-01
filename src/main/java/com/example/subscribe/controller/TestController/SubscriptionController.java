//package com.example.subscribe.controller;
//
//import com.example.subscribe.entity.Subscription;
//import com.example.subscribe.entity.User;
//import com.example.subscribe.repository.SubscriptionRepository;
//import com.example.subscribe.repository.UserRepository;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/subscriptions")
//public class SubscriptionController {
//
//    private final SubscriptionRepository subscriptionRepository;
//    private final UserRepository userRepository;
//
//    // ✅ 구독 등록
//    @PostMapping("/add")
//    public String addSubscription(@RequestParam String serviceName,
//                                  @RequestParam String category,
//                                  @RequestParam int amount,
//                                  @RequestParam String paymentDay,
//                                  @RequestParam(defaultValue = "3") int alarmDays,
//                                  @RequestParam(defaultValue = "true") boolean nextMonthUse,
//                                  @RequestParam(required = false) String memo,
//                                  HttpSession session) {
//
//        Long userId = (Long) session.getAttribute("userId");
//        if (userId == null) return "로그인이 필요합니다.";
//
//        Optional<User> userOpt = userRepository.findById(userId);
//        if (userOpt.isEmpty()) return "존재하지 않는 사용자입니다.";
//
//        Subscription sub = Subscription.builder()
//                .user(userOpt.get())
//                .service_name(serviceName)        // 스네이크 케이스 필드명
//                .category(category)
//                .amount(amount)
//                .payment_day(LocalDate.parse(paymentDay))
//                .alarm_days_before(alarmDays)
//                .next_month_use(nextMonthUse)
//                .memo(memo)
//                .created_at(LocalDateTime.now())
//                .build();
//
//        subscriptionRepository.save(sub);
//        return "구독 등록 완료!";
//    }
//
//    // ✅ 구독 전체 조회
//    @GetMapping("/all")
//    public List<Subscription> getAll(HttpSession session) {
//        Long userId = (Long) session.getAttribute("userId");
//        if (userId == null) return List.of();
//
//        return subscriptionRepository.findByUser_UserId(userId);
//
//    }
//
//    // ✅ 구독 수정
//    @PutMapping("/{id}")
//    public String updateSubscription(@PathVariable Long id,
//                                     @RequestParam(required = false) String serviceName,
//                                     @RequestParam(required = false) String category,
//                                     @RequestParam(required = false) Integer amount,
//                                     @RequestParam(required = false) String paymentDay,
//                                     @RequestParam(required = false) Integer alarmDays,
//                                     @RequestParam(required = false) Boolean nextMonthUse,
//                                     @RequestParam(required = false) String memo) {
//        Optional<Subscription> subOpt = subscriptionRepository.findById(id);
//        if (subOpt.isEmpty()) return "수정할 구독을 찾을 수 없습니다.";
//
//        Subscription sub = subOpt.get();
//
//        if (serviceName != null) sub.setService_name(serviceName);
//        if (category != null) sub.setCategory(category);
//        if (amount != null) sub.setAmount(amount);
//        if (paymentDay != null) sub.setPayment_day(LocalDate.parse(paymentDay));
//        if (alarmDays != null) sub.setAlarm_days_before(alarmDays);
//        if (nextMonthUse != null) sub.setNext_month_use(nextMonthUse);
//        if (memo != null) sub.setMemo(memo);
//
//        subscriptionRepository.save(sub);
//        return "구독 수정 완료!";
//    }
//
//    // ✅ 구독 삭제
//    @DeleteMapping("/{id}")
//    public String deleteSubscription(@PathVariable Long id) {
//        if (!subscriptionRepository.existsById(id)) {
//            return "삭제할 구독을 찾을 수 없습니다.";
//        }
//        subscriptionRepository.deleteById(id);
//        return "구독 삭제 완료!";
//    }
//
//    // ✅ 이번 달 총합 구독비 조회
//    @GetMapping("/stats/total")
//    public String getMonthlyTotal(HttpSession session) {
//        Long userId = (Long) session.getAttribute("userId");
//        if (userId == null) return "로그인이 필요합니다.";
//
//        LocalDate now = LocalDate.now();
//        Integer total = subscriptionRepository.getTotalAmountByMonth(userId, now.getMonthValue(), now.getYear());
//
//        return "이번 달 총 구독비: " + (total != null ? total : 0) + "원";
//    }
//
//
//    // ✅ 카테고리별 지출 통계
//    @GetMapping("/stats/category")
//    public List<String> getCategoryStats(HttpSession session) {
//        Long userId = (Long) session.getAttribute("userId");
//        if (userId == null) return List.of("로그인이 필요합니다.");
//
//        LocalDate now = LocalDate.now();
//        List<Object[]> stats = subscriptionRepository.getAmountGroupedByCategory(userId, now.getMonthValue(), now.getYear());
//
//        return stats.stream()
//                .map(obj -> "카테고리: " + obj[0] + ", 총액: " + obj[1] + "원")
//                .toList();
//    }
//
//}
