<script setup>
import { ref, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import { useAuthStore } from "@/stores/auth";

// 컴포넌트 임포트
import UserInfoSection from "@/views/UserViews/UserInfoSection.vue";
import UserPostsTabs from "@/views/UserViews/UserPostsTabs.vue";
import FriendList from "@/views/UserViews/FriendList.vue";
import UndefinedView from "./UndefinedView.vue";
import { storeToRefs } from "pinia";

const route = useRoute();
const authStore = useAuthStore();

const user = ref({});
const isOwnProfile = ref(false);

// 현재 로그인한 사용자 정보
const { loginUserInfo, userInfo } = storeToRefs(authStore);

const loadUserData = async (nickname) => {
  if (loginUserInfo.value.nickname === nickname) {
    // 자신의 프로필
    user.value = { ...loginUserInfo.value };
    isOwnProfile.value = true;
  } else {
    // 다른 유저의 프로필
    await authStore.getUserInfoByNickName(nickname);
    user.value = { ...userInfo.value };
    isOwnProfile.value = false;
  }
};

// 초기 데이터 로드
onMounted(() => {
  loadUserData(route.params.nickname);
});

// 라우트 변경 감지
watch(
  () => route.params.nickname,
  (newNickname) => {
    loadUserData(newNickname);
  }
);
</script>

<template>
  <v-container>
    <!-- 유저 정보 섹션 -->
    <UserInfoSection :user="user" :isOwnProfile="isOwnProfile" />

    <!-- 여행 계획 목록 (자신의 프로필인 경우에만 표시) -->
    <div v-if="isOwnProfile">
      <UserPostsTabs
        v-if="user.email"
        :user-email="user.email"
        :nickname="user.nickname"
      />
      <!-- 친구 목록 -->
      <FriendList />
    </div>
    <!-- 비공개 메시지 -->
    <div v-else>
      <UndefinedView />
    </div>
  </v-container>
</template>

<style scoped>
/* 필요한 스타일 추가 */
</style>
