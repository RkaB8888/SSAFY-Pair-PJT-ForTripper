import { localAxios } from "@/util/http-commons";

const local = localAxios();

async function userConfirm(param, success, fail) {
  await local.post(`/users/login`, param).then(success).catch(fail);
}

async function findById(userid, success, fail) {
  const encodedEmail = encodeURIComponent(userid);
  console.log(encodedEmail);
  await local.get(`/users/${encodedEmail}`).then(success).catch(fail);
}
async function checkByToken(userid, success, fail) {
  local.defaults.headers["Authorization"] =
    sessionStorage.getItem("accessToken");
  const encodedEmail = encodeURIComponent(userid);
  await local.get(`/users/check/${encodedEmail}`).then(success).catch(fail);
}
async function tokenRegeneration(user, success, fail) {
  local.defaults.headers["refreshToken"] =
    sessionStorage.getItem("refreshToken"); //axios header에 refresh-token 셋팅
  await local.post(`/users/refresh`, user).then(success).catch(fail);
}

async function logout(userid, success, fail) {
  const encodedEmail = encodeURIComponent(userid);
  await local.delete(`/users/logout/${encodedEmail}`).then(success).catch(fail);
}

async function userRegister(param, success, fail) {
  await local.post(`/users/join`, param).then(success).catch(fail);
}
export {
  userConfirm,
  findById,
  checkByToken,
  tokenRegeneration,
  logout,
  userRegister,
};
