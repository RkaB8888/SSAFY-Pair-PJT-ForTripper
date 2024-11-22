<script setup>
import RecentBoardView from "@/views/HomeViews/RecentBoardView.vue";

const posts = [
  {
    id: 1,
    nickname: "돌팔이",
    title: "이렇게 하면 무인도에서 살아남을 수 있다.",
    content:
      "무인도에서 살아남는 방법은 잘먹고 잘자면 됩니다. 길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트길이테스트",
    avatar: null, // 프로필 사진 없음
    image: "/img/No_Image.png", // 게시글 이미지
  },
  {
    id: 2,
    nickname: "싸피인",
    title: "1학기 끝",
    content: "1학기도 이제 얼마 남지 않았네요~ 생각보다 너무 짧게 느껴졌어요",
    avatar: null, // 프로필 사진 없음
    image:
      "/img/8SYj8yiphWO7XAEmbEpqIdUDRUbXHBhVtgxkmeo2j63MqkY_FBfxh8-S_JyJT5X2AcSl7hWYwUfK_s8KbXpy-Q.webp",
  },
  {
    id: 3,
    nickname: "뭉게뭉게",
    title: "날씨 실화냐",
    content:
      "갑자기 추워진거 뭐임 ㄷㄷ 너무 추워서 나오자마자 집에 들어감 ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ",
    avatar:
      "/img/80xZ8JG7rBuQeTZFP34FUuBZ86lHKEua4OF-a11QFKAbc2YJ_T4R5KesVHh5UULU1MgOB8rNk4JErQm-chUfIw.webp", // 프로필 사진 없음
    image:
      "/img/80xZ8JG7rBuQeTZFP34FUuBZ86lHKEua4OF-a11QFKAbc2YJ_T4R5KesVHh5UULU1MgOB8rNk4JErQm-chUfIw.webp",
  },
  {
    id: 4,
    nickname: "뭉게뭉게",
    title: "날씨 실화냐",
    content:
      "갑자기 추워진거 뭐임 ㄷㄷ 너무 추워서 나오자마자 집에 들어감 ㅋㅋ",
    avatar: null, // 프로필 사진 없음
    image: "/img/240301_Cha_Eun-woo.jpg",
  },
  {
    id: 5,
    nickname: "뭉게뭉게",
    title: "날씨 실화냐",
    content:
      "갑자기 추워진거 뭐임 ㄷㄷ 너무 추워서 나오자마자 집에 들어감 ㅋㅋ",
    avatar: null, // 프로필 사진 없음
    image: "/img/273786_540.jpg",
  },
  {
    id: 6,
    nickname: "뭉게뭉게",
    title: "날씨 실화냐",
    content:
      "갑자기 추워진거 뭐임 ㄷㄷ 너무 추워서 나오자마자 집에 들어감 ㅋㅋ",
    avatar: null, // 프로필 사진 없음
    image:
      "/img/DichrZW_i8kBqSycEberk-bb8fSZLA7HTMN84k039j6wa_I8W5BItBn7U-MifTQuV82EIyKXnAA5LMRhKTlveg.webp",
  },
  {
    id: 7,
    nickname: "뭉게뭉게",
    title: "날씨 실화냐",
    content:
      "갑자기 추워진거 뭐임 ㄷㄷ 너무 추워서 나오자마자 집에 들어감 ㅋㅋ",
    avatar: null, // 프로필 사진 없음
    image: "/img/htm_2016121317575698565.jpg",
  },
  {
    id: 8,
    nickname: "뭉게뭉게",
    title: "날씨 실화냐",
    content:
      "갑자기 추워진거 뭐임 ㄷㄷ 너무 추워서 나오자마자 집에 들어감 ㅋㅋ",
    avatar: null, // 프로필 사진 없음
    image: "/img/jms-OqBAhbdRsFg-unsplash.jpg",
  },
  {
    id: 9,
    nickname: "뭉게뭉게",
    title: "날씨 실화냐",
    content:
      "갑자기 추워진거 뭐임 ㄷㄷ 너무 추워서 나오자마자 집에 들어감 ㅋㅋ",
    avatar: null, // 프로필 사진 없음
    image: "/img/martin-brechtl-PIyGabTvIas-unsplash.jpg",
  },
  {
    id: 10,
    nickname: "뭉게뭉게",
    title: "날씨 실화냐",
    content:
      "갑자기 추워진거 뭐임 ㄷㄷ 너무 추워서 나오자마자 집에 들어감 ㅋㅋ",
    avatar: null, // 프로필 사진 없음
    image: "/img/sea-164989_1920.jpg",
  },
];
</script>

<template>
  <div class="recent-board">
    <h2>최신 게시글</h2>

    <!-- Carousel -->
    <v-slide-group show-arrows>
      <v-slide-group-item v-for="post in posts" :key="post.id">
        <RecentBoardView :post="post" />
      </v-slide-group-item>
    </v-slide-group>
  </div>
</template>

<style scoped>
.recent-board {
  padding: 16px;
}
</style>
