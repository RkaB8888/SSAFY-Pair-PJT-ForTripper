<script setup>
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth"; // Pinia 스토어 가져오기

const router = useRouter();
const authStore = useAuthStore(); // Pinia 스토어 인스턴스

// 로그인 함수
const login = () => {
  authStore.login(); // Pinia의 login 메서드 호출
  navigateTo("/"); // 로그인 후 홈 화면으로 이동
};

// 라우팅 함수
const navigateTo = (path) => {
  router.push(path);
};
</script>

<template>
  <div>
    <h2 class="check">로그인 화면 입니다.</h2>
    <v-btn class="check" @click="login">로그인</v-btn>
  </div>
</template>

<style scoped></style>
