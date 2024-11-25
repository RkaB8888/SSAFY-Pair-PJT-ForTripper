<script setup>
import { useRouter } from "vue-router";
import { ref, watch, computed } from "vue";
import { useAuthStore } from "@/stores/auth";
const authStore = useAuthStore();
const { userRegist, checkIdDuplicate, checkNickNameDuplicate } = authStore;

// 상태 관리
const router = useRouter();
const passwordConfirm = ref("");
const emailError = ref("");
const passwordError = ref("");
const nameError = ref("");
const nicknameError = ref("");
const phoneError = ref("");
const isEmailValid = ref(true);
const isNickNameValid = ref(true);
const isEmailChecked = ref(false); // 이메일 중복 확인 상태
const isPasswordChecked = ref(false);
const isNameChecked = ref(false);
const isNickNameChecked = ref(false); // 닉네임 중복 확인 상태
const isPhoneChecked = ref(true);

//회원가입 정보
const registInfo = ref({
  email: "",
  password: "",
  name: "",
  nickName: "",
  phone: "",
});

// 전화번호 포맷팅된 값 (입력 창에 표시될 값)
const formattedPhone = computed({
  get() {
    const digits = registInfo.value.phone.replace(/\D/g, "");
    if (digits.length <= 3) {
      return digits;
    } else if (digits.length <= 7) {
      return `${digits.slice(0, 3)}-${digits.slice(3)}`;
    } else {
      return `${digits.slice(0, 3)}-${digits.slice(3, 7)}-${digits.slice(7)}`;
    }
  },
  set(value) {
    registInfo.value.phone = value.replace(/\D/g, ""); // 숫자만 저장
  },
});

// // 전화번호 입력 시 포맷팅 적용
// const handlePhoneInput = () => {
//   registInfo.value.phone = formatPhoneNumber(registInfo.value.phone);
// };

// 검증 함수
const emailStringCheck = () => {
  return /\S+@\S+\.\S+/.test(registInfo.value.email);
};
const validateEmail = () => {
  emailError.value = emailStringCheck() ? "" : "올바른 이메일 형식이 아닙니다.";
};
const validateName = () => {
  if (registInfo.value.name) {
    isNameChecked.value = true;
    nameError.value = "";
  } else {
    isNameChecked.value = false;
    nameError.value = "이름을 입력해주세요.";
  }
};
const validateNickName = () => {
  const nicknameLength = Array.from(registInfo.value.nickName).length;
  // 검증 로직
  if (!registInfo.value.nickName) {
    nicknameError.value = "닉네임을 입력해주세요.";
    return false;
  } else if (nicknameLength < 3) {
    nicknameError.value = "닉네임은 최소 3자 이상이어야 합니다.";
    return false;
  } else if (nicknameLength > 15) {
    nicknameError.value = "닉네임은 최대 15자 이하이어야 합니다.";
    return false;
  } else {
    nicknameError.value = ""; // 유효한 경우 에러 메시지 없음
    return true;
  }
};
const validatePhone = () => {
  if (/^\d{3}-\d{3,4}-\d{4}$/.test(formattedPhone.value)) {
    phoneError.value = "";
    isPhoneChecked.value = true;
  } else if (registInfo.value.phone) {
    phoneError.value = "올바른 형식이 아닙니다.";
    isPhoneChecked.value = false;
  } else {
    phoneError.value = "";
    isPhoneChecked.value = true;
  }
};
const validatePassword = () => {
  if (!passwordConfirm.value) {
    isPasswordChecked.value = false;
  } else if (registInfo.value.password !== passwordConfirm.value) {
    passwordError.value = "비밀번호가 다릅니다.";
    isPasswordChecked.value = false;
  } else {
    passwordError.value = "";
    isPasswordChecked.value = true;
  }
};

const checkEmail = async () => {
  isEmailValid.value = emailStringCheck();
  if (isEmailValid.value) {
    const response = await checkIdDuplicate(registInfo.value.email);
    if (response.status === 200) {
      isEmailChecked.value = false; // 사용 불가능한 상태로 유지
    } else if (response.status === 404) {
      isEmailChecked.value = true; // 사용 가능한 상태로 변경
      console.log("가능한 아이디");
    } else {
      isEmailChecked.value = false;
    }
    emailError.value = response.msg;
  } else {
    console.log("형식에 안 맞음");
  }
};
const checkNickName = async () => {
  registInfo.value.nickName = registInfo.value.nickName.trim();
  if (validateNickName()) {
    const response = await checkNickNameDuplicate(
      registInfo.value.nickName.trim()
    );
    if (response.status === 200) {
      isNickNameChecked.value = false;
      isNickNameValid.value = false;
    } else if (response.status === 404) {
      isNickNameChecked.value = true;
      isNickNameValid.value = true;
      console.log("가능한 닉네임");
    } else {
      isNickNameChecked.value = false;
      isNickNameValid.value = false;
    }
    nicknameError.value = response.msg;
  } else {
    isNickNameChecked.value = false;
  }
};

// 입력값 변경 감지 (이메일과 닉네임)
watch(
  () => registInfo.value.email,
  () => {
    isEmailChecked.value = false; // 이메일 변경 시 버튼 상태 초기화
  }
);

watch(
  () => registInfo.value.nickName,
  () => {
    isNickNameChecked.value = false; // 닉네임 변경 시 버튼 상태 초기화
  }
);

const registConfirm = async () => {
  if (
    isEmailChecked.value &&
    isPasswordChecked.value &&
    isNameChecked.value &&
    isNickNameChecked.value &&
    isPhoneChecked.value
  ) {
    await userRegist(registInfo.value);
  } else {
    alert("입력 값을 확인해 주세요.");
  }
};

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
                @blur="validateEmail"
              />
              <v-btn
                small
                class="check-btn"
                outlined
                :color="isEmailChecked ? 'success' : 'primary'"
                @click="checkEmail"
                >{{ isEmailChecked ? "사용 가능" : "중복 확인" }}
              </v-btn>
            </div>

            <!-- 비밀번호 -->
            <v-text-field
              label="비밀번호"
              type="password"
              placeholder="비밀번호를 입력해주세요."
              outlined
              dense
              v-model="registInfo.password"
              @blur="validatePassword"
            />
            <v-text-field
              label="비밀번호 확인"
              type="password"
              placeholder="비밀번호를 다시 입력해주세요."
              outlined
              dense
              v-model="passwordConfirm"
              :error-messages="passwordError"
              @blur="validatePassword"
            />

            <!-- 이름 입력 -->
            <v-text-field
              label="이름"
              placeholder="홍길동"
              outlined
              dense
              v-model="registInfo.name"
              :error-messages="nameError"
              @blur="validateName"
            />

            <!-- 닉네임 입력 -->
            <div class="input-group">
              <v-text-field
                label="닉네임"
                placeholder="NickName"
                outlined
                dense
                v-model="registInfo.nickName"
                :error-messages="nicknameError"
              />
              <v-btn
                small
                class="check-btn"
                outlined
                :color="isNickNameChecked ? 'success' : 'primary'"
                @click="checkNickName"
              >
                {{ isNickNameChecked ? "사용 가능" : "중복 확인" }}
              </v-btn>
            </div>

            <!-- 휴대폰 입력 -->
            <v-text-field
              label="휴대폰 번호 (선택)"
              placeholder="01012345678"
              outlined
              dense
              v-model="formattedPhone"
              :error-messages="phoneError"
              @blur="validatePhone"
            />

            <!-- 회원가입 버튼 -->
            <v-btn
              :color="'#FF7043'"
              class="signup-btn"
              block
              large
              @click="registConfirm"
            >
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
  background-color: rgb(98, 0, 234);
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
