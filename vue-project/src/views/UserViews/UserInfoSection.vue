<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { useFriendStore } from "@/stores/friend";
import { localAxios } from "@/util/http-commons";

const { VITE_TRIP_API_URL } = import.meta.env;

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();
const friendStore = useFriendStore();

const onImageError = (event) => {
  event.target.src = authStore.defaultProfileImage;
};

const user = ref(null);

const isOwnProfile = computed(() => {
  return user.value && user.value.nickname === authStore.loginUserInfo.nickname;
});

const formattedJoinDate = computed(() => {
  return user.value ? new Date(user.value.joinDate).toLocaleDateString() : "";
});

const profileImageUrl = computed(() => {
  const imageUrl =
    user.value && user.value.profileImage
      ? `${VITE_TRIP_API_URL}${user.value.profileImage}`
      : authStore.defaultProfileImage;
  return imageUrl;
});

const fetchUserData = async (nickname) => {
  try {
    const response = await localAxios().get(`/users/nickname/${nickname}`);
    user.value = response.data;

    // 로컬 변수로 자신의 프로필인지 확인
    const isOwnProfileLocal =
      user.value && user.value.nickname === authStore.loginUserInfo.nickname;

    if (!isOwnProfileLocal) {
      await friendStore.checkFriendStatus(nickname);
    }
  } catch (error) {
    console.error("유저 정보 가져오기 실패:", error);
  }
};

onMounted(() => {
  fetchUserData(route.params.nickname);
});

watch(
  () => route.params.nickname,
  (newNickname, oldNickname) => {
    if (newNickname !== oldNickname) {
      fetchUserData(newNickname);
    }
  }
);

const isFriend = computed(() => friendStore.isFriend);

const editProfile = () => {
  router.push("/user/edit");
};

const toggleFriend = async () => {
  if (isFriend.value) {
    await friendStore.removeFriend(user.value.nickname);
    alert("친구가 해제되었습니다.");
  } else {
    await friendStore.addFriend(user.value.nickname);
    alert("친구가 추가되었습니다.");
  }
};

const reportUser = () => {
  alert("신고가 접수되었습니다.");
};
</script>

<template>
  <v-card v-if="user" class="user-info-card mb-6">
    <v-card-content>
      <v-row align="center">
        <!-- 프로필 사진 -->
        <v-col cols="12" md="4" class="text-center">
          <v-avatar size="150" class="profile-avatar mx-auto">
            <img
              :src="profileImageUrl"
              alt="Profile Image"
              class="avatar-image"
              @error="onImageError"
            />
          </v-avatar>
        </v-col>
        <!-- 유저 정보 -->
        <v-col cols="12" md="8">
          <div class="user-details">
            <h2 class="nickname mb-2">
              {{ user.nickname || "존재하지 않는 유저" }}
            </h2>
            <p class="join-date mb-4">가입일: {{ formattedJoinDate }}</p>

            <!-- 자신의 정보인 경우 -->
            <div v-if="isOwnProfile" class="action-buttons">
              <v-btn
                color="primary"
                @click="editProfile"
                size="large"
                class="mr-2"
              >
                프로필 수정
              </v-btn>
            </div>
            <!-- 타인의 정보인 경우 -->
            <div v-else class="action-buttons">
              <v-btn
                :color="isFriend ? 'grey' : 'primary'"
                @click="toggleFriend"
                size="large"
                class="mr-2"
              >
                {{ isFriend ? "친구 해제" : "친구 추가" }}
              </v-btn>
              <v-btn color="error" @click="reportUser" size="large">
                유저 신고
              </v-btn>
            </div>
          </div>
        </v-col>
      </v-row>
    </v-card-content>
  </v-card>
  <!-- 로딩 상태 -->
  <v-card v-else class="user-loading-card">
    <v-card-text class="text-center">
      <v-progress-circular indeterminate color="primary"></v-progress-circular>
      <p>유저 정보를 불러오는 중입니다...</p>
    </v-card-text>
  </v-card>
</template>

<style scoped>
.user-info-card {
  background-color: #fff;
  border-radius: 16px;
  padding: 20px;
}

.profile-avatar {
  border: 3px solid rgb(98, 0, 234);
  box-shadow: 0 4px 10px rgba(98, 0, 234, 0.3);
}

.avatar-image {
  object-fit: cover;
}

.nickname {
  color: rgb(98, 0, 234);
  font-size: 24px;
  font-weight: bold;
}

.join-date {
  color: #666;
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.user-loading-card {
  padding: 40px;
  margin-top: 20px;
}
</style>
