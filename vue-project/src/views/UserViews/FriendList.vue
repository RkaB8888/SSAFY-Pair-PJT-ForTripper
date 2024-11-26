<script setup>
import { computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useFriendStore } from "@/stores/friend";
import { useAuthStore } from "@/stores/auth";

const router = useRouter();
const friendStore = useFriendStore();
const authStore = useAuthStore();

const defaultProfileImage = authStore.defaultProfileImage;

// 환경 변수에서 VITE_TRIP_API_URL 가져오기
const { VITE_TRIP_API_URL } = import.meta.env;

onMounted(async () => {
  console.log("친구 목록 불러오기");
  await friendStore.fetchFriends();
});

const friends = computed(() => friendStore.friends);

const goToFriendProfile = (nickname) => {
  router.push(`/user/${nickname}`);
};

// 프로필 이미지 URL 설정 함수
const getProfileImageUrl = (url) => {
  return url ? `${VITE_TRIP_API_URL}${url}` : defaultProfileImage;
};

// 이미지 로딩 오류 시 기본 이미지로 대체
const onImageError = (event) => {
  event.target.src = defaultProfileImage;
};
</script>

<template>
  <v-card class="friend-list-card mb-6">
    <v-card-title class="headline"> 친구 목록 </v-card-title>
    <v-divider></v-divider>
    <v-card-text>
      <v-row>
        <v-col
          v-for="friend in friends"
          :key="friend.email"
          cols="6"
          sm="4"
          md="3"
          lg="2"
        >
          <v-card
            class="friend-card"
            @click="goToFriendProfile(friend.nickName)"
            outlined
            hover
          >
            <v-card-text class="text-center">
              <v-avatar size="80" class="mx-auto">
                <img
                  :src="getProfileImageUrl(friend.profileImage)"
                  alt="Friend Profile"
                  @error="onImageError"
                  class="avatar-image"
                />
              </v-avatar>
              <div class="mt-2 friend-nickname">
                {{ friend.nickName }}
              </div>
            </v-card-text>
          </v-card>
        </v-col>
        <v-col v-if="friends.length === 0" cols="12">
          <p>친구가 없습니다.</p>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<style scoped>
.friend-list-card {
  padding: 20px;
}

.friend-card {
  cursor: pointer;
}

.friend-nickname {
  font-size: 16px;
  color: rgb(98, 0, 234);
}

.avatar-image {
  object-fit: cover;
}
</style>
