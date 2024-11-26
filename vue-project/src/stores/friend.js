import { defineStore } from "pinia";
import { ref } from "vue";
import { localAxios } from "@/util/http-commons";

export const useFriendStore = defineStore("friendStore", () => {
  const friends = ref([]);
  const isFriend = ref(false);

  // 친구 목록 가져오기
  const fetchFriends = async () => {
    try {
      const response = await localAxios().get("/friends/");
      friends.value = response.data;
    } catch (error) {
      console.error("친구 목록 가져오기 실패:", error);
    }
  };

  // 친구 추가
  const addFriend = async (nickname) => {
    try {
      await localAxios().post(`/friends/${nickname}`);
      isFriend.value = true;
      await fetchFriends(); // 친구 목록 갱신
    } catch (error) {
      console.error("친구 추가 실패:", error);
    }
  };

  // 친구 해제
  const removeFriend = async (nickname) => {
    try {
      await localAxios().delete(`/friends/${nickname}`);
      isFriend.value = false;
      await fetchFriends(); // 친구 목록 갱신
    } catch (error) {
      console.error("친구 해제 실패:", error);
    }
  };

  // 특정 유저와 친구 관계인지 확인
  const checkFriendStatus = async (nickname) => {
    try {
      const response = await localAxios().get(`/friends/status/${nickname}`);
      isFriend.value = response.data.isFriend;
      return isFriend.value;
    } catch (error) {
      console.error("친구 상태 확인 실패:", error);
      return false;
    }
  };

  return {
    friends,
    isFriend,
    fetchFriends,
    addFriend,
    removeFriend,
    checkFriendStatus,
  };
});
