# 작업 지시 설계

타입 정의 -> 요구사항 요청 -> 분석 -> 정리
피드백 -> 반영 -> 구현 생성 -> 검토 -> 디버그

질문을 객체로 정의
명시적으로 제약 조건안에서 코드 생성 하도록 컨택스트 강화

에이전트 메모리 사용 전략
기억할것과 버릴것 정의
맥락 유지에 필요한 내용은?
필요없는 내용은?
  사실과 거짓 , 주장과 의견 
 모든 내용을 반복하지 않고 필터링 및 임베딩 보관
 상태에 관한 정보만 기억 

---

e.g)

```java
enum class language{
java,python....
//버전 추가
}
```

```java
enum class platform{
Web,Android,Linux...
}
```

```java

abstract class AppFactory{
//요구 사항을 구조화 하여 빌드
//생성 내용으로 에이전트 작업 지시
}

```
interface Spring{
void WebApp();
void Microservice();
//....
}

public abstract class BootWeb extends Spring{

override public void WebApp()

}

public class SpringBootWeb implements BootWeb{

override public void WebApp(){
    
    String name = "";
  //....
}

---



1. 문제의 핵심 정의
   문제의 요구사항을 명확히 정의합니다. 예를 들어, 사용하려는 기술 스택, 구현해야 하는 기능, 보안 요구 사항 등을 먼저 명시합니다.

2. 기술 스택 및 환경 명시
   어떤 기술을 사용할지 명확히 합니다. 예를 들어, Spring Boot, Spring Security, JWT 인증, 특정 데이터베이스 등을 사용할지에 대한 구체적인 정보를 제공합니다.

예시:

Spring Boot

Spring Security

MySQL 데이터베이스 사용

3. 기능 정의 및 흐름
   필요한 기능을 나열하고, 각 기능이 어떻게 동작해야 하는지에 대한 구체적인 설명을 제공합니다.

예시:

사용자 인증 및 권한 관리: 기본적인 사용자 인증은 HTTP 기본 인증으로 하며, 사용자에게 특정 리소스에 대한 권한을 부여한다.

ItemController: 아이템을 생성, 조회, 수정, 삭제할 수 있는 API를 제공한다.

4. 보안 및 인증 요구 사항
   보안 요구 사항을 명확히 합니다. 예를 들어, 패스워드 암호화, 인증 방식(예: HTTP Basic, JWT 등), 권한 관리 등을 구체적으로 명시합니다.

예시:

패스워드는 BCryptPasswordEncoder를 사용해 암호화

인증 방식은 HTTP 기본 인증 또는 JWT 사용

5. 코드 스타일 및 패턴
   특정 코드 스타일이나 패턴을 선호하는 경우, 예를 들어 @Bean을 사용하거나, @Controller 대신 @RestController를 사용한다거나, 컬렉션 처리 방식에 대한 선호도를 명시합니다.

예시:

@RestController를 사용하여 RESTful API를 제공

동시성 문제를 해결하기 위해 ConcurrentHashMap과 AtomicLong 사용

6. 코드 구현의 세부 사항
   필요한 클래스, 메서드, 반환값, 예외 처리, 요청/응답의 형식 등을 구체적으로 정의합니다.

예시:

POST /items: 아이템 생성

요청 본문: 아이템 이름

응답: 생성된 아이템의 ID

GET /items/{id}: 아이템 조회

응답: 아이템의 이름 또는 "아이템을 찾을 수 없습니다."

7. 환경 설정 및 의존성 관리
   필요한 의존성 라이브러리와 관련된 설정을 제공하고, 추가해야 할 환경 설정 (예: application.properties)을 언급합니다.

예시:

pom.xml에서 spring-boot-starter-security와 spring-boot-starter-web 추가

application.properties에서 보안 설정
