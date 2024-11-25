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
const showPassword = ref(false);
const showConfirmPassword = ref(false);
const message = ref("");
const errorMessage = ref("");

const token = route.query.token;

const validatePassword = () => {
  if (!password.value.trim()) {
    passwordError.value = "비밀번호를 입력해주세요.";
    return false;
  }
  if (password.value.length < 8) {
    passwordError.value = "비밀번호는 8자 이상이어야 합니다.";
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
              label="새 비밀번호"
              :type="showPassword ? 'text' : 'password'"
              placeholder="새 비밀번호를 입력해주세요."
              outlined
              dense
              v-model="password"
              :error-messages="passwordError"
              @blur="validatePassword"
            >
              <v-btn icon @click="showPassword = !showPassword">
                <v-icon>
                  {{ showPassword ? "mdi-eye-off" : "mdi-eye" }}
                </v-icon>
              </v-btn>
            </v-text-field>
            <v-text-field
              label="비밀번호 확인"
              :type="showConfirmPassword ? 'text' : 'password'"
              placeholder="비밀번호를 다시 입력해주세요."
              outlined
              dense
              v-model="confirmPassword"
              :error-messages="confirmPasswordError"
              @blur="validateConfirmPassword"
            >
              <v-btn icon @click="showConfirmPassword = !showConfirmPassword">
                <v-icon>
                  {{ showConfirmPassword ? "mdi-eye-off" : "mdi-eye" }}
                </v-icon>
              </v-btn>
            </v-text-field>
            <v-btn
              @click="resetPassword"
              color="#1E88E5"
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
/* 스타일링은 필요에 따라 추가하세요 */
</style>
