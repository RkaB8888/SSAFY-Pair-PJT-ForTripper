<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";

const props = defineProps({
  friends: {
    type: Array,
    required: true,
  },
});

const router = useRouter();

const defaultProfileImage = "/img/temp_profile.png";

// 페이지당 친구 수
const friendsPerPage = 10;

const pagedFriends = computed(() => {
  const pages = [];
  for (let i = 0; i < props.friends.length; i += friendsPerPage) {
    pages.push(props.friends.slice(i, i + friendsPerPage));
  }
  return pages;
});

const goToFriendProfile = (nickname) => {
  router.push(`/user/${nickname}`);
};

const removeFriend = (friendId) => {
  // 친구 해제 로직
  alert(`친구 ID ${friendId}를 해제합니다.`);
};
</script>
<template>
  <v-card class="friend-list">
    <v-card-title> 친구 목록 </v-card-title>
    <v-card-text>
      <v-row>
        <v-col cols="12">
          <v-carousel hide-delimiter-background height="150">
            <v-carousel-item v-for="(page, index) in pagedFriends" :key="index">
              <v-row>
                <v-col
                  v-for="friend in page"
                  :key="friend.id"
                  cols="12"
                  md="6"
                  lg="4"
                  class="text-center"
                >
                  <v-avatar
                    size="64"
                    @click="goToFriendProfile(friend.nickname)"
                    class="cursor-pointer"
                  >
                    <img
                      :src="friend.profileImage || defaultProfileImage"
                      alt="Friend Profile"
                    />
                  </v-avatar>
                  <p>{{ friend.nickname }}</p>
                  <v-btn small color="error" @click="removeFriend(friend.id)"
                    >친구 해제</v-btn
                  >
                </v-col>
              </v-row>
            </v-carousel-item>
          </v-carousel>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<style scoped>
.friend-list {
  margin-top: 20px;
}

.cursor-pointer {
  cursor: pointer;
}
</style>
