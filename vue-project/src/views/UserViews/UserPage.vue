<script setup>
import { ref, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import { useAuthStore } from "@/stores/auth";

// 컴포넌트 임포트
import UserInfoSection from "@/views/UserViews/UserInfoSection.vue";
import UserPostsTabs from "@/views/UserViews/UserPostsTabs.vue";
import FriendList from "@/views/UserViews/FriendList.vue";
import { storeToRefs } from "pinia";

const route = useRoute();
const authStore = useAuthStore();

const user = ref({});
const friends = ref([]);
const isOwnProfile = ref(false);

// 현재 로그인한 사용자 정보
const { loginUserInfo, userInfo } = storeToRefs(authStore);

const loadUserData = async (nickname) => {
  if (authStore.loginUserInfo.nickname === nickname) {
    // 자신의 프로필
    user.value = { ...loginUserInfo.value };
    isOwnProfile.value = true;

    console.log("loginUserInfo:", loginUserInfo.value);
    console.log("user:", user.value);

    await fetchFriendList();
  } else {
    // 다른 유저의 프로필
    await authStore.getUserInfoByNickName(nickname);
    user.value = { ...userInfo.value };
    isOwnProfile.value = false;
    console.log("user:", user.value);
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
const fetchFriendList = async () => {
  // API를 통해 친구 목록을 가져옵니다.
  try {
    // friends.value = await fetchFriendsFromAPI();
    // 임시 데이터
    friends.value = [
      // 친구 목록 데이터
    ];
  } catch (error) {
    console.error("친구 목록을 가져오는 중 오류 발생 :", error);
  }
};
</script>

<template>
  <v-container>
    <!-- 유저 정보 섹션 -->
    <UserInfoSection :user="user" :isOwnProfile="isOwnProfile" />

    <!-- 게시글 탭 -->
    <UserPostsTabs v-if="user.email" :user-email="user.email" />

    <!-- 자신의 정보인 경우에만 친구 목록 표시 -->
    <FriendList v-if="isOwnProfile" :friends="friends" />
  </v-container>
</template>

<style scoped>
/* 필요에 따라 스타일 추가 */
</style>
