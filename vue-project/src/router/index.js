import { createRouter, createWebHistory } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import SearchView from "@/views/SearchView.vue";
import AuthView from "@/views/AuthView.vue";
import { useAuthStore } from "@/stores/auth";
import { storeToRefs } from "pinia";
import PlanHomeView from "@/views/Plan/PlanHomeView.vue";
import PlanDetail from "@/views/Plan/PlanDetail.vue";
import PlanDetailEdit from "@/views/Plan/PlanDetailEdit.vue";
import ShareBoardMain from "@/views/ShareBoard/ShareBoardMain.vue";
import ShareBoardAdd from "@/views/ShareBoard/ShareBoardAdd.vue";
import SharePlanDetail from "@/views/ShareBoard/SharePlanDetail.vue";

// 로그인 상태 체크 및 리다이렉트 경로 저장
const onlyAuthUser = async (to, from, next) => {
  const authStore = useAuthStore();
  const { userInfo, isValidToken } = storeToRefs(authStore);
  const { checkToken } = authStore;

  try {
    await checkToken(); // checkToken은 비동기 함수
    console.log("onlyAuthUser:", userInfo.value);
    // 유효하지 않은 경우 로그인 페이지로 이동
    if (!isValidToken.value || !userInfo.value) {
      console.warn("유효하지 않은 사용자 상태, 로그인 페이지로 이동");
      if (to.name !== "login") {
        // 원래 가려던 경로 저장
        next({ name: "login", query: { redirect: to.fullPath } });
      } else {
        next();
      }
    } else {
      next(); // 정상적으로 페이지로 이동
    }
  } catch (error) {
    console.error("토큰 검사 중 오류 발생:", error);
    if (to.name !== "login") {
      // 원래 가려던 경로 저장
      next({ name: "login", query: { redirect: to.fullPath } });
    } else {
      next();
    }
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
      path: "/user-search",
      name: "UserSerch",
      component: () => import("@/views/UserViews/UserSearchView.vue"),
      beforeEnter: onlyAuthUser,
    },
    {
      path: "/user/:nickname",
      name: "UserPage",
      component: () => import("@/views/UserViews/UserPage.vue"),
      beforeEnter: onlyAuthUser,
    },
    {
      path: "/user/edit",
      name: "EditProfile",
      component: () => import("@/views/UserViews/EditProfileView.vue"),
      beforeEnter: onlyAuthUser,
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
          path: "forgot-password",
          name: "ForgotPassword",
          component: () => import("@/views/UserViews/ForgotPasswordView.vue"),
        },
        {
          path: "reset-password",
          name: "ResetPassword",
          component: () => import("@/views/UserViews/ResetPasswordView.vue"),
        },
      ],
    },
    {
      path: "/search",
      name: "search",
      beforeEnter: onlyAuthUser,
      component: SearchView,
    },
    {
      path: "/auth/reset-password",
      name: "ResetPassword",
      component: () => import("@/views/UserViews/ResetPasswordView.vue"),
    },
    {
      path: "/plans",
      name: "Plan",
      component: PlanHomeView,
      beforeEnter: onlyAuthUser,
    },
    {
      path: "/plans/:plan_id",
      name: "PlanDetail",
      component: PlanDetail,
      props: true,
      beforeEnter: onlyAuthUser,
    },
    {
      path: "/plans/:plan_id/edit",
      name: "PlanDetailEdit",
      component: PlanDetailEdit,
      props: true,
      beforeEnter: onlyAuthUser,
    },
    {
      path: "/planposts",
      name: "ShareBoardMain",
      component: ShareBoardMain,
    },
    {
      path: "/planposts/add",
      name: "ShareBoardAdd",
      component: ShareBoardAdd,
      beforeEnter: onlyAuthUser,
    },
    {
      path: "/planposts/:plan_id",
      name: "SharePostView",
      component: SharePlanDetail,
      props: true,
    },
  ],
});

export default router;
