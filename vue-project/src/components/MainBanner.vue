<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";

const images = [
  "/img/MainBanner1.png",
  "/img/MainBanner2.png",
  "/img/MainBanner3.png",
];

const currentImageIndex = ref(0);
let intervalId = null;

const startCarousel = () => {
  intervalId = setInterval(() => {
    currentImageIndex.value = (currentImageIndex.value + 1) % images.length;
  }, 4000); // 3초마다 이미지 변경
};

onMounted(() => {
  startCarousel();
});

onBeforeUnmount(() => {
  clearInterval(intervalId);
});
</script>

<template>
  <div class="main-banner">
    <img :src="images[currentImageIndex]" alt="메인 배너 이미지" />
  </div>
</template>

<style scoped>
.main-banner {
  width: 100%;
  max-width: 1920px;
  margin: 0 auto; /* 중앙 정렬 */
  overflow: hidden;
  background-color: rgb(98, 0, 234);
}

.main-banner img {
  width: 100%;
  height: auto;
  object-fit: cover;
}
</style>
