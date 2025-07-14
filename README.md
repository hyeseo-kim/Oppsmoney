# 🫟Oppsmoney

Spring Boot 기반의 **유료 구독 알림 시스템** 입니다.

사용자가 등록한 구독 서비스에 대해 매월 결제일 전 **자동 알림**, **알림 읽음 처리**, **삭제**, **미확인 알림 카운트 표시**, **통계 비교** 등을 제공하며, **고객 지원 Q&A**, **협업 제안** 기능도 포함된 실용적인 포트폴리오 프로젝트입니다.

---

## 프로젝트 개요

|         영역        |                                                    |
|--------------------|----------------------------------------------------|
|      프로젝트 명      |  Oppsmoney                                         |
|       개발 기간      |  2025.05 ~ 2025.06 (약 5주)                         |
|       개발 형태      |  개인 개발 (기획, 백엔드, 프론트엔드)                      |
|       주요 기능      |  구독 등록 / 알림 / 통계 / Q&A / 관리자 응답 / 협업 제안     |
|       GitHub       |  hyeseo-kim/Oppsmoney                              |

---

## 기술 스택

|         영역        |                                                    |
|--------------------|----------------------------------------------------|
|      Language      |  Java 17                                           |
|      Framework     |  Spring Boot 3.x, Spring MVC, Spring Data JPA      |
|       View         |  Thymeleaf, Bootstrap 5                            |
|       DB           |  MySQL 8.x                                         |
|       ORM          |  Hibernate (JPA 기반)                               |
|      Build Tool    |  Gradle                                            |
|       기타          |  Lombok, JSTL, Spring Scheduler                    |

---

## 💡 주요 기능 및 화면 미리보기

---

### 1. 로그인 / 회원가입 

- 이메일 기반 회원가입
- 로그인/로그아웃 처리

<img src="docs/images/login_register_combined.png" width="100%"/>

---

### 2. 대시보드 & 구독 등록

- 이번달 총 구독 금액, 구독 목록, 통계 카드 표시
- 카테고리 / 서비스명 / 결제일 / 알림일 등 구독 정보 등록 가능

<img src="docs/images/dashboard_subscription_combined.png" width="100%"/>

---

### 3. 알림 기능 & 마이페이지

- 결제일 전 자동 알림 발송 (Spring Scheduler)
- 알림 목록 확인, 읽음 처리, 삭제 가능
- 회원 정보 수정 및 구독 내역 관리
- 계정 삭제 기능 포함

<img src="docs/images/notification_mypage_combined.png" width="100%"/>

---

### 4. Q&A 게시판 (사용자 → 관리자)

- 사용자 질문 작성
- 관리자는 관리자 페이지에서 응답 가능

<img src="docs/images/support_combined.png" width="100%"/>

---

### 5. 협업 제안 & 수신 메일

- 사용자가 협업 제안서를 입력하면 관리자 이메일로 자동 전송됨

<img src="docs/images/collaboration_combined.png" width="100%"/>

---

### 6. 관리자 기능 통합

- 관리자 로그인 시 가입통계, 미응답 질문 확인 및 응답
- 협업 제안 목록 확인

<img src="docs/images/admin_combined.png" width="100%"/>

---

### 전체 흐름도

아래는 사용자의 기본적인 서비스 흐름입니다:

````
[회원가입] → [로그인]
             ↓
     [구독 등록 및 저장]
             ↓
        [대시보드 진입]
             ↓
    [자동 알림 스케줄링]
             ↓
[알림 확인] → [읽음/삭제] ↘ [통계 보기]
                           ↘ [Q&A / 협업 제안 작성]

 ````
                                        
<p align="center">
  <img src="docs/images/flowchart.png" alt="유료 구독 사용자 흐름도" width="600"/>
</p>

---

## 프로젝트 폴더 구조

<details>
<summary>📁 폴더 구조 보기</summary>

````
subscribe/
├─ src/main/java/com/example/subscribe/
│ ├─ controller/ # 웹 요청 처리
│ ├─ service/ # 비즈니스 로직
│ ├─ repository/ # DB 접근
│ ├─ dto/ # 데이터 전송 객체
│ └─ entity/ # JPA 엔티티
│
├─ src/main/resources/
│ ├─ templates/
│ │ ├─ dashboard.html
│ │ ├─ login.html
│ │ ├─ user-register.html
│ │ ├─ mypage.html
│ │ ├─ mypage-edit.html
│ │ ├─ subscription-register.html
│ │ ├─ subscription-edit.html
│ │ ├─ notification-list.html
│ │ ├─ support.html
│ │ ├─ collaboration.html
│ │ ├─ admin-dashboard.html
│ │ ├─ admin-unanswered.html
│ │ ├─ admin-proposal-list.html
│ │ └─ fragments/
│ │ ├─ head.html
│ │ ├─ footer.html
│ │ └─ sidebar.html
│ └─ application.properties
│
├─ build.gradle
└─ README.md
````

</details>

## 실행 방법

### 1.DB 연결 설정

`src/main/resources/application.properties` 파일에서 MySQL 연결 정보 설정:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/subscription_db?serverTimezone=Asia/Seoul
spring.datasource.username=root
spring.datasource.password=12345678
spring.jpa.hibernate.ddl-auto=update
```

### 2. Gradle 빌드 및 서버 실행

```bash
./gradlew bootRun
```

## 3. 브라우저에서 접속
▶ http://localhost:8080

---

## 개발자 정보

- 김혜서
- GitHub : [@hyeseo-kim](https://github.com/hyeseo-kim)
- Email : hyeseo0614@gmail.com

---

## 테스트 계정

|   역할    |                ID              |         비밀번호        |
|----------|--------------------------------|-----------------------|
|  사용자    |   hyeseo0614@gmail.com         |     Kimhyeseo06!      |
|  관리자    |   oppsmoney.manager@gmail.com  |     Qwer123!@#        |

---

### 트러블 슈팅 / 개선사항
- 알림을 매일 전체 사용자에게 보내면 부하가 큼 -> 구독자 별 필터링하려 대상만 추출
- 알림 중복 전송 방지 : 날짜 기준으로 이미 전송된 알림은 제외
- 다중 알림 처리 -> JS에서 체크박스로 선택 후 `/read-many`, `/delete-many` 구현

---

## 프로젝트 회고

- Spring Scheduler로 예약 알림 기능을 구성하며 비동기/스케줄링 이해도 향상
- JS 기반 UI 체크박스 처리 및 서버 연동 경험
- 관리자 역할 분리, 이메일 발송, 협업 제안 등 실제 서비스에 가까운 흐름 구현
- 실사용을 고려한 트러블슈팅(알림 중복 방지, 사용자 필터링) 경험 축적

---

### 데모 영상
- 현재는 로컬 전용 / 추후 배포 예정
