<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { useFriendStore } from "@/stores/friend";
import { localAxios } from "@/util/http-commons"; // 필요한 경우 임포트

const { VITE_TRIP_API_URL } = import.meta.env;

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();
const friendStore = useFriendStore();

const onImageError = (event) => {
  event.target.src = authStore.defaultProfileImage;
};

// 유저 정보를 저장할 변수
const user = ref(null);

// 자신의 프로필인지 확인
const isOwnProfile = computed(() => {
  return user.value && user.value.nickname === authStore.loginUserInfo.nickname;
});

// 가입일자 포맷팅
const formattedJoinDate = computed(() => {
  return user.value ? new Date(user.value.joinDate).toLocaleDateString() : "";
});

// 프로필 이미지 URL 설정
const profileImageUrl = computed(() => {
  const imageUrl =
    user.value && user.value.profileImage
      ? `${VITE_TRIP_API_URL}${user.value.profileImage}`
      : authStore.defaultProfileImage;
  return imageUrl;
});

// 유저 정보 및 친구 상태 가져오기 함수
const fetchUserData = async (nickname) => {
  try {
    // 유저 정보 가져오기
    const response = await localAxios().get(`/users/nickname/${nickname}`);
    user.value = response.data;

    // 친구 상태 확인
    if (!isOwnProfile.value) {
      console.log("친구상태확인", nickname, friendStore.isFriend);
      await friendStore.checkFriendStatus(nickname);
      console.log("친구상태확인", nickname, friendStore.isFriend);
    }
  } catch (error) {
    console.error("유저 정보 가져오기 실패:", error);
  }
};

// 초기 마운트 시 데이터 가져오기
onMounted(() => {
  fetchUserData(route.params.nickname);
});

// 라우트 파라미터 변경 감지하여 데이터 다시 가져오기
watch(
  () => route.params.nickname,
  (newNickname, oldNickname) => {
    if (newNickname !== oldNickname) {
      fetchUserData(newNickname);
    }
  }
);

const isFriend = computed(() => friendStore.isFriend);

// 프로필 수정
const editProfile = () => {
  router.push("/user/edit");
};

// 친구 추가/해제 토글
const toggleFriend = async () => {
  if (isFriend.value) {
    // 친구 해제 로직
    await friendStore.removeFriend(user.value.nickname);
    alert("친구가 해제되었습니다.");
  } else {
    // 친구 추가 로직
    await friendStore.addFriend(user.value.nickname);
    alert("친구가 추가되었습니다.");
  }
};

// 유저 신고
const reportUser = () => {
  // 유저 신고 로직
  alert("신고가 접수되었습니다.");
};
</script>

<template>
  <v-row class="user-info-section" v-if="user">
    <v-col cols="12" md="4" class="text-center">
      <!-- 프로필 사진 -->
      <v-avatar size="128">
        <img
          :src="profileImageUrl"
          alt="Profile Image"
          class="avatar-image"
          @error="onImageError"
        />
      </v-avatar>
    </v-col>
    <v-col cols="12" md="8">
      <!-- 닉네임 -->
      <h2>{{ user.nickname || "존재하지 않는 유저" }}</h2>
      <!-- 가입일자 -->
      <p>가입일: {{ formattedJoinDate }}</p>
      <!-- 자신의 정보인 경우 -->
      <div v-if="isOwnProfile">
        <v-btn color="primary" @click="editProfile">정보 수정</v-btn>
      </div>
      <!-- 타인의 정보인 경우 -->
      <div v-else>
        <v-btn color="primary" @click="toggleFriend">
          {{ isFriend ? "친구 해제" : "친구 추가" }}
        </v-btn>
        <v-btn color="error" @click="reportUser">유저 신고</v-btn>
      </div>
    </v-col>
  </v-row>
  <div v-else>
    <p>유저 정보를 불러오는 중입니다... 존재하지 않는 유저일수도...</p>
  </div>
</template>

<style scoped>
.user-info-section {
  margin-top: 20px;
  margin-bottom: 20px;
}
</style>
