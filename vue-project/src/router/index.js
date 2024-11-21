import { createRouter, createWebHistory } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import BoardView from "@/views/BoardView.vue";
import SearchView from "@/views/SearchView.vue";
import AuthView from "@/views/AuthView.vue";
import { useAuthStore } from "@/stores/auth";
import { storeToRefs } from "pinia";

//로그인 상태 체크
const onlyAuthUser = async (to, from, next) => {
  const authStore = useAuthStore();
  const { userInfo, isValidToken } = storeToRefs(authStore);
  const { checkToken } = authStore;
  let token = sessionStorage.getItem("accessToken");

  try {
    // 토큰이 있는 경우에만 유효성 검사
    if (token) {
      await checkToken(token); // checkToken은 비동기 함수
    }
    console.log("onlyAuthUser:", userInfo.value);
    // 유효하지 않은 경우 로그인 페이지로 이동
    if (!isValidToken.value || !userInfo.value) {
      console.warn("유효하지 않은 사용자 상태, 로그인 페이지로 이동");
      next({ name: "login" });
    } else {
      next(); // 정상적으로 페이지로 이동
    }
  } catch (error) {
    console.error("토큰 검사 중 오류 발생:", error);
    next({ name: "login" });
  }
};

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/board",
      name: "board",
      component: BoardView,
    },
    {
      path: "/auth",
      name: "auth",
      component: AuthView,
      redirect: "auth/login",
      children: [
        {
          path: "login",
          name: "login",
          component: () => import("@/views/AuthViews/LoginView.vue"),
        },
        {
          path: "regist",
          name: "regist",
          component: () => import("@/views/AuthViews/RegistView.vue"),
        },
        {
          path: "mypage",
          name: "mypage",
          beforeEnter: onlyAuthUser,
          component: () => import("@/views/UserViews/UserMyPage.vue"),
        },
      ],
    },
    {
      path: "/search",
      name: "search",
      beforeEnter: onlyAuthUser,
      component: SearchView,
    },
  ],
});

export default router;
