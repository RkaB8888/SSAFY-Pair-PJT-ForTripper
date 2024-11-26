<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth";

const authStore = useAuthStore();

const email = ref("");
const emailError = ref("");
const message = ref("");
const errorMessage = ref("");

const validateEmail = () => {
  if (!email.value.trim()) {
    emailError.value = "이메일을 입력해주세요.";
    return false;
  }
  const emailPattern = /\S+@\S+\.\S+/;
  if (!emailPattern.test(email.value)) {
    emailError.value = "올바른 이메일 형식이 아닙니다.";
    return false;
  } else {
    emailError.value = "";
    return true;
  }
};

const requestPasswordReset = async () => {
  if (validateEmail()) {
    try {
      await authStore.requestPasswordReset(email.value);
      message.value = "비밀번호 재설정 이메일을 전송했습니다.";
      errorMessage.value = "";
    } catch (error) {
      errorMessage.value =
        error.response.data.message || "오류가 발생했습니다.";
      message.value = "";
    }
  }
};
</script>

<template>
  <v-container class="password-reset-container">
    <v-row justify="center" align="center">
      <v-col cols="12" md="6" lg="4">
        <v-card class="password-reset-card elevation-2">
          <v-card-title class="text-h5 font-weight-bold">
            비밀번호 재설정
          </v-card-title>
          <v-card-text>
            <v-text-field
              label="이메일"
              placeholder="example@example.com"
              v-model="email"
              :error-messages="emailError"
              @blur="validateEmail"
              prepend-inner-icon="mdi-email"
              outlined
            />
            <v-btn
              @click="requestPasswordReset"
              color="primary"
              class="mt-4"
              block
              large
            >
              재설정 이메일 보내기
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
.password-reset-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
}

.password-reset-card {
  padding: 24px;
  border-radius: 16px;
}

.v-card-title {
  color: rgb(98, 0, 234);
  justify-content: center;
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
