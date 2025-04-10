
> Kafka는 **분산형 메시징 시스템**으로, 데이터를 생성하는 프로듀서와 소비하는 컨슈머 간의 연결을 담당합니다. 다음은 주요 구성요소와 작동 방식을 단계별로 설명한 내용입니다:

### 1. **주요 구성 요소**

> **프로듀서(Producer)** : 
> - 메시지를 Kafka **토픽**에 보냅니다. 예를 들어, 검색 이벤트에 대한 메시지는 'searches'라는 토픽으로 보내질 수 있습니다.

> **토픽(Topic)** : 
> - 동일 유형의 메시지를 저장하는 **피드**입니다. 각 토픽은 여러 **브로커**에 분산됩니다.

> **브로커(Broker)** : 
> - 각 Kafka 클러스터는 여러 브로커로 이루어지며, 브로커는 특정 토픽의 섹션을 저장합니다.

> **파티션(Partition)** : 
> - 토픽 내 메시지는 순서대로 기록된 파티션에 저장됩니다. 파티션은 메시지에 대한 병렬 처리를 가능하게 합니다.

> **컨슈머(Consumer)** : 
> - 메시지를 구독하여 처리합니다. 컨슈머 그룹은 메시지를 수신하고 각 파티션별로 로드 밸런싱을 수행합니다.

### 2. **작동 방식**
1. **메시지 전송**:
    - 프로듀서는 메시지를 생성하고 특정 토픽으로 메시지를 보냅니다. 메시지는 브로커의 파티션에 추가되며, 특정 키를 사용해 파티션이 선택될 수 있습니다.
    - 메시지는 한 번 작성되면 삭제되지 않고 브로커에서 유지되므로 여러 컨슈머가 접근할 수 있습니다.

2. **메시지 소비**:
    - 컨슈머는 특정 파티션에서 지정된 메시지 ID부터 메시지를 읽습니다.
    - 컨슈머 그룹은 자동으로 로드 밸런싱을 수행하며, 그룹 내 각 노드가 지정된 파티션만 소비하도록 조정됩니다.
    - 소비한 메시지는 컨슈머가 자체적으로 기록하며, 충돌 시에는 소량의 메시지 재처리로 복구할 수 있습니다.

3. **파티션 관리**:
    - 각 파티션은 메시지의 순서를 보장하며, 컨슈머 그룹이 메시지를 처리할 때 병렬성을 제공합니다.
    - 컨슈머와 파티션의 동적 조정은 Zookeeper를 통해 이루어지며, 시스템이 자동으로 노드 증감 및 로드 밸런싱을 관리합니다.

### 3. **주요 특징**
- 메시지 전달은 **최소 1회 전달 보장(At-least-once)** 방식으로 이루어지며, 컨슈머가 확인되지 않은 메시지를 재처리할 수 있습니다.
- 데이터의 **압축 및 배치 처리**로 네트워크 및 디스크 I/O 효율을 극대화하여 고성능을 제공합니다.
- 다양한 구독 시스템을 지원하며, 실시간 데이터 소비와 배치 데이터 소비를 모두 처리합니다.

