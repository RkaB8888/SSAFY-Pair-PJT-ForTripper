<script setup>
import { useRouter } from "vue-router";
import { ref } from "vue";

// Vue Router 사용
const router = useRouter();

// 로그인 상태 (true: 로그인됨, false: 비로그인)
const isLoggedIn = ref(true);

// 라우팅 함수
const navigateTo = (path) => {
  router.push(path);
};
</script>

<template>
  <!-- Navigation Bar -->
  <v-app-bar color="grey lighten-4" dense>
    <v-container class="d-flex align-center justify-space-between" fluid>
      <!-- Left Side: 로고, 탐색, 목록 -->
      <div class="left-section d-flex align-center">
        <v-btn class="logo-btn" @click="navigateTo('/')">
          <img src="@/assets/img/temp_logo.png" class="nav-logo" alt="Logo" />
        </v-btn>
        <v-btn text class="nav-btn" @click="navigateTo('/search')">탐색</v-btn>
        <v-btn text class="nav-btn" @click="navigateTo('/board')">목록</v-btn>
      </div>

      <!-- Right Side: 로그인/회원가입 또는 다른 메뉴 -->
      <div class="right-section d-flex align-center">
        <template v-if="!isLoggedIn">
          <!-- 비로그인 상태 -->
          <v-btn outlined class="nav-btn" @click="navigateTo('/auth/login')">
            로그인
          </v-btn>
          <v-btn
            color="black"
            dark
            class="nav-btn"
            @click="navigateTo('/auth/regist')"
          >
            회원가입
          </v-btn>
        </template>
        <template v-else>
          <!-- 로그인 상태 -->
          <v-btn text class="nav-btn" @click="navigateTo('/plan')">계획</v-btn>
          <v-btn text class="nav-btn" @click="navigateTo('/friends')">
            친구
          </v-btn>
          <v-btn text class="nav-btn" @click="navigateTo('/photos')">
            사진
          </v-btn>
          <v-btn text class="nav-btn" @click="navigateTo('/notifications')">
            알람
          </v-btn>
          <v-btn text class="nav-btn" @click="navigateTo('/logout')">
            로그아웃
          </v-btn>
          <v-avatar size="36">
            <img src="@/assets/img/temp_profile.jpg" alt="Profile" />
          </v-avatar>
        </template>
      </div>
    </v-container>
  </v-app-bar>
</template>

<style scoped>
/* 로고 스타일 */
.nav-logo {
  height: 36px;
  margin-right: 8px;
}

/* 로고 버튼 스타일 */
.logo-btn {
  padding: 0;
}

/* 버튼 스타일 */
.nav-btn {
  height: 36px;
  min-width: 36px;
  margin-right: 8px;
}

/* 좌우 정렬 */
.left-section {
  gap: 8px; /* 버튼 간 간격 */
}

.right-section {
  gap: 8px; /* 버튼 간 간격 */
}
</style>
