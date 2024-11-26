<script setup>
import { defineProps } from "vue";
import PlanList from "@/views/UserViews/PlanList.vue";

defineProps({
  userEmail: {
    type: String,
    required: true,
  },
  nickname: {
    type: String,
    required: true,
  },
});
</script>

<template>
  <v-card class="plan-list-card mb-6">
    <v-card-title class="headline plan-list-title">
      {{ nickname }} 님의 여행 계획
    </v-card-title>
    <v-divider></v-divider>
    <v-card-text>
      <PlanList :userEmail="userEmail" />
    </v-card-text>
  </v-card>
</template>

<style scoped>
.plan-list-card {
  padding: 20px;
}

.plan-list-title {
  color: rgb(98, 0, 234);
}
</style>
