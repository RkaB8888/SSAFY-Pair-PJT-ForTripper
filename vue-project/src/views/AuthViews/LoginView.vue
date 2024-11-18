<script setup>
import { useRouter } from "vue-router";
import { ref } from "vue";

// 상태 관리
const router = useRouter();
const email = ref("");
const password = ref("");
const emailError = ref(""); // 이메일 검증 에러 메시지
const rememberMe = ref(false); // 아이디 기억하기 상태
// 이메일 검증 함수
const validateEmail = () => {
  if (!email.value) {
    emailError.value = ""; // 입력값이 비어 있으면 경고 메시지 제거
    return;
  }
  const emailPattern = /\S+@\S+\.\S+/; // 이메일 형식 정규표현식
  emailError.value = emailPattern.test(email.value)
    ? ""
    : "올바른 이메일 형식이 아닙니다.";
};

// 회원가입 페이지로 이동
const mvRegist = () => {
  router.push("/auth/regist");
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
              label="이메일"
              placeholder="abc1234@naver.com"
              outlined
              dense
              v-model="email"
              :error-messages="emailError"
              @blur="validateEmail"
            />
            <!-- 비밀번호 입력 -->
            <v-text-field
              label="비밀번호"
              type="password"
              placeholder="비밀번호를 입력해주세요."
              outlined
              dense
              v-model="password"
            />
            <!-- 아이디 기억하기 -->
            <div class="remember-me">
              <v-checkbox
                v-model="rememberMe"
                label="아이디 기억하기"
                density="compact"
                max-width="150"
              ></v-checkbox>
            </div>
            <!-- 로그인 버튼 -->
            <v-btn color="#1E88E5" class="mt-4 login-btn" block large>
              로그인
            </v-btn>
          </v-card-text>

          <!-- 회원가입 버튼 -->
          <v-card-actions class="signup-section">
            <span>아직 계정이 없으신가요?</span>
            <v-btn
              color="primary"
              outlined
              class="signup-btn"
              @click="mvRegist"
            >
              회원가입 하러 가기
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
  background-color: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); /* 카드 그림자 */
  max-width: 480px; /* 최대 너비 제한 */
  width: 100%; /* 화면 크기와 조화를 유지 */
}

/* 회원가입 섹션 */
.signup-section {
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
}
</style>
