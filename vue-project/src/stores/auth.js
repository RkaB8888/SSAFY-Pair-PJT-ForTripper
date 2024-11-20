import { ref } from "vue";
import { defineStore } from "pinia";
import { useRouter } from "vue-router";
import { jwtDecode } from "jwt-decode";
import {
  userConfirm,
  findById,
  findByNickName,
  tokenRegeneration,
  logout,
  userRegister,
  checkByToken,
} from "@/api/authApi";
import { httpStatusCode } from "@/util/http-status";

export const useAuthStore = defineStore("authStore", () => {
  const router = useRouter();

  const isLogin = ref(false); //로그인 했는지 확인
  const isLoginError = ref(false); //로그인 에러가 있는지 확인
  const isValidToken = ref(false); //토큰 유효성 확인
  const userInfo = ref(null); // 사용자 정보

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
    console.log("로그아웃 아이디 : " + userInfo.value.email);
    await logout(
      userInfo.value.email,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          isLogin.value = false;
          userInfo.value = null;
          isValidToken.value = false;

          sessionStorage.removeItem("accessToken"); //토큰 제거
          sessionStorage.removeItem("refreshToken"); //토큰 제거
        } else {
          console.error("유저 정보 없음!!!!");
        }
      },
      (error) => {
        console.log(error);
      }
    );
  };
  const tokenRegenerate = async () => {
    await tokenRegeneration(
      JSON.stringify(userInfo.value),
      (response) => {
        if (response.status === httpStatusCode.OK) {
          let accessToken = response.data["access-token"];
          sessionStorage.setItem("accessToken", accessToken);
          isValidToken.value = true;
        }
      },
      async (error) => {
        // HttpStatus.UNAUTHORIZE(401) : RefreshToken 기간 만료 >> 다시 로그인!!!!
        if (error.response.status === httpStatusCode.UNAUTHORIZED) {
          // 다시 로그인 전 DB에 저장된 RefreshToken 제거.
          await logout(
            userInfo.value.email,
            (response) => {
              if (response.status === httpStatusCode.OK) {
                console.log("리프레시 토큰 제거 성공");
              } else {
                console.log("리프레시 토큰 제거 실패");
              }
              alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
              isLogin.value = false;
              userInfo.value = null;
              isValidToken.value = false;
              router.push({ name: "login" });
            },
            (error) => {
              console.error(error);
              isLogin.value = false;
              userInfo.value = null;
            }
          );
        }
      }
    );
  };
  const checkToken = async (token) => {
    console.log("checkToken 진입");
    console.log("encodeToken 값 확인:", token);
    let decodeToken = jwtDecode(token);
    console.log("1111111");
    console.log(decodeToken);
    await checkByToken(
      decodeToken.id,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          userInfo.value = response.data.userInfo;
          console.log("응답 성공");
        } else {
          userInfo.value = null;
          console.log("유저 정보 없음!!!!");
        }
      },
      async (error) => {
        console.log("에러");
        console.error(
          "g[토큰 만료되어 사용 불가능.] : ",
          error.response.status,
          error.response.statusText
        );
        isValidToken.value = false;

        await tokenRegenerate();
      }
    );
  };
  const checkIdDuplicate = async (userid) => {
    const isIdDuplicate = ref({});
    await findById(
      userid,
      (response) => {
        console.log("응답 성공:", response.data.message);
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
          userInfo.value = response.data.userInfo; // 유저 정보 업데이트
        } else {
          userInfo.value = null; // 유저 정보 없음
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
          userInfo.value = null; // 유저 정보 없음
          console.log("유저 정보 없음");
        }
      },
      (error) => {
        console.error("유저 정보 조회 실패:", error);
      }
    );
  };
  return {
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
  };
});
