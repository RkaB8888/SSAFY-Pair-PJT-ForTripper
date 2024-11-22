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
  <v-container class="main fill-height" fluid>
    <v-row justify="center" align="center">
      <v-col cols="10" md="5">
        <v-card outlined>
          <v-card-text>
            <h3>비밀번호 재설정</h3>
            <v-text-field
              label="이메일"
              placeholder="abc1234@naver.com"
              outlined
              dense
              v-model="email"
              :error-messages="emailError"
              @blur="validateEmail"
            />
            <v-btn
              @click="requestPasswordReset"
              color="#1E88E5"
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
/* 스타일링은 필요에 따라 추가하세요 */
</style>
