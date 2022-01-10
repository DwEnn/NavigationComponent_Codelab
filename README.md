# Navigation Component Codelab

## 목표

- [코드랩 진행](https://developer.android.com/codelabs/android-navigation?index=..%2F..%2Findex&hl=ko#3)
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

## Activity 와 탐색

탐색 구성요소는 탐색 원리에 설명된 안내를 따른다. 탐색 원리에서는 Activity 를 앱의 시작 지점으로 사용할 것을 권장한다.

Activity 에는 하단 탐색 메뉴와 같은 전역 탐색도 포함된다.

## [탐색 원리](https://developer.android.com/guide/navigation/navigation-principles?hl=ko)

### 고정 시작 대상

빌드하는 모든 앱에는 고정 시작 대상이 있다. 이 대상은 사용자가 런처에서 앱을 실행할 때 처음으로 표시되는 화면이다. 또한 사용자가 `뒤로 버튼` 을 누른 후 런처로 돌아오면 마지막으로 표시되는 화면이기도 한다.

아래는 예시로 작성된 [Sunflower 앱](https://github.com/android/sunflower/tree/main/app) 이다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c7cc46cb-4dc1-45f9-8cb8-93dbd8166896/Untitled.png)

> `Launcher` 에서 Sunflower 앱을 실행하면 `List Screen` 이 가장 먼저 사용자에게 표시되고 앱을 종료하기 전에 마지막으로 표시되는 화면이기도 하다.
>

### 대상의 스택으로 표현되는 탐색 상태

앱이 처음으로 실행되면 새 Task 가 생성되고 앱에서 이 task 의 시작 대상을 표시한다. 이 시작 대상이 **백 스택**으로 알려진 기본 대상이 되며 앱 탐색 상태의 기본이다.

스택의 상단은 현재 화면이며 스택의 이전 대상은 방문한 위치의 기록을 나타낸다. 백 스택에는 항상 스택 하단에 앱의 시작 대상이 있다.

백 스택을 변경하는 작업은 스택 상단으로 새 대상을 푸시하거나 최상위에 있는 대상을 스택에서 삭제하여 항상 스택 상단에서 실행된다. 대상으로 이동하면 그 대상이 스택 상단으로 푸시된다.

### 위로 및 뒤로는 앱 작업 내에서 동일하다

`뒤로 버튼` 은 화면 하단의 시스템 탐색 메뉴에 표시되고 사용자가 최근에 작업한 화면 기록을 역순으로 탐색하는데 사용된다.

뒤로 버튼을 누르면 현재 대상이 백 스택 상단에서 사라지고 이전 대상으로 이동한다.

`위로 버튼` 은 화면 상단의 앱 바에 표시된다. 앱의 task 내에서 위로 버튼과 뒤로 버튼은 동일하게 작동한다.

### 위로 버튼은 앱을 종료하지 않는다

**앱이 시작 대상에 있는 경우**

- 위로 버튼 표시되지 않음
- 뒤로 버튼은 표시되고 앱을 종료한다

**앱이 다른 앱의 task 에서 딥 링크를 사용하여 실행된 경우**

- 위로 버튼은 사용자를 다시 앱의 task 및 *시뮬레이션된 백 스택*을 통해 전환하며, 딥 링크를 트리거한 앱으로는 전환하지 않는다
- 뒤로 버튼을 누르면 다른 앱으로 돌아간다

### 수동 탐색을 시뮬레이션하는 딥 링크






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