<script setup>
import { useRouter, useRoute } from "vue-router";
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/auth";
import { storeToRefs } from "pinia";

const authStore = useAuthStore();
const { isLogin, isLoginError } = storeToRefs(authStore);
const { userLogin, checkToken } = authStore;
// 상태 관리
const router = useRouter();
const route = useRoute();
const emailError = ref("");
const passwordError = ref("");
const showPassword = ref(false); // 비밀번호 표시 토글
// const rememberMe = ref(false); // 아이디 기억하기 상태

// 로그인 페이지가 로드될 때 실행되는 로직
onMounted(() => {
  isLoginError.value = false; // 로그인 에러 상태 초기화
});
const loginUser = ref({
  email: "",
  password: "",
});

// 이메일 검증 함수
const validateEmail = () => {
  if (!loginUser.value.email.trim()) {
    emailError.value = "이메일을 입력해주세요.";
    return false;
  }
  const emailPattern = /\S+@\S+\.\S+/; // 이메일 형식 정규표현식
  if (!emailPattern.test(loginUser.value.email)) {
    emailError.value = "올바른 이메일 형식이 아닙니다.";
    return false;
  } else {
    emailError.value = "";
    return true;
  }
};

// 비밀번호 검증 함수
const validatePassword = () => {
  if (!loginUser.value.password.trim()) {
    passwordError.value = "비밀번호를 입력해주세요.";
    return false;
  }
  passwordError.value = "";
  return true;
};

// 회원가입 페이지로 이동
const mvRegist = () => {
  router.push("/auth/regist");
};

// 비밀번호 찾기 페이지로 이동
const mvFindPassword = () => {
  router.push("/auth/forgot-password");
};

//로그인
const login = async () => {
  console.log("로그인 버튼 클릭");
  const isEmailValid = validateEmail();
  const isPasswordValid = validatePassword();
  if (isEmailValid && isPasswordValid) {
    await userLogin(loginUser.value);
    if (isLogin.value) {
      console.log("로그인 성공 신호 받아서 토큰 체크로 간다.");
      const token = sessionStorage.getItem("accessToken");
      await checkToken(token);
      console.log("토큰 검사 끝 메인페이지 이동");
      const redirect = route.query.redirect || "/"; // 리다이렉트 경로가 없으면 홈으로
      router.push(redirect);
    }
  } else {
    console.error("로그인 실패: 입력 값 확인 필요");
  }
};
</script>

<template>
  <v-container class="main fill-height" fluid>
    <v-row justify="center" align="center">
      <!-- 로그인 카드 -->
      <v-col cols="10" md="5" class="login-card">
        <v-card outlined>
          <v-card-text>
            <h3>로그인</h3>
            <!-- 이메일 입력 -->
            <v-text-field
              class="mt-2"
              label="이메일"
              placeholder="abc1234@naver.com"
              outlined
              dense
              v-model="loginUser.email"
              :error-messages="emailError"
              @blur="validateEmail"
              @input="isLoginError = false"
            />
            <!-- 비밀번호 입력 -->
            <v-text-field
              label="비밀번호"
              :type="showPassword ? 'text' : 'password'"
              placeholder="비밀번호를 입력해주세요."
              outlined
              dense
              v-model="loginUser.password"
              :error-messages="passwordError"
              class="password-field"
              @input="isLoginError = false"
            >
              <v-btn
                icon
                class="password-toggle-btn"
                @click="showPassword = !showPassword"
              >
                <v-icon>
                  {{ showPassword ? "mdi-eye-off" : "mdi-eye" }}
                </v-icon>
              </v-btn>
            </v-text-field>
            <!-- 아이디 기억하기 -->
            <!-- <div class="remember-me">
              <v-checkbox
                v-model="rememberMe"
                label="아이디 기억하기"
                density="compact"
                max-width="150"
              ></v-checkbox>
            </div> -->

            <!-- 로그인 버튼 -->
            <v-btn @click="login" class="mt-2 login-btn" block large>
              로그인
            </v-btn>
            <v-alert v-if="isLoginError" type="error" class="mt-4">
              로그인 실패: 이메일 또는 비밀번호를 확인하세요.
            </v-alert>
          </v-card-text>

          <!-- 회원가입 버튼 -->
          <v-card-actions class="bottom-section">
            <v-btn color="primary" class="" @click="mvFindPassword"
              >비밀번호를 잊으셨나요?
            </v-btn>
            <v-btn color="primary" outlined class="" @click="mvRegist">
              아직 계정이 없으신가요?
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
/* 메인 컨테이너 */
.main {
  background-color: #665e5e;
  height: 100vh; /* 전체 화면 높이 */
}

/* 로그인 카드 */
.login-card {
  background-color: rgb(98, 0, 234);
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); /* 카드 그림자 */
  max-width: 480px; /* 최대 너비 제한 */
  width: 100%; /* 화면 크기와 조화를 유지 */
}

/* 회원가입 섹션 */
.bottom-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-top: 1px solid #ddd; /* 상단 분리선 */
}

/* 회원가입 버튼 */
.signup-btn {
  margin-left: 16px;
}

/* 로그인 버튼 */
.login-btn {
  font-size: 16px;
  font-weight: bold;
  padding: 12px 24px;
  border-radius: 8px;
  color: white; /* 텍스트 색상 */
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); /* 버튼 그림자 */
  background-color: rgb(72, 169, 166);
}
.password-field {
  position: relative; /* 부모 요소를 기준으로 절대 배치 */
}

.password-toggle-btn {
  position: absolute;
  right: 10px;
  top: 50%; /* 수직 중앙 정렬 */
  transform: translateY(-50%);
  z-index: 10; /* 버튼이 텍스트 필드 위에 나타나도록 설정 */
  color: #757575; /* 버튼 색상 (선택 사항) */
}
</style>
