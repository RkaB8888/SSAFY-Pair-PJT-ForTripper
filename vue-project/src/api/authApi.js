import { localAxios } from "@/util/http-commons";

const local = localAxios();

//get
async function findById(param, success, fail) {
  await local.get(`/users/email/${param}`).then(success).catch(fail);
}
async function findByNickName(param, success, fail) {
  await local.get(`/users/nickname/${param}`).then(success).catch(fail);
}
async function checkByToken(success, fail) {
  await local.get(`/users/validate`).then(success).catch(fail);
}
// async function getProfileIMG(param, success, fail) {
//   await local.get(`/profileIMG/${param}`).then(success).catch(fail);
// }
// 유저 닉네임으로 검색
async function searchUsersByNickname(param, success, fail) {
  await local
    .get(`/users/search`, { params: { nickname: param } })
    .then(success)
    .catch(fail);
}

//post
async function userConfirm(param, success, fail) {
  await local.post(`/users/login`, param).then(success).catch(fail);
}
async function tokenRegeneration(param, success, fail) {
  await local.post(`/users/refresh`, param).then(success).catch(fail);
}
async function userRegister(param, success, fail) {
  await local.post(`/users/join`, param).then(success).catch(fail);
}
async function resetPasswordRequest(param, success, fail) {
  await local.post(`/users/forgot-password`, param).then(success).catch(fail);
}
async function resetPassword(param, success, fail) {
  await local.post(`/users/reset-password`, param).then(success).catch(fail);
}
// 프로필 사진 변경
async function updateProfileImageAPI(param, success, fail) {
  await local
    .post(`/users/profile-image`, param, {
      headers: { "Content-Type": "multipart/form-data" },
    })
    .then(success)
    .catch(fail);
}

//put
// 닉네임 수정
async function updateNicknameAPI(param, success, fail) {
  await local.put(`/users/nickname`, param).then(success).catch(fail);
}

// 비밀번호 수정
async function updatePasswordAPI(param, success, fail) {
  await local.put(`/users/password`, param).then(success).catch(fail);
}

//delete
// 회원 탈퇴
async function deleteAccountAPI(success, fail) {
  await local.delete(`/users/delete`).then(success).catch(fail);
}
// 로그아웃
// async function logout(param, success, fail) {
//   await local.delete(`/users/logout/${param}`).then(success).catch(fail);
// }

export {
  userConfirm,
  findById,
  findByNickName,
  checkByToken,
  tokenRegeneration,
  // logout,
  userRegister,
  resetPasswordRequest,
  resetPassword,
  updateProfileImageAPI,
  updateNicknameAPI,
  updatePasswordAPI,
  deleteAccountAPI,
  // getProfileIMG,
  searchUsersByNickname,
};
