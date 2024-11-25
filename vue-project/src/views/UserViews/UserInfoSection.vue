<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";

const { VITE_TRIP_API_URL } = import.meta.env;

const props = defineProps({
  user: {
    type: Object,
    required: true,
  },
  isOwnProfile: {
    type: Boolean,
    required: true,
  },
});

const router = useRouter();
const authStore = useAuthStore(); // Pinia 스토어 인스턴스 가져오기

const onImageError = (event) => {
  // console.log("이미지 에러 발생");
  event.target.src = authStore.defaultProfileImage;
};
// 가입일자 포맷팅
const formattedJoinDate = computed(() => {
  return new Date(props.user.joinDate).toLocaleDateString();
});

// 프로필 이미지 URL 설정
const profileImageUrl = computed(() => {
  const imageUrl = props.user.profileImage
    ? `${VITE_TRIP_API_URL}${props.user.profileImage}`
    : authStore.defaultProfileImage;
  // console.log(imageUrl);
  return imageUrl;
});

// 친구 여부 (임시로 false로 설정)
const isFriend = ref(false);

const editProfile = () => {
  router.push("/user/edit");
};

const toggleFriend = () => {
  if (isFriend.value) {
    // 친구 해제 로직
    isFriend.value = false;
  } else {
    // 친구 추가 로직
    isFriend.value = true;
  }
};

const reportUser = () => {
  // 유저 신고 로직
  alert("신고가 접수되었습니다.");
};
</script>
<template>
  <v-row class="user-info-section">
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
      <!-- 가입일자 (주석 처리) -->
      <p>가입일: {{ formattedJoinDate }}</p>
      <!-- 자신의 정보인 경우 -->
      <div v-if="isOwnProfile">
        <v-btn color="primary" @click="editProfile">정보 수정</v-btn>
      </div>
      <!-- 타인의 정보인 경우 -->
      <div v-else-if="user.nickname">
        <v-btn color="primary" @click="toggleFriend">
          {{ isFriend ? "친구 해제" : "친구 추가" }}
        </v-btn>
        <v-btn color="error" @click="reportUser">유저 신고</v-btn>
      </div>
      <div v-else></div>
    </v-col>
  </v-row>
</template>

<style scoped>
.user-info-section {
  margin-top: 20px;
  margin-bottom: 20px;
}
</style>
