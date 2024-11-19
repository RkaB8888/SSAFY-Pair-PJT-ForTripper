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
  const { getUserInfo } = authStore;

  let token = sessionStorage.getItem("accessToken");

  if (userInfo.value != null && token) {
    await getUserInfo(token);
  }
  if (!isValidToken.value || userInfo.value == null) {
    next({ name: "login" });
  } else {
    next();
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
      component: SearchView,
    },
  ],
});

export default router;
