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
  resetPasswordRequest,
  resetPassword,
  updateProfileImageAPI,
  updateNicknameAPI,
  updatePasswordAPI,
  deleteAccountAPI,
  // getProfileIMG,
} from "@/api/authApi";
import { httpStatusCode } from "@/util/http-status";
import { resetTokenRefreshFailed } from "@/util/http-commons";
export const useAuthStore = defineStore("authStore", () => {
  const router = useRouter();

  const isLogin = ref(false); //로그인 했는지 확인
  const isLoginError = ref(false); //로그인 에러가 있는지 확인
  const isValidToken = ref(false); //토큰 유효성 확인
  const loginUserInfo = ref({}); // 사용자 정보
  const userInfo = ref({});

  const defaultProfileImage = "/img/Default_Profile.png";
  const initializeAuthState = async () => {
    const token = sessionStorage.getItem("accessToken");
    if (token) {
      try {
        await checkToken(); // 토큰 유효성 확인
        loginUserInfo.value = { ...userInfo.value };
        userInfo.value = {};
      } catch (error) {
        console.error("토큰 검증 실패:", error);
        isLogin.value = false;
        loginUserInfo.value = {}; // 초기화
      }
    } else {
      isLogin.value = false;
      loginUserInfo.value = {}; // 초기화
    }
  };

  const userRegist = async (registUser) => {
    await userRegister(
      registUser,
      (response) => {
        if (response.status === httpStatusCode.CREATE) {
          console.log("회원가입 성공!!!");
          alert(
            "회원가입이 완료되었습니다. 이메일을 확인하여 계정을 활성화하세요."
          );
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

          resetTokenRefreshFailed(); // 토큰 재발급 실패 플래그 초기화
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
    console.log(loginUserInfo);
    isLogin.value = false;
    isLoginError.value = false; // 로그인 에러 상태 초기화
    loginUserInfo.value = {};
    isValidToken.value = false;
    sessionStorage.clear();
    alert("로그아웃 되었습니다.");
    resetTokenRefreshFailed(); // 토큰 재발급 실패 플래그 초기화
  };
  const tokenRegenerate = async () => {
    const refreshToken = sessionStorage.getItem("refreshToken");
    console.log("토큰 재발급 시작");
    // Refresh Token이 없는 경우
    if (!refreshToken) {
      isLogin.value = false;
      loginUserInfo.value = {};
      isValidToken.value = false;
      sessionStorage.clear();
      // 알림 및 리다이렉트 제거
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
        console.log("auth.js tokenRegeneration", error);
        isLogin.value = false;
        loginUserInfo.value = {};
        isValidToken.value = false;
        sessionStorage.clear();
        // 알림 및 리다이렉트 제거
        return;
      }
    );
  };
  const checkToken = async () => {
    const token = sessionStorage.getItem("accessToken");
    if (token) {
      try {
        const decodedToken = jwtDecode(token);
        console.log("디코딩 토큰:", decodedToken);

        // JWT 토큰의 페이로드에서 사용자 식별자 필드 확인 (예: email 또는 sub)
        const userId = decodedToken.id;
        if (!userId) {
          throw new Error("Token does not contain user identifier");
        }

        // 토큰 검증
        await checkByToken(
          async (response) => {
            if (response.status === httpStatusCode.OK) {
              console.log("checkByToken 응답 성공:");
              isValidToken.value = true;
              isLogin.value = true; // 로그인 상태로 설정
              await getUserInfoById(userId);
              loginUserInfo.value = { ...userInfo.value };
              console.log("getUserInfoById 이후 :", loginUserInfo.value);
            } else {
              console.warn("checkByToken 응답 실패");
              loginUserInfo.value = {};
              isValidToken.value = false;
              isLogin.value = false; // 로그인 상태 해제
              sessionStorage.clear();
            }
          },
          (error) => {
            console.error("checkByToken 에러:", error);
            loginUserInfo.value = {};
            isValidToken.value = false;
            isLogin.value = false; // 로그인 상태 해제
            sessionStorage.clear();
          }
        );
      } catch (error) {
        console.error("checkToken 실패:", error);
        loginUserInfo.value = {};
        isValidToken.value = false;
        isLogin.value = false; // 로그인 상태 해제
        sessionStorage.clear();
      }
    } else {
      console.log("토큰이 존재하지 않음");
      isValidToken.value = false;
      isLogin.value = false; // 로그인 상태 해제
      loginUserInfo.value = {};
      // API 호출 없이 종료
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
            joinDate: response.data.joinDate,
            profileImage:
              response.data.profileImage === "null"
                ? null
                : response.data.profileImage,
          };
          console.log("유저 정보 저장 :", userInfo.value);
        } else {
          userInfo.value = {}; // 유저 정보 없음
          console.log("유저 정보 없음");
        }
      },
      (error) => {
        userInfo.value = {};
        console.error("유저 정보 조회 실패:", error);
      }
    );
  };
  const getUserInfoByNickName = async (userNickName) => {
    await findByNickName(
      userNickName,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          userInfo.value = {
            email: response.data.email,
            name: response.data.name,
            nickname: response.data.nickname,
            role: response.data.role,
            joinDate: response.data.joinDate,
            profileImage:
              response.data.profileImage === "null"
                ? null
                : response.data.profileImage,
          }; // 유저 정보 업데이트
          console.log("닉네임 찾기로 가져온 유저 정보", userInfo.value);
        } else {
          userInfo.value = {}; // 유저 정보 없음
          console.log("유저 정보 없음");
        }
      },
      (error) => {
        userInfo.value = {};
        console.error("닉네임으로 유저 정보 조회 실패:", error);
      }
    );
  };
  const requestPasswordReset = async (email) => {
    await resetPasswordRequest(
      { email },
      (response) => {
        console.log("비밀번호 재설정 이메일 전송 성공:", response.data.message);
      },
      (error) => {
        console.error("비밀번호 재설정 이메일 전송 실패:", error);
        throw error;
      }
    );
  };
  const handleResetPassword = async (token, newPassword) => {
    await resetPassword(
      { token, newPassword },
      (response) => {
        console.log("비밀번호 재설정 성공:", response.data.message);
      },
      (error) => {
        console.error("비밀번호 재설정 실패:", error);
        throw error;
      }
    );
  };
  const updateProfileImage = async (profileImageFile) => {
    const formData = new FormData();
    formData.append("profileImage", profileImageFile);

    await updateProfileImageAPI(
      formData,
      (response) => {
        console.log("프로필 사진 변경 성공:", response);
        loginUserInfo.value.profileImage = response.data.profileImageUrl;
      },
      (error) => {
        console.error("프로필 사진 변경 실패:", error);
        throw error;
      }
    );
  };
  const updateNickname = async (newNickname) => {
    await updateNicknameAPI(
      { nickname: newNickname },
      (response) => {
        console.log("닉네임 변경 성공:", response.data);
        userInfo.value.nickname = newNickname;
      },
      (error) => {
        console.error("닉네임 변경 실패:", error);
        throw error;
      }
    );
  };
  const updatePassword = async (currentPassword, newPassword) => {
    await updatePasswordAPI(
      { currentPassword, newPassword },
      (response) => {
        console.log("비밀번호 변경 성공:", response.data);
      },
      (error) => {
        console.error("비밀번호 변경 실패:", error);
        throw error;
      }
    );
  };
  const deleteAccount = async () => {
    await deleteAccountAPI(
      (response) => {
        console.log("회원 탈퇴 성공:", response.data);
        userLogout();
      },
      (error) => {
        console.error("회원 탈퇴 실패:", error);
        throw error;
      }
    );
  };
  // const getProfileImage = async (filename) => {
  //   await getProfileIMG(
  //     filename,
  //     (response) => {
  //       const blob = response.data; // 서버에서 반환된 Blob 데이터
  //       const imageUrl = URL.createObjectURL(blob); // Blob 데이터를 렌더링 가능한 URL로 변환
  //       userInfo.value.profileImage = imageUrl; // 이미지 URL을 저장
  //       console.log("프로필 이미지 로드 성공:", imageUrl);
  //     },
  //     (error) => {
  //       console.error("프로필 이미지 로드 실패:", error);
  //       userInfo.value.profileImage = defaultProfileImage; // 실패 시 기본 이미지 사용
  //     }
  //   );
  // };

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
    loginUserInfo,
    userInfo,
    isValidToken,
    requestPasswordReset,
    handleResetPassword,
    updateProfileImage,
    updateNickname,
    updatePassword,
    deleteAccount,
    defaultProfileImage,
    // getProfileImage,
  };
});
