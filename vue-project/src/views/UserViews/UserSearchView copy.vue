<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { searchUsersByNickname } from "@/api/authApi";
import { useAuthStore } from "@/stores/auth";

const nickname = ref("");
const users = ref([]);
const errorMessage = ref("");
const loading = ref(false);
const router = useRouter();

const authStore = useAuthStore();
const defaultProfileImage = authStore.defaultProfileImage;

// 서버의 이미지 URL과 합치기 위해 API URL 가져오기
const { VITE_TRIP_API_URL } = import.meta.env;

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
</script>

<template>
  <v-container>
    <h1 class="text-center mb-6">유저 검색</h1>

    <!-- 검색 바 -->
    <v-row align="center" justify="center" class="mb-4">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-text-field
          v-model="nickname"
          label="닉네임 검색"
          @keyup.enter="searchUsers"
          clearable
          dense
          outlined
          hide-details
        ></v-text-field>
      </v-col>
      <v-col cols="12" sm="4" md="2" lg="2">
        <v-btn
          color="primary"
          @click="searchUsers"
          :disabled="loading"
          class="search-button"
          block
        >
          검색
        </v-btn>
      </v-col>
    </v-row>

    <!-- 에러 메시지 표시 -->
    <v-row v-if="errorMessage">
      <v-col cols="12">
        <v-alert type="error" class="mb-4" dense>
          {{ errorMessage }}
        </v-alert>
      </v-col>
    </v-row>

    <!-- 로딩 인디케이터 -->
    <v-row v-if="loading">
      <v-col cols="12" class="text-center">
        <v-progress-circular
          indeterminate
          color="primary"
        ></v-progress-circular>
      </v-col>
    </v-row>

    <!-- 유저 목록 표시 -->
    <v-row v-else>
      <v-col
        v-for="user in users"
        :key="user.nickName"
        cols="12"
        sm="6"
        md="4"
        lg="3"
      >
        <v-card
          @click="goToUserPage(user.nickName)"
          class="user-card"
          hover
          outlined
        >
          <v-card-text class="text-center">
            <v-avatar size="80" class="mx-auto">
              <img
                :src="
                  user.profileImage
                    ? VITE_TRIP_API_URL + user.profileImage
                    : defaultProfileImage
                "
                alt="Profile Image"
                @error="onImageError"
                class="avatar-image"
              />
            </v-avatar>
            <div class="mt-3">
              <div class="text-h6" style="color: rgb(98, 0, 234)">
                {{ user.nickName }}
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
/* 검색 버튼 스타일 조정 */
.search-button {
  height: 56px; /* v-text-field의 기본 높이와 일치 */
}

/* 유저 카드 스타일 */
.user-card {
  cursor: pointer;
}

/* 프로필 이미지 스타일 */
.avatar-image {
  object-fit: cover;
}
</style>
