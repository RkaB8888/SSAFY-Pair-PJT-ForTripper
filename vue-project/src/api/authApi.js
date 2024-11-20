import { localAxios } from "@/util/http-commons";

const local = localAxios();

//get
async function findById(userid, success, fail) {
  //const encodedEmail = encodeURIComponent(userid); // 이메일 인코딩
  console.log("Encoded Email:", userid);
  await local.get(`/users/email/${userid}`).then(success).catch(fail);
}
async function findByNickName(userNickName, success, fail) {
  await local.get(`/users/nickname/${userNickName}`).then(success).catch(fail);
}
async function checkByToken(userid, success, fail) {
  local.defaults.headers["Authorization"] =
    sessionStorage.getItem("accessToken");
  await local.get(`/users/check/${userid}`).then(success).catch(fail);
}

//post
async function userConfirm(param, success, fail) {
  await local.post(`/users/login`, param).then(success).catch(fail);
}
async function tokenRegeneration(user, success, fail) {
  local.defaults.headers["refreshToken"] =
    sessionStorage.getItem("refreshToken"); //axios header에 refresh-token 셋팅
  await local.post(`/users/refresh`, user).then(success).catch(fail);
}
async function userRegister(param, success, fail) {
  await local.post(`/users/join`, param).then(success).catch(fail);
}

//delete
async function logout(userid, success, fail) {
  await local.delete(`/users/logout/${userid}`).then(success).catch(fail);
}

export {
  userConfirm,
  findById,
  findByNickName,
  checkByToken,
  tokenRegeneration,
  logout,
  userRegister,
};
