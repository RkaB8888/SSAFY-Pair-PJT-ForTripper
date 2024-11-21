<script setup>
import { onMounted } from "vue";
import NavigationView from "@/views/NavigationView.vue";
import { useAuthStore } from "@/stores/auth";

onMounted(async () => {
  const authStore = useAuthStore();
  await authStore.initializeAuthState(); // 상태 초기화
});
</script>

<template>
  <v-app>
    <!-- Header -->
    <header>
      <NavigationView />
    </header>

    <!-- Main Content -->
    <main class="main-content">
      <router-view />
    </main>
  </v-app>
</template>

<style scoped>
/* 전체 화면 스타일 */
html,
body {
  margin: 0;
  padding: 0;
  height: 100%; /* HTML과 BODY의 높이 설정 */
  overflow: hidden; /* HTML 스크롤 제거 */
}

/* v-app 스타일 */
v-app {
  display: flex;
  flex-direction: column;
  height: 100%; /* 전체 높이 차지 */
}

/* Header 스타일 */
header {
  height: 68px; /* 네비게이션 바 높이 */
  position: sticky;
  top: 0;
  z-index: 10;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* Main Content 스타일 */
.main-content {
  flex: 1; /* 남은 공간 차지 */
  overflow-y: auto; /* 수직 스크롤 가능 */
  background-color: #f9f9f9; /* 콘텐츠 배경색 */
}
</style>
