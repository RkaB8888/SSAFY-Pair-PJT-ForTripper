<script setup>
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth";
const authStore = useAuthStore();
const { checkIdDuplicate } = authStore;
// 상태 관리
const router = useRouter();
const passwordConfirm = ref("");
const emailError = ref("");
const passwordError = ref("");
const nicknameError = ref("");
const phoneError = ref("");

//회원가입 정보
const registInfo = ref({
  email: "",
  password: "",
  name: "",
  nickname: "",
  phone: "",
});

// 검증 함수
const validate = () => {
  emailError.value = validateEmail(registInfo.value.email)
    ? ""
    : "올바른 이메일 형식이 아닙니다.";
  passwordError.value =
    registInfo.value.password !== passwordConfirm.value
      ? "비밀번호가 다릅니다."
      : "";
  nicknameError.value =
    registInfo.value.nickname === "NickName" ? "이미 존재하는 닉네임" : "";
  phoneError.value = validatePhone(registInfo.value.phone)
    ? ""
    : registInfo.value.phone
    ? "올바른 형식이 아닙니다."
    : "";
};

const validateEmail = (value) => /\S+@\S+\.\S+/.test(value);
const validatePhone = (value) => /^010\d{8}$/.test(value);

const checkId = async () => {
  const isEmailValid = validateEmail();
  if (isEmailValid && (await checkIdDuplicate(registInfo.value.email))) {
    console.log("가능");
  } else {
    console.log("불가능");
  }
};
// const login = async () => {
//   const isEmailValid = validateEmail();
//   const isPasswordValid = validatePassword();
//   if (isEmailValid && isPasswordValid) {
//     await userLogin(loginUser.value);
//     if (isLogin.value) {
//       const token = sessionStorage.getItem("accessToken");
//       await getUserInfo(token);
//       router.replace("/");
//     }
//   } else {
//     console.error("로그인 실패: 입력 값 확인 필요");
//   }
// };

// 로그인 페이지로 이동
const navigateToLogin = () => {
  router.push("/auth/login");
};
</script>

<template>
  <v-container class="signup-container fill-height" fluid>
    <v-row justify="center" align="center">
      <!-- 회원가입 카드 -->
      <v-col cols="10" md="6" class="signup-card">
        <v-card outlined>
          <v-card-text>
            <h3>회원가입</h3>

            <!-- 이메일 입력 -->
            <div class="input-group">
              <v-text-field
                label="이메일"
                placeholder="abc1234@naver.com"
                outlined
                dense
                v-model="registInfo.email"
                :error-messages="emailError"
                @blur="validate"
              />
              <v-btn small class="check-btn" outlined @click="checkId"
                >중복 확인</v-btn
              >
            </div>

            <!-- 비밀번호 -->
            <v-text-field
              label="비밀번호"
              type="password"
              placeholder="비밀번호를 입력해주세요."
              outlined
              dense
              v-model="registInfo.password"
            />
            <v-text-field
              label="비밀번호 확인"
              type="password"
              placeholder="비밀번호를 다시 입력해주세요."
              outlined
              dense
              v-model="passwordConfirm"
              :error-messages="passwordError"
              @blur="validate"
            />

            <!-- 이름 입력 -->
            <v-text-field
              label="이름"
              placeholder="홍길동"
              outlined
              dense
              v-model="registInfo.name"
            />

            <!-- 닉네임 입력 -->
            <div class="input-group">
              <v-text-field
                label="닉네임"
                placeholder="NickName"
                outlined
                dense
                v-model="registInfo.nickname"
                :error-messages="nicknameError"
                @blur="validate"
              />
              <v-btn small class="check-btn" outlined>중복 확인</v-btn>
            </div>

            <!-- 휴대폰 입력 -->
            <v-text-field
              label="휴대폰 번호 (선택)"
              placeholder="010-1234-5678"
              outlined
              dense
              v-model="registInfo.phone"
              :error-messages="phoneError"
              @blur="validate"
            />

            <!-- 회원가입 버튼 -->
            <v-btn :color="'#FF7043'" class="signup-btn" block large>
              회원가입
            </v-btn>
          </v-card-text>

          <!-- 로그인 이동 버튼 -->
          <v-card-actions>
            <v-btn
              color="primary"
              outlined
              class="navigate-btn"
              block
              @click="navigateToLogin"
            >
              로그인 하러 가기
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.signup-container {
  background-color: #665e5e;
  height: 100vh;
}

/* 회원가입 카드 */
.signup-card {
  background-color: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  max-width: 480px;
}

/* 입력 그룹 - 버튼과 필드 정렬 */
.input-group {
  display: flex;
  align-items: center;
  gap: 8px; /* 입력 칸과 버튼 간격 */
  margin-bottom: 16px; /* 필드 간 간격 */
}

/* 중복 확인 버튼 */
.check-btn {
  font-size: 14px;
  padding: 8px 12px;
  white-space: nowrap; /* 버튼 텍스트 한 줄로 유지 */
}

/* 회원가입 버튼 */
.signup-btn {
  font-size: 16px;
  font-weight: bold;
  color: white; /* 텍스트 색상 */
  padding: 12px 24px;
  border-radius: 8px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); /* 버튼 그림자 */
}

/* 로그인 이동 버튼 */
.navigate-btn {
  font-size: 16px;
  font-weight: bold;
  padding: 12px 24px;
  border-radius: 8px;
}
</style>
