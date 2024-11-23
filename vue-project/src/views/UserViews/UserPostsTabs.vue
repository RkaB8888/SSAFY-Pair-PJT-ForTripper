<script setup>
import { ref } from "vue";

// 각 탭에 해당하는 컴포넌트 임포트
import PlanList from "@/views/UserViews/PlanList.vue";
import ReviewList from "@/views/UserViews/ReviewList.vue";
import FreePostList from "@/views/UserViews/FreePostList.vue";

const props = defineProps({
  userEmail: {
    type: String,
    required: true,
  },
});

// 탭 데이터
const tabs = [
  { label: "여행 계획", value: "plans" },
  { label: "여행 후기", value: "reviews" },
  { label: "자유게시글", value: "posts" },
];

// 현재 활성화된 탭
const activeTab = ref("plans");
</script>

<template>
  <v-card>
    <!-- 탭 헤더 -->
    <v-tabs
      v-model="activeTab"
      align-tabs="center"
      color="deep-purple-accent-4"
    >
      <v-tab v-for="tab in tabs" :key="tab.value" :value="tab.value">
        {{ tab.label }}
      </v-tab>
    </v-tabs>

    <!-- 탭 콘텐츠 -->
    <v-tabs-window v-model="activeTab">
      <!-- 여행 계획 -->
      <v-tabs-window-item value="plans">
        <PlanList :userEmail="userEmail" />
      </v-tabs-window-item>

      <!-- 여행 후기 -->
      <v-tabs-window-item value="reviews">
        <ReviewList :userEmail="userEmail" />
      </v-tabs-window-item>

      <!-- 자유 게시글 -->
      <v-tabs-window-item value="posts">
        <FreePostList :userEmail="userEmail" />
      </v-tabs-window-item>
    </v-tabs-window>
  </v-card>
</template>

<style scoped>
/* 필요에 따라 스타일 추가 */
</style>
