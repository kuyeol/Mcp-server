#!/bin/bash

# 스크립트 실행 중 오류 발생 시 즉시 종료
set -e
# 정의되지 않은 변수 사용 시 오류 발생
set -u
# 파이프라인 중간에서 오류 발생 시 종료
set -o pipefail

# --- 설정 변수 ---
# 임시 이미지 및 컨테이너 이름에 사용할 고유 식별자 (타임스탬프)
TIMESTAMP=$(date +%Y%m%d%H%M%S)
# 생성할 임시 Docker 이미지 태그
IMAGE_TAG="maven-project-generator:temp-${TIMESTAMP}"
# 생성할 임시 Dockerfile 이름
DOCKERFILE_NAME="Dockerfile.temp.${TIMESTAMP}"
# 컨테이너 내부 작업 디렉토리
WORKDIR_IN_CONTAINER="/workspace"
# Maven 캐시 디렉토리 (기본 maven 이미지의 root 사용자 기준)
# non-root 사용 시 경로 변경 필요 (예: /home/appuser/.m2)
MAVEN_CACHE_DIR="/root/.m2"

# 생성할 Maven 프로젝트 정보
GROUP_ID="com.example.generated"
ARTIFACT_ID="my-generated-app-${TIMESTAMP}" # 고유 ID 부여
ARCHETYPE_ARTIFACT_ID="maven-archetype-quickstart"
ARCHETYPE_VERSION="1.4"

# Docker 실행 옵션 (보안 및 리소스 제한)
# non-root 사용자로 실행하려면 -u 옵션 추가 및 tmpfs 경로 조정 필요
DOCKER_RUN_OPTS=(
#"--rm"                      # 실행 후 컨테이너 자동 제거
"--name" "project-gen-${TIMESTAMP}" # 컨테이너 이름
"--cap-drop=ALL"            # 모든 Linux Capability 제거
"--read-only"               # 루트 파일 시스템 읽기 전용
#"--tmpfs" "${WORKDIR_IN_CONTAINER}:rw,size=512m,noexec" # 작업 공간 (쓰기 가능, 실행 불가)
#"--tmpfs" "${MAVEN_CACHE_DIR}:rw,size=1g"             # Maven 캐시 (쓰기 가능)
"--network" "bridge"        # Maven 다운로드를 위해 네트워크 필요
"--memory" "2g" limit       # 메모리 제한 (예: 2GB)
"--cpus" "1.0"              # CPU 제한 (예: 1 코어)
"--pids-limit" "200"        # 프로세스 개수 제한
# "--user" "1001:1001"      # non-root 실행 시 주석 해제 및 UID/GID 지정
)

# 컨테이너 내부에서 실행할 Maven 명령어 배열
MAVEN_COMMAND=(
"mvn"
"archetype:generate"
"-DgroupId=${GROUP_ID}"
"-DartifactId=${ARTIFACT_ID}"
"-DarchetypeArtifactId=${ARCHETYPE_ARTIFACT_ID}"
"-DarchetypeVersion=${ARCHETYPE_VERSION}"
"-DinteractiveMode=false"
# "-Dmaven.repo.local=${MAVEN_CACHE_DIR}" # non-root 사용 시 캐시 경로 명시 필요할 수 있음
)

# --- 기본 Dockerfile 내용 정의 ---
# 나중에 이 부분을 AI가 생성하도록 대체할 수 있습니다.
# non-root 사용자 설정은 주석 처리됨 (필요시 활성화 및 DOCKER_RUN_OPTS 수정)
DOCKERFILE_CONTENT=$(cat <<EOF
# Maven과 JDK가 포함된 기본 이미지 사용
FROM maven:3.8-openjdk-11

# 작업 디렉토리 설정
ARG WORK_DIR=${WORKDIR_IN_CONTAINER}
WORKDIR \${WORK_DIR}

# (선택적) non-root 사용자 설정
# RUN groupadd -r appuser --gid 1001 && useradd -r -g appuser --uid 1001 --home-dir \${WORK_DIR} --shell /sbin/nologin appuser
# RUN mkdir -p /home/appuser/.m2 && chown -R appuser:appuser \${WORK_DIR} /home/appuser/.m2
# USER appuser

# 기본 실행 명령어 (docker run 시 덮어쓸 예정이므로 중요하지 않음)
CMD ["mvn", "--version"]
EOF
)

# --- 정리 함수 ---
# 스크립트 종료 시 (정상 또는 오류) 임시 파일 및 이미지 정리
  cleanup() {
echo "--- Cleaning up ---"
if [[ -f "$DOCKERFILE_NAME" ]]; then
echo "Removing temporary Dockerfile: $DOCKERFILE_NAME"
rm "$DOCKERFILE_NAME"
fi
# 이미지가 존재하는지 확인 후 삭제
if docker image inspect "$IMAGE_TAG" &> /dev/null; then
echo "Removing temporary Docker image: $IMAGE_TAG"
docker rmi "$IMAGE_TAG" || echo "Warning: Failed to remove image $IMAGE_TAG"
fi
echo "Cleanup finished."
}
# 스크립트 종료, 인터럽트(Ctrl+C), 종료 요청 시 cleanup 함수 실행 등록
trap cleanup EXIT INT TERM

# --- 스크립트 실행 ---

echo "--- Step 1: Preparing Environment ---"
echo "Generating temporary Dockerfile: $DOCKERFILE_NAME"
echo "$DOCKERFILE_CONTENT" > "$DOCKERFILE_NAME"
echo "Dockerfile content:"
cat "$DOCKERFILE_NAME"
echo "-------------------------------------"

echo "Building temporary Docker image: $IMAGE_TAG"
docker build -t "$IMAGE_TAG" -f "$DOCKERFILE_NAME" .
echo "Image build successful."
echo "-------------------------------------"

echo "--- Step 2: Running Maven Project Generation in Container ---"
echo "Executing command: docker run ${DOCKER_RUN_OPTS[*]} $IMAGE_TAG ${MAVEN_COMMAND[*]}"

# docker run 명령어 실행 및 종료 코드 캡처
# 표준 출력/에러는 터미널에 표시됨 (필요시 파일로 리디렉션 가능)

set +e # docker run 실패 시 스크립트 즉시 종료 방지
docker run "${DOCKER_RUN_OPTS[@]}" "$IMAGE_TAG" "${MAVEN_COMMAND[@]}"
EXIT_CODE=$?
set -e # 오류 시 즉시 종료 다시 활성화
echo "-------------------------------------"

echo "--- Step 3: Processing Result ---"
CONTAINER_NAME="project-gen-${TIMESTAMP}" # 컨테이너 이름 변수 사용
HOST_OUTPUT_DIR="./generated_projects/${ARTIFACT_ID}" # 호스트에 저장할 경로


if [[ $EXIT_CODE -eq 0 ]]; then
echo "Success: Maven project '${ARTIFACT_ID}' generated successfully inside the container (exit code 0)."

# --- 파일 복사 단계 추가 ---
echo "Copying generated project from container '${CONTAINER_NAME}' to host path '${HOST_OUTPUT_DIR}'..."
mkdir -p "$(dirname "$HOST_OUTPUT_DIR")" # 호스트 경로의 부모 디렉토리 생성
# docker cp <컨테이너 이름>:<컨테이너 내부 경로> <호스트 경로>
docker cp "${CONTAINER_NAME}:${WORKDIR_IN_CONTAINER}/${ARTIFACT_ID}" "${HOST_OUTPUT_DIR}"
if [[ $? -eq 0 ]]; then
  echo "Project files copied successfully to ${HOST_OUTPUT_DIR}"
# 여기에 MinIO 업로드 로직 추가 가능
# upload_to_minio "${HOST_OUTPUT_DIR}" # 예시 함수 호출
else
  echo "Error: Failed to copy project files from container."
# 실패 처리
fi

else # <-- 외부 if 문의 else 시작
  echo "Failure: Maven project generation failed (exit code $EXIT_CODE)."
  echo "Check the logs above for errors."
  # 실패 처리
fi # <<----- !!! 여기!!! 외부 if 문을 닫는 'fi' 추가 !!!

echo "-------------------------------------"


# --- 컨테이너 수동 삭제 추가 ---
# cleanup 함수 전에 컨테이너를 명시적으로 삭제
echo "Removing container '${CONTAINER_NAME}'..."
docker rm -f "${CONTAINER_NAME}" || echo "Warning: Failed to remove container ${CONTAINER_NAME}"

# --- 컨테이너 수동 삭제 끝 ---
# cleanup 함수는 trap에 의해 자동으로 실행됩니다.

exit 0 # 스크립트 정상 종료