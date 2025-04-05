## RAG vs Embedding
- RAG
  - Reriever
    - 컨텍스트 관련 문서 검색
  - Generator
    - 검색 결과 기반 텍스트 생성
  - Ranker
    - 생성 텍스트의 품질 평가 -> 선택

- Embedding
  > Word , Sentence , Document
  - Word : 단어를 벡터화
    - 단어 의미 이해 
  - Sentence : 문장을 벡터화 
    - 문장의 의미 이해
  - Doucument : 문서를 벡터화
    - 문서의 의미 이해





# 0404 정리
- 응답 생성
- 응답 객체 바이트스트림 할당


x 미나이오 별도 운영 한뒤 호출로 가져오기?..

### 현재 생성 프로젝트를 Minio Server로 운영

- [x] 1.minio 라이브러리 추가 
- [x] 2.프로퍼티 구성 추가   
- [x] 3.미나이오 클라이언트 가져오기
- [x] 4.객체 생성로직으로 클라이언트 업로드
                       





로직
String text = "";
byte[] byteData = text.getByteArrayStream(Standard.UTF8)
InputStream toStream = new ByteArrayInputStream(byteData)

minio.put.....





# 0404

TODO :

1. 파일 경로 정의
    - 절대 경로,상대 경로
    - 상대 경로에서만 생성 하도록
2. 파일 생성
    - 인공지능 응답 객체를 파일로 저장 하도록
3. 파일 업로드
    - 생성된 파일을 미나오에 업로드

생각: 경로에 파일 생성하지 않고 응답 객체를 미니오에 바로 업로드 하면???


> 사용중인 윈도우 파일시스템
> - URI를 사용해 브라우저에서 볼 수 있다
> > `file:///D:/Project_Git/Back/`

### 오라클 자바 파일 스트림 참고

- https://dev.java/learn/modernio/

### 미나이오 문서

- https://min.io/docs/minio/linux/developers/java/minio-java.html
- https://min.io/docs/minio/linux/developers/java/API.html#uploadObject

# 수행 목적

- 코드 실행
    - 실행방법
        - 파일?
        - 깃풀?
        - 사용자 피씨에서?
        - 검증만?
        -
            - 커맨드 전달

- 결과 피드백
    - 출력 결과를 인공지능이 전달 받아 다음 작업 수행

프로젝트 생성 실행에 필요한 방법은?

- 필요한 것
    - 공간
    - 파일 저장
    - 파일 다운로드
    - 파일 업로드 안함
    - 동기화? 필요 없음

# Mcp 구조 분해

호스트 -> 서버

호스트: 진입점
서버 : 서비스 , 기능 제공

프로그래밍 구성 요소
요청핸들러
알림핸들러
요청
알림

전송 방식
STDIO (Standard I/0)

- 서버
    - 서버 시작
    -
- 클라이언트
    - 커맨드,인수 정의
    - 세션 시작
      SSE
- 서버
    - api 생성
    - api 요청 응답 인수 정의
- 클라이언트
    - SSE 서버 호출 객체 생성
    - 객체 연결

`JSON-RPC2.0` 객체 사용하여 통신

데이터를 찾는 방법
> URI를 구성 한다

- 엔드포인트에 노출
- 리소스 템플릿

프롬프트 구현 예제
프롬프트 구조화 목록 생성
프롬프트 목록 전달

스위치 or if 분기하여 프롬프트 선택 실행 조건 생성

방법 .1 깃헙
깃클론 -> 실행 -> 결과를 컨테이너 외부에 저장

방법 .2 미니오
파일 생성 업로드 -> 다운로드 -> 실행 -> 결과 저장

둘다 우선 파일 생성부터 해야됨

인공지능으로 파일 생성 저장 방법

1.기존 호출 방식으로 생성을 요청
2.파일 생성
3.업로드

java I/O read / write
NIO (Non-Blocking I/O)

1. 경로
    - 절대 경로, 상대 경로
    - 텍스트 파일 읽기 작업 시 호출 인수

### 파일 생성 예제

- 경로 구성 , 파일명
    - 경로와 내용 인수 전달
    - 생성

파일 생성
파일 읽기
스트림
http

```java

public static void main(String[] args)
{
    Path filePath = Paths.get("./data", "example.txt");

    try {

        // Create directory if it doesn't exist
        Files.createDirectories(filePath.getParent());

        // Write content
        Files.writeString(filePath, "Advanced content!", StandardOpenOption.CREATE);
        System.out.println("File created successfully!");

    } catch (IOException e) {
        System.err.println("Error: " + e.getMessage());
    }

}

```

문제는 실행 후 피드백 전달 방법이?
프로젝트 실행
결과 저장 저장위치는?...
저장된 결과 읽기

# CLI

네, 제공해주신 mcp의 Dockerfile을 분석하고, 그 원리를 사용자님의 Java/Maven 프로젝트 생성 시나리오에 어떻게 적용할 수 있는지 살펴보겠습니다.

MCP Dockerfile 분석 및 핵심 원리:

Multi-stage Build: builder 단계와 release 단계를 분리했습니다.

builder: 소스 코드를 복사하고, 빌드 시 필요한 모든 의존성을 설치합니다 (npm install). 빌드 캐시(--mount=type=cache)를 사용하여 반복 빌드 속도를 높입니다. 빌드 결과물(dist)
을 생성합니다.

release: 최종 실행에 필요한 최소한의 파일만 builder 단계에서 복사합니다 (빌드 결과물, 프로덕션 의존성 정의 파일). npm ci를 사용하여 프로덕션 의존성만 설치하여 최종 이미지 크기를 최소화하고
설치 속도를 높입니다.

Executable Container: ENTRYPOINT ["node", "dist/index.js"]를 사용하여 컨테이너가 시작될 때 특정 스크립트(dist/index.js)를 실행하도록 정의합니다. 이
스크립트가 작업을 수행하고 정상적으로 종료되면 컨테이너도 함께 종료됩니다. (만약 index.js가 웹 서버처럼 계속 실행되는 코드라면 컨테이너는 종료되지 않습니다. 하지만 사용자님의 설명에 따르면 작업 후 종료되는
방식으로 이해하고 계신 것 같습니다.)

사용자님의 시나리오에 적용하기:

사용자님의 목표는 "1단계: AI가 Dockerfile 생성 -> 이미지 빌드 -> 컨테이너 실행", "2단계: 컨테이너 내부에서 Maven 프로젝트 생성", "3단계: 결과 처리 후 컨테이너 종료/제거" 입니다.
MCP Dockerfile의 원리를 여기에 적용해 봅시다.

Dockerfile 생성 (AI 담당):

AI는 사용자님의 구조화된 옵션(groupId, artifactId, 사용할 Maven/JDK 버전 등)을 받아 Dockerfile을 생성합니다.

Multi-stage 적용? 이 시나리오에서는 Maven 프로젝트 생성 자체가 주 목적이므로, 복잡한 빌드 과정이 없습니다. 따라서 Multi-stage는 과도한 복잡성일 수 있습니다. 단일 스테이지
Dockerfile이 더 적합해 보입니다.

Dockerfile 내용 (예시):

# 1. 기본 이미지 선택 (Maven과 JDK 포함)

ARG MAVEN_VERSION=3.8
ARG JDK_VERSION=11
FROM maven:${MAVEN_VERSION}-openjdk-${JDK_VERSION} AS generator

# 2. 작업 디렉토리 설정

ARG WORK_DIR=/workspace
WORKDIR ${WORK_DIR}

# 3. (선택적이지만 권장) Non-root 사용자 설정

Maven 이미지는 기본적으로 root로 실행될 수 있으므로,
보안을 위해 non-root 사용자를 만들고 권한을 부여하는 것이 좋습니다.
이 경우 WORK_DIR과 Maven 캐시 디렉토리(/home/appuser/.m2)에 대한 쓰기 권한 필요.
RUN groupadd -r appuser && useradd -r -g appuser -d ${WORK_DIR} -s /sbin/nologin appuser
RUN mkdir -p /home/appuser/.m2 && chown -R appuser:appuser ${WORK_DIR} /home/appuser/.m2
USER appuser

# 4. (선택적) MinIO 클라이언트 설치 (3단계 옵션 A 선택 시)

RUN apt-get update && apt-get install -y --no-install-recommends wget ca-certificates && \
wget https://dl.min.io/client/mc/release/linux-amd64/mc -O /usr/local/bin/mc && \
chmod +x /usr/local/bin/mc && \
apt-get remove -y wget && apt-get autoremove -y && rm -rf /var/lib/apt/lists/*
RUN mc --version # 설치 확인

# 5. 컨테이너 시작 시 실행될 기본 명령어 (프로젝트 생성)

    CMD 나 ENTRYPOINT 를 사용합니다. 여기서는 CMD 예시.
    실제 groupId, artifactId 등은 docker run 시점에 커맨드를 덮어쓰거나,
    ENTRYPOINT 스크립트에서 환경 변수를 읽어 사용하도록 설계할 수 있습니다.
    아래는 단순히 기본 틀만 보여줍니다. 실제 값은 동적으로 채워져야 합니다.

CMD ["mvn", "archetype:generate", \
"-DgroupId=com.example.default", \
"-DartifactId=default-app", \
"-DarchetypeArtifactId=maven-archetype-quickstart", \
"-DarchetypeVersion=1.4", \
"-DinteractiveMode=false"]

# "-Dmaven.repo.local=/home/appuser/.m2" # Non-root 사용 시 캐시 경로 지정 필요할 수 있음

Use code with caution.
Dockerfile
Orchestrator (Java ProcessBuilder) 실행 흐름:

AI로부터 위와 같은 Dockerfile 내용을 받습니다.

임시 파일(예: Dockerfile.gen)에 저장합니다.

docker build -t my-maven-generator:temp -f Dockerfile.gen . 명령으로 이미지를 빌드합니다. (빌드 컨텍스트 .는 보통 비어있어도 됩니다.)

docker run 명령을 실행합니다. 이때 Dockerfile의 CMD를 덮어씁니다.

# ProcessBuilder로 실행할 명령어 예시 (보안 옵션 포함)

docker run \
--rm \
--name project-gen-$(date +%s) \
--user 1001:1001 `# non-root UID/GID, Dockerfile에서 생성한 사용자와 맞춰야 함` \
--cap-drop=ALL \
--read-only \
--tmpfs /workspace:rw,size=512m `# 프로젝트 생성 공간 (쓰기 가능)` \
--tmpfs /home/appuser/.m2:rw,size=1g `# Maven 캐시 공간 (non-root 사용자 경로 예시)` \
--network bridge `# Maven 다운로드를 위해 네트워크 필요` \
--memory 2g --cpus 1.0 --pids-limit 200 `# 리소스 제한` \

# --env MINIO_ACCESS_KEY=... --env MINIO_SECRET_KEY=... # 옵션 A 선택 시 자격 증명 전달 (보안 주의!)

my-maven-generator:temp \

# Dockerfile의 CMD를 여기서 덮어씁니다 (실제 값 사용)

mvn archetype:generate \
-DgroupId=com.mycompany.actual \
-DartifactId=actual-app \
-DarchetypeArtifactId=maven-archetype-quickstart \
-DarchetypeVersion=1.4 \
-DinteractiveMode=false \
-Dmaven.repo.local=/home/appuser/.m2 `# non-root 캐시 경로 명시`

# 옵션 A 선택 시: && mc config host add ... && mc cp ... # 업로드 명령 추가

Use code with caution.
Bash
process.waitFor()로 컨테이너 종료를 기다리고 종료 코드를 확인합니다.

옵션 B (권장): 종료 코드가 0이면, docker cp project-gen-xxxx:/workspace/actual-app /host/temp/ 명령으로 생성된 프로젝트를 호스트로 복사합니다.

옵션 B: 호스트에서 Java S3 SDK로 MinIO에 업로드합니다.

업로드 성공 여부(옵션 A는 종료 코드, 옵션 B는 예외 처리)에 따라 분기하여 후속 처리합니다.

--rm 옵션으로 컨테이너는 자동 제거됩니다.

docker rmi my-maven-generator:temp 명령으로 임시 이미지를 제거합니다.

MCP 방식과의 유사점 및 적용:

Executable Container: 생성된 이미지는 docker run으로 실행 시 특정 작업(Maven 프로젝트 생성)을 수행하고 종료되도록 설계됩니다 (CMD 또는 ENTRYPOINT 활용). 이것이
사용자님이 원하시는 "명령 수행 후 종료" 방식과 일치합니다.

최소 환경: 비록 Multi-stage는 아니지만, 필요한 도구(Maven, JDK, 선택적 mc)만 포함된 이미지를 사용합니다.

캐싱 (간접적): Dockerfile 빌드 시 레이어 캐싱은 적용됩니다. tmpfs를 사용하여 컨테이너 실행 중 Maven 캐시를 관리하지만, 실행 간 캐싱은 일회성 컨테이너 원칙상 포기합니다 (이는 보안과 격리를
위한 트레이드오프).

결론:

MCP Dockerfile의 핵심 원리 중 "컨테이너를 특정 작업을 수행하고 종료하는 실행 파일처럼 만드는 것 (ENTRYPOINT/CMD)" 은 사용자님의 시나리오에 직접적으로 적용 가능하며, AI가 생성한
Dockerfile에 CMD나 ENTRYPOINT로 Maven 생성 명령을 정의하면 됩니다.

Multi-stage 빌드는 이 특정 작업에는 과할 수 있으므로 단일 스테이지 이미지가 더 적합합니다.

중요한 것은 AI가 안전하고 정확한 Dockerfile을 생성하도록 유도하고, Java Orchestrator가 docker build로 이미지를 만든 뒤, 모든 필수 보안 옵션을 포함하여 docker run
명령어를 구성하고 실행하는 것입니다. 이때 Dockerfile의 기본 CMD를 실제 파라미터가 포함된 명령으로 덮어쓰는 것이 일반적입니다.

# Ref

- https://github.com/quarkiverse/quarkus-docker-client/blob/main/docs/modules/ROOT/pages/index.adoc
- https://github.com/docker-java/docker-java/blob/main/docs/getting_started.md

# code-with-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
