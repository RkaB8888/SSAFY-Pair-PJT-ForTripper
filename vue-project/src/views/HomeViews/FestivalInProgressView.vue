<script setup>
import { computed } from "vue";

// Props 정의
const props = defineProps({
  festival: {
    type: Object,
    required: true,
    default: () => ({
      title: "제목 없음",
      addr1: "주소 없음",
      addr2: "주소 없음",
      tel: "N/A",
      firstimage: "",
      // 추가적인 필드 필요 시 추가
    }),
  },
});

// 축제 이미지 처리
const festivalImage = computed(() => {
  return props.festival.firstimage && props.festival.firstimage.trim() !== ""
    ? props.festival.firstimage
    : "/img/No_Image.png"; // 기본 이미지
});

// 전화번호와 인스타그램 링크 처리
const processedContactInfo = computed(() => {
  const tel = props.festival.tel || "N/A";

  // HTML 태그를 포함한 경우
  if (/<\/?[a-z][\s\S]*>/i.test(tel)) {
    const linkMatch = tel.match(/href="([^"]+)"/); // 링크 추출
    const link = linkMatch ? linkMatch[1] : null;

    return {
      tel: "N/A", // 전화번호를 N/A로 표시
      instagram: link ? link : null, // 인스타그램 링크 추출
    };
  }

  // 일반 텍스트인 경우
  return {
    tel: tel,
    instagram: null,
  };
});
</script>

<template>
  <v-card class="festival-card">
    <!-- 축제 이미지 -->
    <v-img
      :src="festivalImage"
      class="post-preview-image"
      @error="onImageError"
      alt="축제 이미지"
    ></v-img>

    <!-- 축제 제목 -->
    <v-card-title class="card-title">{{ festival.title }}</v-card-title>

    <!-- 축제 위치 -->
    <v-card-subtitle class="card-location">
      {{ festival.addr1 }}, {{ festival.addr2 }}
    </v-card-subtitle>

    <!-- 연락처 정보 -->
    <v-card-text class="card-text">
      <p><strong>전화번호:</strong> {{ processedContactInfo.tel }}</p>
      <p v-if="processedContactInfo.instagram">
        <strong>인스타그램 DM:</strong>
        <a
          :href="processedContactInfo.instagram"
          target="_blank"
          rel="noopener noreferrer"
        >
          {{ processedContactInfo.instagram }}
        </a>
      </p>
    </v-card-text>
  </v-card>
</template>
<style scoped>
.festival-card {
  width: 300px;
  margin: 8px;
}

.card-title {
  font-weight: bold;
  font-size: 1.1rem;
  color: #333;
  margin-bottom: 8px;
}

.card-location {
  color: #666;
  font-size: 0.9rem;
}

.card-text {
  font-size: 0.9rem;
  color: #666;
}

.card-text a {
  color: #1976d2; /* 링크 색상 */
  text-decoration: underline;
}

.post-preview-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
  background-color: #eeeeee;
  margin-bottom: 16px;
}
</style>
