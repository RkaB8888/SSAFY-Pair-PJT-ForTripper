<script setup>
import NavigationView from "@/views/NavigationView.vue";
</script>

<template>
  <v-app>
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
main {
  padding-top: 68px; /* 네비게이션 바 높이만큼 공간 확보 */
  padding: 16px;
}
</style>
