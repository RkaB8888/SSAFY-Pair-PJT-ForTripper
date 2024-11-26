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

  // 정규식 패턴 정의
  const anchorTagRegex = /<a[^>]*href=['"]([^'"]+)['"][^>]*>([^<]*)<\/a>/i;
  const urlRegex = /^(https?:\/\/[^\s]+)$/i;

  if (anchorTagRegex.test(tel)) {
    // `<a>` 태그에서 href와 링크 텍스트 추출
    const linkMatch = tel.match(anchorTagRegex);
    const link = linkMatch ? linkMatch[1] : null;
    const linkText = linkMatch ? linkMatch[2] : null;

    return {
      tel: "N/A", // 전화번호를 N/A로 표시
      instagram: link ? link : null, // 인스타그램 링크 추출
      instagramText: linkText ? linkText : link,
    };
  } else if (urlRegex.test(tel)) {
    // `tel`이 URL인 경우
    return {
      tel: "N/A",
      instagram: tel,
      instagramText: tel,
    };
  } else {
    // 일반 텍스트인 경우 (전화번호)
    return {
      tel: tel,
      instagram: null,
      instagramText: null,
    };
  }
});

// 네이버 검색 URL 생성
const naverSearchUrl = computed(() => {
  const baseUrl = "https://search.naver.com/search.naver";
  const params = new URLSearchParams({
    where: "nexearch",
    sm: "top_hty",
    fbm: "0",
    ie: "utf8",
    query: props.festival.title,
  });
  return `${baseUrl}?${params.toString()}`;
});
</script>

<template>
  <v-card class="festival-card">
    <!-- 축제 이미지 -->
    <v-img
      :src="festivalImage"
      class="post-preview-image"
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
      <p v-if="processedContactInfo.tel !== 'N/A'">
        <strong>전화번호:</strong> {{ processedContactInfo.tel }}
      </p>
      <p v-else-if="processedContactInfo.instagram">
        <strong>인스타그램 DM:</strong>
        <a
          :href="processedContactInfo.instagram"
          target="_blank"
          rel="noopener noreferrer"
        >
          인스타그램으로 이동
        </a>
      </p>
    </v-card-text>

    <!-- 네이버 검색 버튼 -->
    <v-card-actions>
      <v-btn
        color="green"
        :href="naverSearchUrl"
        target="_blank"
        rel="noopener noreferrer"
      >
        네이버에서 검색
      </v-btn>
    </v-card-actions>
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
