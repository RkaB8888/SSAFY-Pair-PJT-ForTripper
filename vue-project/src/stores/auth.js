import { ref } from "vue";
import { defineStore } from "pinia";
// import authApi from "@/api/authApi";

export const useAuthStore = defineStore(
  "auth",
  () => {
    const token = ref(null); // JWT 토큰 (관련 없음)
    const user = ref(null); // 사용자 정보 (관련 없음)
    const isLoggedIn = ref(false); // 로그인 여부 상태 관리

    const login = () => {
      // 로그인 로직 (예제)
      isLoggedIn.value = true; // 로그인 상태를 true로 변경
    };

    const logout = () => {
      // 로그아웃 로직 (예제)
      isLoggedIn.value = false; // 로그인 상태를 false로 변경
    };

    const regist = ref(null); // 추가 로직 필요 시 구현

    return { token, user, login, regist, logout, isLoggedIn };
  },
  {
    persist: true, // 상태를 로컬 스토리지에 저장
  }
);
