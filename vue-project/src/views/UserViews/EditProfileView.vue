<template>
  <v-container class="edit-profile-container">
    <h1 class="text-h3 mb-6 text-center font-weight-bold">정보 수정</h1>

    <!-- 프로필 사진 변경 -->
    <v-card class="mb-6 profile-card elevation-1">
      <v-card-title class="profile-card-title">
        <v-icon left class="mr-2">mdi-camera</v-icon>
        프로필 사진 변경
      </v-card-title>
      <v-divider></v-divider>
      <v-card-text class="text-center">
        <v-avatar size="150" class="mb-4 profile-avatar mx-auto">
          <img
            :src="profileImageUrl"
            alt="Profile Image"
            class="avatar-image"
          />
        </v-avatar>
        <v-file-input
          label="프로필 사진 업로드"
          accept="image/*"
          @change="onProfileImageChange"
          :multiple="false"
          prepend-icon="mdi-upload"
          dense
          hide-details
          class="mb-4"
        ></v-file-input>
        <v-btn @click="updateProfileImage" class="update-btn"> 변경하기 </v-btn>
      </v-card-text>
    </v-card>

    <!-- 닉네임 수정 -->
    <v-card class="mb-6 profile-card elevation-1">
      <v-card-title class="profile-card-title">
        <v-icon left class="mr-2">mdi-account</v-icon>
        닉네임 수정
      </v-card-title>
      <v-divider></v-divider>
      <v-card-text>
        <v-text-field
          label="새 닉네임"
          v-model="newNickname"
          :error-messages="nicknameError"
          prepend-icon="mdi-account-edit"
          dense
          hide-details
          class="mb-4"
        ></v-text-field>
        <v-btn @click="updateNickname" class="update-btn"> 수정하기 </v-btn>
      </v-card-text>
    </v-card>

    <!-- 비밀번호 수정 -->
    <v-card class="mb-6 profile-card elevation-1">
      <v-card-title class="profile-card-title">
        <v-icon left class="mr-2">mdi-lock</v-icon>
        비밀번호 수정
      </v-card-title>
      <v-divider></v-divider>
      <v-card-text>
        <v-text-field
          label="현재 비밀번호"
          v-model="currentPassword"
          :type="showCurrentPassword ? 'text' : 'password'"
          @click:append="showCurrentPassword = !showCurrentPassword"
          :append-icon="showCurrentPassword ? 'mdi-eye-off' : 'mdi-eye'"
          prepend-icon="mdi-lock"
          dense
          hide-details
          class="mb-4"
        ></v-text-field>
        <v-text-field
          label="새 비밀번호"
          v-model="newPassword"
          :type="showNewPassword ? 'text' : 'password'"
          @click:append="showNewPassword = !showNewPassword"
          :append-icon="showNewPassword ? 'mdi-eye-off' : 'mdi-eye'"
          prepend-icon="mdi-lock-plus"
          dense
          hide-details
          class="mb-4"
        ></v-text-field>
        <v-text-field
          label="새 비밀번호 확인"
          v-model="confirmNewPassword"
          :type="showConfirmPassword ? 'text' : 'password'"
          @click:append="showConfirmPassword = !showConfirmPassword"
          :append-icon="showConfirmPassword ? 'mdi-eye-off' : 'mdi-eye'"
          prepend-icon="mdi-lock-check"
          dense
          hide-details
          class="mb-4"
        ></v-text-field>
        <v-btn @click="updatePassword" class="update-btn"> 변경하기 </v-btn>
      </v-card-text>
    </v-card>

    <!-- 회원 탈퇴 -->
    <v-card class="mb-6 profile-card elevation-1">
      <v-card-title class="profile-card-title">
        <v-icon left class="mr-2" color="error">mdi-account-remove</v-icon>
        회원 탈퇴
      </v-card-title>
      <v-divider></v-divider>
      <v-card-text>
        <p class="mb-4">
          회원 탈퇴 시 모든 데이터가 삭제되며 복구할 수 없습니다.
        </p>
        <v-btn color="error" @click="deleteAccount" class="delete-btn">
          회원 탈퇴
        </v-btn>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
const { VITE_TRIP_API_URL } = import.meta.env;
const router = useRouter();
const authStore = useAuthStore();

const user = ref(authStore.loginUserInfo);

const profileImageFile = ref(null);
const profileImagePreview = ref(null);

const newNickname = ref(user.value.nickname);
const nicknameError = ref("");

const currentPassword = ref("");
const newPassword = ref("");
const confirmNewPassword = ref("");

const showCurrentPassword = ref(false);
const showNewPassword = ref(false);
const showConfirmPassword = ref(false);

// 프로필 이미지 URL 설정
const profileImageUrl = computed(() => {
  const imageUrl =
    profileImagePreview.value ||
    (user.value.profileImage
      ? `${VITE_TRIP_API_URL}${user.value.profileImage}`
      : authStore.defaultProfileImage);
  return imageUrl;
});

// 프로필 사진 변경
const onProfileImageChange = (event) => {
  const file = event.target.files[0];
  if (!file) {
    profileImagePreview.value = null;
    return;
  }
  profileImageFile.value = file;
  const reader = new FileReader();
  reader.onload = (e) => {
    profileImagePreview.value = e.target.result;
  };
  reader.readAsDataURL(file);
};

const updateProfileImage = async () => {
  if (!profileImageFile.value) {
    alert("프로필 사진을 선택해주세요.");
    return;
  }
  try {
    await authStore.updateProfileImage(profileImageFile.value);
    alert("프로필 사진이 변경되었습니다.");
  } catch (error) {
    console.error(error);
    alert("프로필 사진 변경에 실패하였습니다.");
  }
};

// 닉네임 수정
const updateNickname = async () => {
  if (!newNickname.value.trim()) {
    nicknameError.value = "닉네임을 입력해주세요.";
    return;
  }
  try {
    await authStore.updateNickname(newNickname.value);
    alert("닉네임이 변경되었습니다.");
    user.value.nickname = newNickname.value;
  } catch (error) {
    console.error(error);
    nicknameError.value = "닉네임 변경에 실패하였습니다.";
  }
};

// 비밀번호 수정
const updatePassword = async () => {
  if (
    !currentPassword.value ||
    !newPassword.value ||
    !confirmNewPassword.value
  ) {
    alert("모든 필드를 입력해주세요.");
    return;
  }
  if (newPassword.value !== confirmNewPassword.value) {
    alert("새 비밀번호가 일치하지 않습니다.");
    return;
  }
  try {
    await authStore.updatePassword(currentPassword.value, newPassword.value);
    alert("비밀번호가 변경되었습니다.");
    currentPassword.value = "";
    newPassword.value = "";
    confirmNewPassword.value = "";
  } catch (error) {
    console.error(error);
    alert("비밀번호 변경에 실패하였습니다.");
  }
};

// 회원 탈퇴
const deleteAccount = async () => {
  if (confirm("정말로 회원 탈퇴를 하시겠습니까?")) {
    try {
      await authStore.deleteAccount();
      alert("회원 탈퇴가 완료되었습니다.");
      router.push("/");
    } catch (error) {
      console.error(error);
      alert("회원 탈퇴에 실패하였습니다.");
    }
  }
};
</script>

<style scoped>
.edit-profile-container {
  max-width: 600px;
  margin: 0 auto;
}

.profile-card {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.profile-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.profile-card-title {
  background-color: #f5f5f5;
  color: #333;
  padding: 16px;
  display: flex;
  align-items: center;
}

.profile-avatar {
  border: 2px solid rgb(98, 0, 234); /* 테마 컬러 적용 */
  box-shadow: none;
}

.avatar-image {
  object-fit: cover;
}

.update-btn,
.delete-btn {
  width: 100%;
  height: 44px;
  font-weight: bold;
  letter-spacing: 1px;
  text-transform: uppercase;
}

.update-btn {
  background-color: rgb(98, 0, 234);
  color: white;
}

.delete-btn {
  background-color: #ff5252;
  color: white;
}

.v-text-field {
  margin-bottom: 16px;
}

.v-btn {
  transition: all 0.3s ease;
}

.v-btn:hover {
  opacity: 0.9;
  transform: translateY(-2px);
}
</style>
