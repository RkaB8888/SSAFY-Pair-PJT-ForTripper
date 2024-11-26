<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const password = ref("");
const confirmPassword = ref("");
const passwordError = ref("");
const confirmPasswordError = ref("");
const message = ref("");
const errorMessage = ref("");

const token = route.query.token;

const validatePassword = () => {
  if (!password.value.trim()) {
    passwordError.value = "비밀번호를 입력해주세요.";
    return false;
  } else {
    passwordError.value = "";
    return true;
  }
};

const validateConfirmPassword = () => {
  if (password.value !== confirmPassword.value) {
    confirmPasswordError.value = "비밀번호가 일치하지 않습니다.";
    return false;
  } else {
    confirmPasswordError.value = "";
    return true;
  }
};

const resetPassword = async () => {
  if (validatePassword() && validateConfirmPassword()) {
    try {
      await authStore.handleResetPassword(token, password.value);
      message.value = "비밀번호가 성공적으로 재설정되었습니다.";
      errorMessage.value = "";
      setTimeout(() => {
        router.push("/auth/login");
      }, 3000); // 3초 후 로그인 페이지로 이동
    } catch (error) {
      errorMessage.value =
        error.response?.data?.message || "오류가 발생했습니다.";
      message.value = "";
    }
  }
};
</script>

<template>
  <v-container class="reset-password-container">
    <v-row justify="center" align="center">
      <v-col cols="12" md="6" lg="4">
        <v-card class="reset-password-card elevation-2">
          <v-card-title class="text-h5 font-weight-bold">
            비밀번호 재설정
          </v-card-title>
          <v-card-text>
            <v-text-field
              label="새 비밀번호"
              type="password"
              placeholder="새 비밀번호를 입력해주세요."
              v-model="password"
              :error-messages="passwordError"
              @blur="validatePassword"
              prepend-inner-icon="mdi-lock"
              outlined
              dense
              hide-details
              class="mb-4"
            ></v-text-field>
            <v-text-field
              label="비밀번호 확인"
              type="password"
              placeholder="비밀번호를 다시 입력해주세요."
              v-model="confirmPassword"
              :error-messages="confirmPasswordError"
              @blur="validateConfirmPassword"
              prepend-inner-icon="mdi-lock-check"
              outlined
              dense
              hide-details
              class="mb-4"
            ></v-text-field>
            <v-btn
              @click="resetPassword"
              color="primary"
              class="mt-4"
              block
              large
            >
              비밀번호 재설정
            </v-btn>
            <v-alert v-if="message" type="success" class="mt-4">
              {{ message }}
            </v-alert>
            <v-alert v-if="errorMessage" type="error" class="mt-4">
              {{ errorMessage }}
            </v-alert>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.reset-password-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
}

.reset-password-card {
  padding: 24px;
  border-radius: 16px;
}

.v-card-title {
  color: rgb(98, 0, 234);
  justify-content: center;
}

.v-text-field {
  margin-bottom: 16px;
}

.v-text-field input {
  font-size: 16px;
}

.v-btn {
  height: 48px;
  font-size: 16px;
}

.v-alert {
  font-size: 14px;
}
</style>
