<script setup>
import NavigationView from "@/views/NavigationView.vue";
</script>

<template>
  <v-app class="all">
    <!-- Header -->
    <header>
      <NavigationView />
    </header>

    <!-- Main Content -->
    <main>
      <router-view />
    </main>
  </v-app>
</template>

<style scoped>
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
.all {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 전체 화면 높이 */
}
main {
  flex: 1; /* 남은 공간 차지 */
  overflow: auto; /* 콘텐츠 넘칠 경우 스크롤 활성화 */
}
</style>
