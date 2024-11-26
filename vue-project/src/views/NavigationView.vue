<script setup>
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { computed } from "vue";
import { storeToRefs } from "pinia";

const { VITE_TRIP_API_URL } = import.meta.env;

const router = useRouter();
const authStore = useAuthStore();
const { loginUserInfo } = storeToRefs(authStore);

// 프로필 이미지 URL 설정
const profileImageUrl = computed(() => {
  const imageUrl = loginUserInfo.value.profileImage
    ? `${VITE_TRIP_API_URL}${loginUserInfo.value.profileImage}`
    : authStore.defaultProfileImage;
  return imageUrl;
});

// 로그인 상태 확인
const isLogin = computed(() => authStore.isLogin);

// 로그아웃 함수
const isLogout = () => {
  authStore.userLogout();
  navigateTo("/");
};

// 라우팅 함수
const navigateTo = (path) => {
  router.push(path);
};

const onImageError = (event) => {
  event.target.src = authStore.defaultProfileImage;
};
</script>
<template>
  <!-- Navigation Bar -->
  <v-app-bar dense>
    <v-container fluid class="py-0">
      <v-row align="center" justify="space-between" no-gutters>
        <!-- Left Side: 탐색 메뉴 -->
        <v-col cols="4">
          <div class="left-section d-flex align-center">
            <v-btn text class="nav-btn text-h6" @click="navigateTo('/search')">
              축제
            </v-btn>
            <v-btn
              text
              class="nav-btn text-h6"
              @click="navigateTo('/planposts')"
            >
              추천 플랜
            </v-btn>
          </div>
        </v-col>

        <!-- Center: 로고 -->
        <v-col cols="4" class="center-section">
          <v-btn class="logo-btn" @click="navigateTo('/')">
            <img src="@/assets/img/LOGO.png" class="nav-logo" alt="Logo" />
          </v-btn>
        </v-col>

        <!-- Right Side: 로그인/회원가입 또는 다른 메뉴 -->
        <v-col cols="4">
          <div class="right-section d-flex align-center justify-end">
            <template v-if="!isLogin">
              <!-- 비로그인 상태 -->
              <v-btn
                outlined
                class="nav-btn text-h6"
                @click="navigateTo('/auth/login')"
              >
                로그인
              </v-btn>
              <v-btn
                color="black"
                dark
                class="nav-btn text-h6"
                @click="navigateTo('/auth/regist')"
              >
                회원가입
              </v-btn>
            </template>
            <template v-else>
              <!-- 로그인 상태 -->
              <v-btn text class="nav-btn text-h6" @click="navigateTo('/plans')">
                계획
              </v-btn>
              <v-btn
                text
                class="nav-btn text-h6"
                @click="navigateTo('/user-search')"
              >
                유저
              </v-btn>
              <!-- 로그아웃 -->
              <v-btn text class="nav-btn text-h6" @click="isLogout">
                로그아웃
              </v-btn>
              <!-- 프로필 -->
              <v-avatar
                size="36"
                @click="navigateTo(`/user/${loginUserInfo.nickname}`)"
                class="cursor-pointer"
              >
                <img
                  :src="profileImageUrl"
                  alt="Profile"
                  class="avatar-image"
                  @error="onImageError"
                />
              </v-avatar>
            </template>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </v-app-bar>
</template>
<style scoped>
/* 로고 이미지 스타일 */
.nav-logo {
  height: 50px;
}

.logo-btn {
  height: 100%;
}

/* 좌우 섹션 스타일 */
.left-section,
.right-section {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 중앙 섹션 스타일 */
.center-section {
  display: flex;
  justify-content: center;
  height: 100%;
}

.nav-btn {
  color: rgb(98, 0, 234);
}

.cursor-pointer {
  cursor: pointer;
}

.avatar-image {
  background-color: rgb(98, 0, 234);
}
</style>
