import { ref } from "vue";
import { defineStore } from "pinia";
import { useRouter } from "vue-router";
import { jwtDecode } from "jwt-decode";
import {
  userConfirm,
  findById,
  findByNickName,
  tokenRegeneration,
  // logout,
  userRegister,
  checkByToken,
} from "@/api/authApi";
import { httpStatusCode } from "@/util/http-status";

export const useAuthStore = defineStore("authStore", () => {
  const router = useRouter();

  const isLogin = ref(false); //로그인 했는지 확인
  const isLoginError = ref(false); //로그인 에러가 있는지 확인
  const isValidToken = ref(false); //토큰 유효성 확인
  const userInfo = ref({}); // 사용자 정보

  const initializeAuthState = async () => {
    const token = sessionStorage.getItem("accessToken");
    if (token) {
      try {
        await checkToken(token); // 토큰 유효성 확인
        isLogin.value = true; // 토큰 유효하면 로그인 상태로 설정
      } catch (error) {
        console.error("토큰 검증 실패:", error);
        isLogin.value = false;
        userInfo.value = {}; // 초기화
      }
    } else {
      isLogin.value = false;
      userInfo.value = {}; // 초기화
    }
  };

  const userRegist = async (registUser) => {
    await userRegister(
      registUser,
      (response) => {
        if (response.status === httpStatusCode.CREATE) {
          console.log("회원가입 성공!!!");
          alert("회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.");
          router.push({ name: "login" });
        }
      },
      (error) => {
        console.log("회원가입 실패 :", error);
        alert("회원가입에 실패했습니다. 다시 시도해주세요.");
      }
    );
  };
  const userLogin = async (loginUser) => {
    await userConfirm(
      loginUser,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          console.log("로그인 성공!!!!");
          let { data } = response;
          let accessToken = data["access-token"]; //받은 액세스 토큰 저장
          let refreshToken = data["refresh-token"]; //받은 리프레시 토큰 저장
          isLogin.value = true; //로그인 성공
          isLoginError.value = false; //로그인 에러 없음
          isValidToken.value = true; //토큰 유효함
          sessionStorage.setItem("accessToken", accessToken);
          sessionStorage.setItem("refreshToken", refreshToken);
        }
      },
      (error) => {
        console.log("로그인 실패!!!!");
        isLogin.value = false; //로그인 실패 체크
        isLoginError.value = true; //에러 발생 체크
        isValidToken.value = false; //토큰 불가 체크
        console.error(error);
      }
    );
  };

  const userLogout = async () => {
    console.log(userInfo);
    isLogin.value = false;
    userInfo.value = {};
    isValidToken.value = false;
    sessionStorage.clear();
    alert("로그아웃 되었습니다.");
    // await logout(
    //   userInfo.value.email,
    //   (response) => {
    //     if (response.status === httpStatusCode.OK) {
    //       isLogin.value = false;
    //       userInfo.value = {};
    //       isValidToken.value = false;

    //       sessionStorage.removeItem("accessToken"); //토큰 제거
    //       sessionStorage.removeItem("refreshToken"); //토큰 제거
    //     } else {
    //       console.error("유저 정보 없음!!!!");
    //     }
    //   },
    //   (error) => {
    //     console.log(error);
    //   }
    // );
  };
  const tokenRegenerate = async () => {
    const refreshToken = sessionStorage.getItem("refreshToken");
    console.log("토큰 재발급 시작");
    // Refresh Token이 없는 경우
    if (!refreshToken) {
      alert("로그인이 만료되었습니다. 다시 로그인해주세요.");
      isLogin.value = false;
      userInfo.value = {};
      isValidToken.value = false;
      sessionStorage.clear();
      router.push({ name: "login" });
      return;
    }

    await tokenRegeneration(
      { refreshToken },
      (response) => {
        if (response.status === httpStatusCode.OK) {
          let accessToken = response.data["access-token"];
          sessionStorage.setItem("accessToken", accessToken);
          isValidToken.value = true;
        }
      },
      async (error) => {
        if (error.response.status === httpStatusCode.UNAUTHORIZED) {
          console.log("리프레시 만료");
          alert("로그인 세션이 만료되었습니다. 다시 로그인해주세요.");
        } else {
          console.log("비정상 토큰");
          alert("로그인 세션 비정상! 로그아웃 합니다.");
        }
        isLogin.value = false;
        userInfo.value = {};
        isValidToken.value = false;
        sessionStorage.clear();
        router.push({ name: "login" });
      }
    );
  };
  const checkToken = async (token) => {
    if (token) {
      try {
        let decodeToken = jwtDecode(token);
        console.log("디코딩 토큰:", decodeToken);

        // 토큰 검증
        await checkByToken(
          async (response) => {
            if (response.status === httpStatusCode.OK) {
              console.log("checkByToken 응답 성공:");
              isValidToken.value = true;
              await getUserInfoById(decodeToken.id);
              console.log("getUserInfoById 이후 :", userInfo.value);
            } else {
              console.warn("checkByToken 응답 실패");
              userInfo.value = {};
              isValidToken.value = false;
              sessionStorage.clear();
            }
          },
          (error) => {
            console.error("checkByToken 에러:", error);
            isValidToken.value = false;
            sessionStorage.clear();
          }
        );
      } catch (error) {
        console.error("checkToken 실패:", error);
        userInfo.value = {};
        isValidToken.value = false;
        sessionStorage.clear();
      }
    } else {
      console.log("토큰이 존재하지 않음");
    }
  };
  const checkIdDuplicate = async (userid) => {
    const isIdDuplicate = ref({});
    await findById(
      userid,
      (response) => {
        console.log("응답 성공:", response.data.status);
        isIdDuplicate.value.status = 200; // 중복된 아이디
        isIdDuplicate.value.msg = "해당 이메일은 이미 존재합니다.";
      },
      (error) => {
        console.error("응답 에러:", error.response.data.message);
        if (error.response.status === 404) {
          // 사용자 없음 메시지 확인
          if (error.response.data.message === "사용자를 찾을 수 없습니다.") {
            isIdDuplicate.value.status = 404;
            isIdDuplicate.value.msg = "";
          }
        } else {
          isIdDuplicate.value.status = 500;
          isIdDuplicate.value.msg = "서버 에러 등 기타 문제"; // 서버 에러 등 기타 문제
        }
      }
    );
    return isIdDuplicate.value;
  };
  const checkNickNameDuplicate = async (userNickName) => {
    const isNickNameDuplicate = ref({});
    await findByNickName(
      userNickName,
      (response) => {
        console.log("응답 성공:", response.data.message);
        isNickNameDuplicate.value.status = 200; // 중복된 아이디
        isNickNameDuplicate.value.msg = "해당 닉네임은 이미 존재합니다.";
      },
      (error) => {
        console.error("응답 에러:", error.response.data.message);
        if (error.response.status === 404) {
          // 사용자 없음 메시지 확인
          if (error.response.data.message === "사용자를 찾을 수 없습니다.") {
            isNickNameDuplicate.value.status = 404;
            isNickNameDuplicate.value.msg = "";
          }
        } else {
          isNickNameDuplicate.value.status = 500;
          isNickNameDuplicate.value.msg = "서버 에러 등 기타 문제"; // 서버 에러 등 기타 문제
        }
      }
    );
    return isNickNameDuplicate.value;
  };

  const getUserInfoById = async (userid) => {
    await findById(
      userid,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          userInfo.value = {
            email: response.data.email,
            name: response.data.name,
            nickname: response.data.nickname,
            role: response.data.role,
          };
          console.log("유저 정보 저장 :", userInfo.value);
        } else {
          userInfo.value = {}; // 유저 정보 없음
          console.log("유저 정보 없음");
        }
      },
      (error) => {
        console.error("유저 정보 조회 실패:", error);
      }
    );
  };
  const getUserInfoByNickName = async (userNickName) => {
    await findByNickName(
      userNickName,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          userInfo.value = response.data.userInfo; // 유저 정보 업데이트
        } else {
          userInfo.value = {}; // 유저 정보 없음
          console.log("유저 정보 없음");
        }
      },
      (error) => {
        console.error("유저 정보 조회 실패:", error);
      }
    );
  };
  return {
    initializeAuthState,
    isLogin,
    isLoginError,
    userLogin,
    userLogout,
    tokenRegenerate,
    checkToken,
    userRegist,
    getUserInfoById,
    getUserInfoByNickName,
    checkIdDuplicate,
    checkNickNameDuplicate,
    userInfo,
    isValidToken,
  };
});
