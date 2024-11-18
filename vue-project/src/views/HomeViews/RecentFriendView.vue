<script setup>
import { computed } from "vue";

// Props 정의
defineProps({
  nickname: { type: String, default: "Unknown" },
  title: { type: String, default: "제목 없음" },
  content: { type: String, default: "내용 없음" },
  avatar: { type: String, default: "" },
  image: { type: String, default: "" },
});

// 프로필 사진이 없는 경우 기본 이미지 사용
const profileImage = computed(() => {
  console.log("avatar:", avatar); // 디버깅용 로그
  return avatar && avatar.trim() !== "" ? avatar : "/img/Default_Profile.png";
});
</script>

<template>
  <v-card class="friend-card">
    <!-- 카드 상단: 프로필 사진, 닉네임, 닫기 버튼 -->
    <v-card-title>
      <div class="d-flex align-center">
        <v-avatar size="40">
          <img :src="avatar" alt="프로필" />
        </v-avatar>
        <span class="nickname ml-3">{{ nickname }}</span>
      </div>
      <v-btn icon>
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-card-title>

    <!-- 게시글 이미지 -->
    <v-img :src="image" class="post-image" />

    <!-- 제목과 내용 -->
    <v-card-subtitle>{{ title }}</v-card-subtitle>
    <v-card-text>{{ content }}</v-card-text>
  </v-card>
</template>

<style scoped>
.friend-card {
  width: 300px;
  margin: 8px;
}

.nickname {
  font-weight: bold;
}

.post-image {
  height: 200px;
}
</style>
