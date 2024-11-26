<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { searchUsersByNickname } from "@/api/authApi";
import { useAuthStore } from "@/stores/auth";

const { VITE_TRIP_API_URL } = import.meta.env; // 추가

const nickname = ref("");
const users = ref([]);
const errorMessage = ref("");
const loading = ref(false);
const router = useRouter();

const authStore = useAuthStore();
const defaultProfileImage = authStore.defaultProfileImage;

const searchUsers = async () => {
  if (!nickname.value.trim()) {
    errorMessage.value = "닉네임을 입력해주세요.";
    return;
  }

  loading.value = true;
  errorMessage.value = "";
  users.value = [];

  try {
    await searchUsersByNickname(
      nickname.value,
      (response) => {
        users.value = response.data;
        if (users.value.length === 0) {
          errorMessage.value = "검색 결과가 없습니다.";
        }
      },
      (error) => {
        console.error("유저 검색 오류:", error);
        errorMessage.value = "유저 검색 중 오류가 발생했습니다.";
      }
    );
  } catch (error) {
    console.error("유저 검색 오류:", error);
    errorMessage.value = "유저 검색 중 오류가 발생했습니다.";
  } finally {
    loading.value = false;
  }
};

const goToUserPage = (nickname) => {
  router.push(`/user/${nickname}`);
};

const onImageError = (event) => {
  event.target.src = defaultProfileImage;
};

// 프로필 이미지 URL 생성 함수
const getUserProfileImageUrl = (user) => {
  console.log(user);
  return user.profileImage
    ? `${VITE_TRIP_API_URL}${user.profileImage}`
    : defaultProfileImage;
};
</script>

<template>
  <v-container fluid class="user-search-container">
    <h1 class="page-title mb-4">유저 검색</h1>

    <v-row justify="center">
      <v-col cols="12" sm="8" md="6">
        <v-text-field
          v-model="nickname"
          label="닉네임 검색"
          @keyup.enter="searchUsers"
          clearable
          dense
          class="nickname-search"
          variant="outlined"
        ></v-text-field>
      </v-col>
      <v-col cols="12" sm="2" md="2">
        <v-btn
          color="primary"
          @click="searchUsers"
          :disabled="loading"
          block
          height="56px"
        >
          검색
        </v-btn>
      </v-col>
    </v-row>

    <!-- 에러 메시지 표시 -->
    <v-row v-if="errorMessage" justify="center">
      <v-col cols="12" sm="8" md="6">
        <v-alert type="error" variant="outlined">
          {{ errorMessage }}
        </v-alert>
      </v-col>
    </v-row>

    <!-- 로딩 인디케이터 -->
    <v-row v-if="loading" justify="center">
      <v-col cols="12" class="text-center">
        <v-progress-circular
          indeterminate
          :color="'primary'"
          size="50"
        ></v-progress-circular>
      </v-col>
    </v-row>

    <!-- 유저 목록 표시 (카드 형태) -->
    <v-row v-else-if="users.length > 0" justify="center">
      <v-col cols="12" sm="10" md="8">
        <v-card
          v-for="user in users"
          :key="user.nickName"
          class="user-card mb-3"
          @click="goToUserPage(user.nickName)"
          hover
        >
          <v-card-content class="d-flex align-center pa-3">
            <v-avatar size="64" class="mr-4">
              <img
                :src="getUserProfileImageUrl(user)"
                alt="Profile Image"
                @error="onImageError"
                class="avatar-image"
              />
            </v-avatar>
            <div>
              <v-card-title class="pa-0 user-nickname">
                {{ user.nickName }}
              </v-card-title>
            </div>
          </v-card-content>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.user-search-container {
  max-width: 800px;
  margin: 0 auto;
}

.page-title {
  color: rgb(98, 0, 234);
  text-align: center;
}

.nickname-search {
  width: 100%;
}

.user-card {
  transition: all 0.3s ease;
  border-radius: 12px;
  background-color: rgba(98, 0, 234, 0.05);
}

.user-card:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 6px rgba(98, 0, 234, 0.2);
}

.user-nickname {
  font-weight: bold;
  color: rgb(98, 0, 234);
}

.avatar-image {
  object-fit: cover;
  width: 100%;
  height: 100%;
}
</style>
