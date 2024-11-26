<script setup>
import { ref, onMounted } from "vue";
import { usePlanStore } from "@/stores/plan";
import PlanList from "@/components/layout/PlanList.vue";

const planStore = usePlanStore();
const plans = ref([]);

const fetchPlans = async () => {
  await planStore.fetchPlans();
  plans.value = planStore.plans;
};

const storeSavePlan = async (plan) => {
  try {
    await planStore.savePlanToLocalStorage(plan);
  } catch (error) {
    console.error("에러: ", error);
  }
};

onMounted(fetchPlans);

// CreatePlanDialog에서 Plan 생성 후 자동 새로고침
const handlePlanCreated = () => {
  fetchPlans();
};
</script>

<template>
  <v-container class="text-center">
    <v-row justify="center" align="center">
      <v-col cols="12">
        <h1 class="text-h3 font-weight-bold mt-6 mb-6">
          나만의 여행 플랜 생성하기
        </h1>
      </v-col>
    </v-row>

    <!-- 기존 컨텐츠 -->
  </v-container>
  <PlanList
    :plans="plans"
    @storeSavePlan="storeSavePlan"
    @plan-created="handlePlanCreated"
  />
</template>
