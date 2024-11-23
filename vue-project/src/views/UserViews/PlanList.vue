<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

const props = defineProps({
  userEmail: {
    type: String,
    required: true,
  },
});

const router = useRouter();

const plans = ref([]);

onMounted(async () => {
  await fetchPlans();
});

const fetchPlans = async () => {
  // API를 통해 여행 계획 목록을 가져옵니다.
  try {
    // plans.value = await fetchPlansFromAPI(props.userId);
    // 임시 데이터
    plans.value = [
      {
        id: 1,
        title: "여행 계획 1",
        startDate: "2023-09-01",
        endDate: "2023-09-05",
      },
      {
        id: 2,
        title: "여행 계획 1",
        startDate: "2023-09-01",
        endDate: "2023-09-05",
      },
      {
        id: 3,
        title: "여행 계획 1",
        startDate: "2023-09-01",
        endDate: "2023-09-05",
      },
      // 추가 데이터...
    ];
  } catch (error) {
    console.error("여행 계획을 가져오는 중 오류 발생:", error);
  }
};

const formatDate = (date) => {
  return new Date(date).toLocaleDateString();
};

const goToPlan = (planId) => {
  router.push(`/plans/${planId}`);
};
</script>
<template>
  <v-list>
    <v-list-item
      v-for="plan in plans"
      :key="plan.id"
      @click="goToPlan(plan.id)"
    >
      <v-list-item-title>{{ plan.title }}</v-list-item-title>
      <v-list-item-subtitle
        >{{ formatDate(plan.startDate) }} ~
        {{ formatDate(plan.endDate) }}</v-list-item-subtitle
      >
    </v-list-item>
  </v-list>
</template>

<style scoped>
/* 필요에 따라 스타일 추가 */
</style>
