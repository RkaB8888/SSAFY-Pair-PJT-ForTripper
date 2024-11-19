<script setup>
import { computed } from "vue";

// Props 정의
const props = defineProps({
  post: {
    type: Object,
    required: true,
    default: () => ({
      nickname: "Unknown",
      title: "제목 없음",
      content: "내용 없음",
      avatar: "",
      image: "",
    }),
  },
});

// 프로필 이미지 처리
const profileImage = computed(() => {
  return props.post.avatar && props.post.avatar.trim() !== ""
    ? props.post.avatar
    : "/img/Default_Profile.png";
});
</script>

<template>
  <v-card class="friend-card">
    <!-- 카드 상단: 프로필 사진, 닉네임, 닫기 버튼 -->
    <v-card-title>
      <div class="d-flex justify-space-between align-center w-100">
        <!-- 왼쪽: 프로필 사진과 닉네임 -->
        <div class="d-flex align-center">
          <v-avatar size="40">
            <img :src="profileImage" alt="프로필" class="avatar-img" />
          </v-avatar>
          <span class="nickname ml-3">{{ post.nickname }}</span>
        </div>

        <!-- 오른쪽: 닫기 버튼 -->
        <v-btn icon>
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </div>
    </v-card-title>

    <!-- 게시글 이미지 -->
    <v-img :src="post.image" class="post-preview-image" />

    <!-- 제목과 내용 -->
    <v-card-subtitle class="card-title">{{ post.title }}</v-card-subtitle>
    <v-card-text class="card-text">{{ post.content }}</v-card-text>
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

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

/* 게시글 미리보기 이미지 스타일 */
.post-preview-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
  background-color: #eeeeee;
  margin-bottom: 16px;
}

/* 제목 스타일 */
.card-title {
  font-weight: bold; /* 진한 글씨 */
  font-size: 1.1rem; /* 약간 더 큰 글씨 */
  color: #333; /* 진한 색상 */
  margin-bottom: 8px; /* 내용과의 간격 */
}

/* 게시글 내용 스타일 */
.card-text {
  width: 100%;
  height: 5em; /* line-height(1.5) * 3줄 */
  line-height: 1.5em; /* 줄 높이 */
  overflow: hidden; /* 초과 텍스트 숨김 */
  text-overflow: ellipsis; /* 생략(...) 처리 */
  display: -webkit-box; /* Flexbox 기반 줄 수 제한 */
  -webkit-line-clamp: 3; /* WebKit 기반 브라우저에서 3줄로 제한 */
  -webkit-box-orient: vertical; /* 수직 방향 설정 */
  padding: 8px 16px 0px; /* 텍스트와 주변 간격 */
  margin-bottom: 16px;
  background-color: #f9f9f9;
  border-radius: 4px;
  color: #666; /* 연한 색상 */
}
</style>
