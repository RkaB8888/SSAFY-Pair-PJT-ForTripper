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

//delete
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
};
