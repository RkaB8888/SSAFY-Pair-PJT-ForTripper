import axios from "axios";
import router from "@/router";
import { httpStatusCode } from "./http-status";
import { useAuthStore } from "@/stores/auth";

const { VITE_TRIP_API_URL } = import.meta.env;

// 토큰 재발급 실패 여부를 추적하는 플래그 (모듈 스코프 변수로 선언)
let tokenRefreshFailed = false;

// tokenRefreshFailed 플래그를 초기화하는 함수
export function resetTokenRefreshFailed() {
  tokenRefreshFailed = false;
}

// local vue api axios instance
function localAxios() {
  const instance = axios.create({
    baseURL: VITE_TRIP_API_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  // 요청 인터셉터
  instance.interceptors.request.use(
    (config) => {
      const accessToken = sessionStorage.getItem("accessToken");
      if (accessToken) {
        config.headers["Authorization"] = `Bearer ${accessToken}`;
      }

      return config;
    },
    (error) => {
      console.log("인터셉터 요청 에러 :", error);
      return Promise.reject(error);
    }
  );

  let isTokenRefreshing = false;
  let requestQueue = [];

  // 응답 인터셉터
  instance.interceptors.response.use(
    (response) => {
      return response;
    },
    async (error) => {
      const { config, response } = error;

      console.log("인터셉터 응답 에러");
      if (!response) {
        console.error("네트워크 에러");
        return Promise.reject(error);
      }

      const status = response.status;

      //accessToekn이 만료된 경우
      if (status == httpStatusCode.UNAUTHORIZED) {
        const originalRequest = config;

        // **로그인 요청 예외 처리 추가**
        if (originalRequest.url.includes("/users/login")) {
          // 로그인 실패 시 토큰 재발급 시도하지 않음
          return Promise.reject(error);
        }

        // `/users/refresh` 요청 자체가 실패한 경우
        if (originalRequest.url.includes("/users/refresh")) {
          console.error("리프레시 토큰 만료");
          tokenRefreshFailed = true; // 토큰 재발급 실패 플래그 설정
          sessionStorage.clear();
          alert("로그인이 필요합니다.");
          router.push({ name: "login" });
          return Promise.reject(error);
        }

        if (tokenRefreshFailed) {
          // 이미 토큰 재발급이 실패한 경우, 추가 처리 없이 거절
          return Promise.reject(error);
        }

        const refreshToken = sessionStorage.getItem("refreshToken");
        if (!refreshToken) {
          isTokenRefreshing = false;
          sessionStorage.clear();
          alert("로그인이 필요합니다.");
          router.push({ name: "login" });
          return Promise.reject(error);
        }

        //토큰 재발급 요청
        if (!isTokenRefreshing) {
          isTokenRefreshing = true;

          try {
            const authStore = useAuthStore();
            await authStore.tokenRegenerate();

            const newAccessToken = sessionStorage.getItem("accessToken");
            console.log("인터셉터의 새로운 토큰 :", newAccessToken);
            // 재발급받은 accessToken을 모든 대기 중인 요청에 적용
            requestQueue.forEach((req) => req.resolve(newAccessToken));
            requestQueue = [];

            isTokenRefreshing = false;

            //새로운 토큰으로 재요청
            originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
            return instance(originalRequest);
          } catch (refreshError) {
            requestQueue.forEach((req) => req.reject(refreshError));
            requestQueue = [];
            isTokenRefreshing = false;
            tokenRefreshFailed = true; // 토큰 재발급 실패 플래그 설정

            sessionStorage.clear();
            alert("로그인이 필요합니다.");
            router.push({ name: "login" }); // Vue Router로 로그인 페이지로 이동
            return Promise.reject(refreshError);
          }
        }

        return new Promise((resolve, reject) => {
          requestQueue.push({ resolve, reject });
        })
          .then((token) => {
            originalRequest.headers.Authorization = `Bearer ${token}`;
            return instance(originalRequest);
          })
          .catch((err) => {
            // 토큰 재발급 실패 시 요청 거절
            return Promise.reject(err);
          });
      }

      //잘못된 접근
      if (status == httpStatusCode.FORBIDDEN) {
        alert(error.response.data.message);
        window.location.href = "/403";
      }

      return Promise.reject(error);
    }
  );
  return instance;
}

export { localAxios };
