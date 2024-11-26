<script setup>
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { ref, computed, onMounted } from "vue";
import { usePlanStore } from "@/stores/plan";
import ShareBoardList from "@/components/layout/ShareBoardList.vue";

// Vue Router 사용
const router = useRouter();
const authStore = useAuthStore(); // Pinia 스토어 인스턴스 가져오기
const planStore = usePlanStore();
const sharePosts = ref([]);

// 라우팅 함수
const navigateTo = (path) => {
  router.push(path);
};

//게시글 불러오기
const fetchPosts = async () => {
  await planStore.fetchShareBoard();
  sharePosts.value = planStore.sharePosts;
};

onMounted(fetchPosts);
</script>

<template>
  <v-container class="text-center">
    <v-row justify="center" align="center">
      <v-col cols="12">
        <h1 class="text-h3 font-weight-bold mt-6 mb-6">플랜 공유 게시판</h1>
      </v-col>
    </v-row>
  </v-container>
  <v-row justify="center" align="center">
    <!-- 추가 버튼 중앙 정렬 -->
    <v-col cols="auto">
      <v-btn
        color="purple"
        variant="outlined"
        v-bind="props"
        @click="navigateTo('/planposts/add')"
      >
        나의 플랜 공유하기
      </v-btn>
    </v-col>
  </v-row>
  <ShareBoardList :sharePosts="sharePosts" />
  <!-- <v-card
    class="mx-auto"
    max-width="400"
  >
    <v-img
      class="align-end text-white"
      height="200"
      src="https://cdn.vuetifyjs.com/images/cards/docks.jpg"
      cover
    >
      <v-card-title>Top 10 Australian beaches</v-card-title>
    </v-img>

    <v-card-subtitle class="pt-4">
      Number 10
    </v-card-subtitle>

    <v-card-text>
      <div>Whitehaven Beach</div>

      <div>Whitsunday Island, Whitsunday Islands</div>
    </v-card-text>

    <v-card-actions>
      <v-btn color="orange" text="Share"></v-btn>

      <v-btn color="orange" text="Explore"></v-btn>
    </v-card-actions>
  </v-card> -->
</template>
<style></style>
