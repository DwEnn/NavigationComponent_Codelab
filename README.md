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
딥 링크 하는지 또는 수동으로 특정 대상으로 이동하는지와 상관없이 `위로 버튼` 을 사용하면 대상을 통해 다시 시작 대상으로 이동할 수 있다.

Sunflower 앱 예제를 통해 알아보자.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d36605b1-0094-4fc4-9b80-ecd7634b5a1d/Untitled.png)

> 사용자가 런처 화면에서 앱을 실행 → 사과의 세부정보 화면으로 이동
⇒ 백스택 결과 : Detail Screen - List Screen - Launch Screen
>

이 시점에 사용자는 `홈 버튼` 을 탭하여 앱을 백그라운드 상태로 전환하였다고 가정하자.

그런 다음 이 앱에 딥 링크 기능이 있어서 사용자가 특정 식물의 세부정보 화면을 이름으로 직접 실행할 수 있다고 가정 해보자.

이 딥 링크를 통해 앱을 열면 위 그림에 표시된 현재 Sunflower 백 스택이 아래와 같이 새로운 백 스택으로 완전히 대체된다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e2470c38-0b3d-414f-8b76-45104c7cfce1/Untitled.png)

> Sunflower 백 스택은 상단에 아보카도 세부정보 화면이 있는 합성 백 스택으로 대체된다.
사용자가 이전에 사과 세부정보 화면에 있었다는 앱 정보를 포함하여 원래의 백 스택은 사라진 것이다.
>

이 화면은 앱을 유기적으로 탐색하여 얻을 수 있는 백 스택과 일치해야 한다.

이러한 요구사항을 충족하기 위해 생성된 합성 백 스택은 `NavGraph` 를 기반으로 단순화된 백 스택이다. 중첩이 없는 간단한 `NavGraph` 의 경우 이는 시작 대상과 딥 링크 대상으로 구성된다.

더 복잡하게 [중첩된 탐색 그래프](https://developer.android.com/guide/navigation/navigation-nested-graphs?hl=ko)의 경우 합성 백 스택은 딥 링크 대상의 상위 항목인 모든 중첩 그래프의 시작 대상도 포함한다.

## NavController

사용자가 버튼을 클릭하는 등의 작업을 할 때 탐색 명령어를 트리거해야 한다.

`NavController` 라는 특수 클래스는 `NavHostFragment` 에서 프래그먼트 교체를 트리거하는 것이다

```kotlin
// command to navigate to flow_step_one_dest
findNavController().navigate(R.id.flow_step_one_dest)
```

`NavController` 는 강력하다. `navigate()` 또는 `popBackStack()` 과 같은 메서드를 호출할 때 이동하는 대상의 유형에 따라 이러한 명령어를 적절한 프레임워크 작업으로 변환하기 때문이다.

> 예로, Activity 대상과 함께 `navigate()` 를 호출하면 `NavController` 에서 개발자를 대신해 `startActivity()` 를 호출한다.
>

### NavHostFragment 와 연결된 NavController 객체를 가져오는 방법

- Fragment.findNavController()
- View.findNavController()
- Activity.findNavController(viewId: Int)

<aside>
📢 **NavController 는 NavHostFragment 와 연결된다.** 따라서 어떤 메서드를 사용하든지 프래그먼트, 뷰 또는 뷰 ID 가 NavHostFragment 자체인지 아니면 NavHostFragment 를 상위 요소로 보유하는지 확인해야 한다. 그렇지 않으면 IllegalStateException 이 발생한다.

</aside>


### enter/exit, popEnter/popExit 애니메이션의 차이

간단한 예로 확인해 보자.

Fragment A 를 Fragment B 로 replace

- Fragment B  `enter` anim
- Fragment A `exit` anim

백 버튼을 눌러 replace 실행을 취소

- Fragment B `popExit` anim
- Framgnet A `popEnter` anim

**그렇다면 이제 컨테이너가 이미 fragment 를 가지고 있는 경우와 아닌 경우에 대해 알아보자.**

Fragment 0 이라는 fragment 를 컨테이너가 가지고 있는 상황 → Fagment A 로 replace 작업 실행

- Fragment B `popExit` anim
- Fragment 0 `popEnter` anim

컨테이너가 비어있음 → Fragment A 로 replace 작업 실행

- Fragment B `popExit` anim
- 컨테이너가 비어있기 때문에 popEnter 애니메이션 실행되지 않음







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