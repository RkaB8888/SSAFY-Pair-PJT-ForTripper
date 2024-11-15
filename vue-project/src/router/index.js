import { createRouter, createWebHistory } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import BoardView from "@/views/BoardView.vue";
import SearchView from "@/views/SearchView.vue";
import AuthView from "@/views/AuthView.vue";
import LoginView from "@/views/AuthViews/LoginView.vue";
import RegistView from "@/views/AuthViews/RegistView.vue";

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
          component: LoginView,
        },
        {
          path: "regist",
          name: "regist",
          component: RegistView,
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
