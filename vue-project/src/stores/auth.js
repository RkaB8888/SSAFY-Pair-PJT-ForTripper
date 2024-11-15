import { ref, computed } from "vue";
import { defineStore } from "pinia";
import authApi from "@/api/authApi";

export const userStore = defineStore(
  "auth",
  () => {
    const token = ref(null);
    const user = ref(null);
    const login = authApi.post;
    // const regist;
    // const logout;

    return { token, user, login, regist, logout };
  },
  {
    persist: true,
  }
);
