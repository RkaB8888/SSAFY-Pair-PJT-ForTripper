<script setup>
import RecentFriendView from "@/views/HomeViews/RecentFriendView.vue";
// 더미 데이터 (API 요청으로 대체 가능)
const posts = [
  {
    id: 1,
    nickname: "Friend 1",
    title: "제목 1",
    content: "게시글 내용 1",
    avatar: "@/assets/img/Default_Profile.png", // 프로필 사진 없음
    image: "https://via.placeholder.com/300", // 게시글 이미지
  },
  {
    id: 2,
    nickname: "Friend 2",
    title: "제목 2",
    content: "게시글 내용 2",
    avatar: "@/assets/img/Default_Profile.png", // 프로필 사진 없음
    image: "https://via.placeholder.com/300",
  },
  {
    id: 3,
    nickname: "Friend 3",
    title: "제목 3",
    content: "게시글 내용 3",
    avatar: "@/assets/img/Default_Profile.png", // 프로필 사진 없음
    image: "https://via.placeholder.com/300",
  },
];
</script>

<template>
  <div class="recent-friends-board">
    <h2>친구 소식</h2>

    <!-- Carousel -->
    <v-slide-group show-arrows>
      <v-slide-item v-for="post in posts" :key="post.id">
        <RecentFriendView
          :nickname="post.nickname"
          :title="post.title"
          :content="post.content"
          :avatar="post.avatar"
          :image="post.image"
        />
      </v-slide-item>
    </v-slide-group>
  </div>
</template>

<style scoped>
.recent-friends-board {
  padding: 16px;
}
</style>
