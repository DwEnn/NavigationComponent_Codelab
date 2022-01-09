# Navigation Component Codelab

## 목표

- 코드랩 진행
- 탐색 구성요소 내용 정리

---

탐색 아키텍처 구성요소는 탐색 구현을 간소화하면서 개발자가 앱의 탐색 흐름을 시각화하는데 도움을 준다. 이 라이브러리는 아래와 같은 이점들을 제공한다.

- 프래그먼트 트랜잭션 자동 처리
- 기본적으로 위로 및 뒤로 동작을 올바르게 처리
- 애니메이션 및 전환의 기본 동작
- 일급 작업으로서의 딥 링크
- 추가 작업 없이 탐색 UI 패턴 구현(예: 탐색 창과 하단 탐색 메뉴)
- 탐색 중에 정보를 전달할 때 유형 안정성
- 앱의 탐색 흐름을 시각화하고 수정하는 Android 스튜디오 도구


## 탐색 개요

- 탐색 그래프(새 xml 리소스) : 하나의 중앙 위치에 모든 탐색 관련 정보가 포함된 리소스이다. 여기에는 **Destinations** 이라고 하는 앱의 모든 위치와 사용자가 앱에서 선택 가능한 경로가 포함된다.
- NavHostFragment(레이아웃 xml 뷰) : 레이아웃에 추가하는 특수 위젯이다. 탐색 그래프와는 다른 대상을 표시한다.
- NavController(Kotlin/자바 객체) : 탐색 그래프 내에서 현재 위치를 계속 추적하는 객체이다. 탐색 그래프에서 이동할 때 `NavHostFragment` 에서 대상 콘텐츠 전환을 조정한다.

탐색 시 `NavController` 객체를 사용하여 이동하려는 위치나 탐색 그래프에서 선택하려는 경로를 알려준다. 그러면 `NavController` 가 `NavHostFragment` 에 적절한 대상을 표시한다.

## 탐색 그래프

탐색 구성요소에는 Destinations 이라는 개념이 도입된다.

> Destinations :
앱에서 이동할 수 있는 모든 위치이며 일반적으로 프래그먼트나 액티비티이다.
>

탐색 그래프는 사용자가 앱에서 선택 가능한 모든 경로를 정의하는 새로운 리소스 유형이다. 지정된 Destination 에서 도달할 수 있는 모든 Destination 을 시각적으로 표시한다.

### 탐색 xml 파일 분석

```xml
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            xmlns:tools="http://schemas.android.com/tools"
    app:startDestination="@+id/home_dest">

    <!-- ...tags for fragments and activities here -->

</navigation>
```

- `<navigation>` 모든 탐색 그래프의 루트 노드
- `<navigation>` 에는 `<activity>` 또는 `<fragment>` 요소로 표시된 destination 이 하나 이상 포함된다
- `app:startDestination` 은 사용자가 앱을 처음 열 때 기본적으로 실행되는 대상을 지정하는 속성

```xml
<fragment
    android:id="@+id/flow_step_one_dest"
    android:name="com.example.android.codelabs.navigation.FlowStepFragment"
    tools:layout="@layout/flow_step_one_fragment">
    <argument
        .../>

    <action
        android:id="@+id/next_action"
        app:destination="@+id/flow_step_two_dest">
    </action>
</fragment>
```

- `android:id` 는 이 xml 및 코드의 다른 위치에서 destination 을 참조하는 데 사용할 수 있는 프래그먼트의 ID 를 정의한다
- `android:name` 은 destination 으로 이동할 때 인스턴스화할 프래그먼트의 정규화된 클래스 이름을 선언한다
- `tools:layout` 은 그래픽 편집기에서 표시해야하는 레이아웃을 지정

일부 `<fragment>` 태그에도 `<action>` , `<argument>` , `<deepLink>` 가 포함된다.








### License
-------

Copyright 2018 The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.