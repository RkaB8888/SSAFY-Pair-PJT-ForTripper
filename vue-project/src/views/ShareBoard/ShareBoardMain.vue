<script setup>
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { computed } from "vue";

// Vue Router 사용
const router = useRouter();
const authStore = useAuthStore(); // Pinia 스토어 인스턴스 가져오기

// 라우팅 함수
const navigateTo = (path) => {
  router.push(path);
};
</script>



<template>
    <h1>공유 게시판</h1>
    <v-btn @click="navigateTo('/planposts/add')">추가</v-btn>
    <v-card
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
  </v-card>
</template>
<style></style>