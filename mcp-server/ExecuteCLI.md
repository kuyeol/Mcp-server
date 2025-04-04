
# 도커 실행 커맨드

- 문서 참고 생성
> 실행 후 제거 컨테이너 없음
```shell
docker run --name "java-$(date +%Y%m%d_%H%M%S_%N)" -v "$(pwd)":/ws --workdir=/ws -ti jbangdev/jbang-action helloworld.java
```

- ai 생성 스크립트
> 실행 후 컨테이너 삭제, 출력 저장 
```shell
docker run --rm -v "$(pwd)/my-test-data":/app/storage alpine sh -c "echo '이 데이터는 호스트에 남습니다.' > /app/storage/message.txt && ls -l /app/storage"
```

## 도커 커맨드 조합
> 도커실행후제거,디렉토리에 로그저장(생성시간포함),--verbose 인수전달
```shell
docker run --rm  --name "java-$(date +%Y%m%d_%H%M%S_%N)" -v "$(pwd)":/ws --workdir=/ws -ti jbangdev/jbang-action --verbose helloworld.java  > "java-$(date +%Y%m%d_%H%M%S_%N)".log 2> "java-$(date +%Y%m%d_%H%M%S_%N)"error.log
```

# 클래스 결과를 파일로 저장
jbang hello.java > output.log 2> error.log

- 구조화 
```shell
#!/bin/bash

JAVA_SCRIPT="YourScript.java"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
STDOUT_FILE="output_${TIMESTAMP}.log"
STDERR_FILE="error_${TIMESTAMP}.log"
RESULT_FILE="result_${TIMESTAMP}.json" # 예: JSON 형식으로 저장

echo "Running ${JAVA_SCRIPT}..."

# JBang 실행 및 출력 리디렉션
jbang ${JAVA_SCRIPT} > "${STDOUT_FILE}" 2> "${STDERR_FILE}"
EXIT_CODE=$?

echo "Execution finished with exit code: ${EXIT_CODE}"

# 결과 요약 파일 생성 (예: JSON)
echo "{" > "${RESULT_FILE}"
echo "  \"script\": \"${JAVA_SCRIPT}\"," >> "${RESULT_FILE}"
echo "  \"timestamp\": \"${TIMESTAMP}\"," >> "${RESULT_FILE}"
echo "  \"exit_code\": ${EXIT_CODE}," >> "${RESULT_FILE}"
# stdout/stderr 내용을 JSON 문자열로 이스케이프 처리하는 것은 복잡할 수 있음
# 여기서는 파일 경로만 기록하거나, 간단한 내용만 포함
echo "  \"stdout_file\": \"${STDOUT_FILE}\"," >> "${RESULT_FILE}"
echo "  \"stderr_file\": \"${STDERR_FILE}\"" >> "${RESULT_FILE}"
# 또는 파일 내용을 직접 읽어와서 넣을 수도 있음 (jq 같은 도구 활용 추천)
# stdout_content=$(jq -Rs . < "${STDOUT_FILE}")
# echo "  \"stdout_content\": ${stdout_content}," >> "${RESULT_FILE}"
echo "}" >> "${RESULT_FILE}"

echo "Results summary saved to ${RESULT_FILE}"
echo "Stdout log: ${STDOUT_FILE}"
echo "Stderr log: ${STDERR_FILE}"

# 필요시 종료 코드를 반환하여 스크립트 자체의 성공/실패 나타내기
exit ${EXIT_CODE}

```

```shell
#!/bin/bash

JAVA_SCRIPT="YourScript.java"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
STDOUT_FILE="output_${TIMESTAMP}.log"
STDERR_FILE="error_${TIMESTAMP}.log"
RESULT_FILE="result_${TIMESTAMP}.json" # 예: JSON 형식으로 저장

echo "Running ${JAVA_SCRIPT}..."

# JBang 실행 및 출력 리디렉션
jbang ${JAVA_SCRIPT} > "${STDOUT_FILE}" 2> "${STDERR_FILE}"
EXIT_CODE=$?

echo "Execution finished with exit code: ${EXIT_CODE}"

# 결과 요약 파일 생성 (예: JSON)
echo "{" > "${RESULT_FILE}"
echo "  \"script\": \"${JAVA_SCRIPT}\"," >> "${RESULT_FILE}"
echo "  \"timestamp\": \"${TIMESTAMP}\"," >> "${RESULT_FILE}"
echo "  \"exit_code\": ${EXIT_CODE}," >> "${RESULT_FILE}"
# stdout/stderr 내용을 JSON 문자열로 이스케이프 처리하는 것은 복잡할 수 있음
# 여기서는 파일 경로만 기록하거나, 간단한 내용만 포함
echo "  \"stdout_file\": \"${STDOUT_FILE}\"," >> "${RESULT_FILE}"
echo "  \"stderr_file\": \"${STDERR_FILE}\"" >> "${RESULT_FILE}"
# 또는 파일 내용을 직접 읽어와서 넣을 수도 있음 (jq 같은 도구 활용 추천)
# stdout_content=$(jq -Rs . < "${STDOUT_FILE}")
# echo "  \"stdout_content\": ${stdout_content}," >> "${RESULT_FILE}"
echo "}" >> "${RESULT_FILE}"

echo "Results summary saved to ${RESULT_FILE}"
echo "Stdout log: ${STDOUT_FILE}"
echo "Stderr log: ${STDERR_FILE}"

# 필요시 종료 코드를 반환하여 스크립트 자체의 성공/실패 나타내기
exit ${EXIT_CODE}
```