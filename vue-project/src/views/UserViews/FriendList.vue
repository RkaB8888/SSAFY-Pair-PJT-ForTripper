<script setup>
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useFriendStore } from "@/stores/friend";
import { useAuthStore } from "@/stores/auth";

const router = useRouter();
const friendStore = useFriendStore();
const authStore = useAuthStore();

const defaultProfileImage = authStore.defaultProfileImage;

// 환경 변수에서 VITE_TRIP_API_URL 가져오기
const { VITE_TRIP_API_URL } = import.meta.env;

// 페이지당 친구 수
const friendsPerPage = 10;

const carouselIndex = ref(0);

onMounted(async () => {
  console.log("친구 목록 불러오기");
  await friendStore.fetchFriends();
});

const friends = computed(() => friendStore.friends);

const pagedFriends = computed(() => {
  const pages = [];
  for (let i = 0; i < friends.value.length; i += friendsPerPage) {
    pages.push(friends.value.slice(i, i + friendsPerPage));
  }
  return pages;
});

const goToFriendProfile = (nickname) => {
  router.push(`/user/${nickname}`);
};

// 프로필 이미지 URL 설정 함수
const getProfileImageUrl = (profileImagePath) => {
  return profileImagePath
    ? `${VITE_TRIP_API_URL}${profileImagePath}`
    : defaultProfileImage;
};
</script>

<template>
  <v-card class="friend-list">
    <v-card-title> 친구 목록 </v-card-title>
    <v-card-text>
      <v-row>
        <!-- 왼쪽 네비게이션 버튼 -->
        <v-col cols="1" class="d-flex align-center justify-center">
          <v-btn
            icon
            @click="
              carouselIndex =
                (carouselIndex - 1 + pagedFriends.length) % pagedFriends.length
            "
          >
            <v-icon>mdi-chevron-left</v-icon>
          </v-btn>
        </v-col>

        <!-- 캐러셀 -->
        <v-col cols="10">
          <v-carousel
            v-if="pagedFriends.length > 0"
            v-model="carouselIndex"
            hide-delimiter-background
            height="200"
            :show-arrows="false"
          >
            <v-carousel-item v-for="(page, index) in pagedFriends" :key="index">
              <v-row>
                <v-col
                  v-for="friend in page"
                  :key="friend.email"
                  cols="12"
                  md="6"
                  lg="1"
                  class="text-center"
                >
                  <v-avatar
                    size="64"
                    @click="goToFriendProfile(friend.nickName)"
                    class="cursor-pointer"
                  >
                    <img
                      :src="getProfileImageUrl(friend.profileImage)"
                      alt="Friend Profile"
                      @error="(e) => (e.target.src = defaultProfileImage)"
                      class="avatar-image"
                    />
                  </v-avatar>
                  <p>{{ friend.nickName }}</p>
                  <!-- 필요에 따라 친구 해제 버튼 추가 -->
                </v-col>
              </v-row>
            </v-carousel-item>
          </v-carousel>
          <div v-else>
            <p>친구 목록이 없습니다.</p>
          </div>
        </v-col>

        <!-- 오른쪽 네비게이션 버튼 -->
        <v-col cols="1" class="d-flex align-center justify-center">
          <v-btn
            icon
            @click="carouselIndex = (carouselIndex + 1) % pagedFriends.length"
          >
            <v-icon>mdi-chevron-right</v-icon>
          </v-btn>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<style scoped>
.friend-list .v-carousel {
  margin: 0;
}

.friend-list .v-btn {
  background-color: transparent;
}
</style>
