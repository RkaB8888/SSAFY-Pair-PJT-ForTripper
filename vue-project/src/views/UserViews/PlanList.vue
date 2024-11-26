<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { localAxios } from "@/util/http-commons";

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
  try {
    // API를 통해 여행 계획 목록을 가져옵니다.
    const response = await localAxios().get(`/plans/user/${props.userEmail}`);
    plans.value = response.data;
  } catch (error) {
    console.error("여행 계획을 가져오는 중 오류 발생:", error);
    // 임시 데이터 사용
    plans.value = [
      {
        id: 1,
        title: "일본 휴가",
        startDate: "2021-09-01",
        endDate: "2021-09-07",
      },
      {
        id: 2,
        title: "서울 3박 5일",
        startDate: "2022-09-01",
        endDate: "2022-09-05",
      },
      {
        id: 3,
        title: "세계 여행",
        startDate: "2023-09-01",
        endDate: "2024-06-05",
      },
      // 추가 데이터...
    ];
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
  <v-row>
    <v-col v-for="plan in plans" :key="plan.id" cols="12" sm="6" md="4" lg="3">
      <v-card class="plan-card" @click="goToPlan(plan.id)" outlined hover>
        <v-card-title class="plan-title">
          {{ plan.title }}
        </v-card-title>
        <v-card-text>
          <div class="plan-dates">
            {{ formatDate(plan.startDate) }} ~ {{ formatDate(plan.endDate) }}
          </div>
        </v-card-text>
      </v-card>
    </v-col>
    <v-col v-if="plans.length === 0" cols="12">
      <p>여행 계획이 없습니다.</p>
    </v-col>
  </v-row>
</template>

<style scoped>
.plan-card {
  cursor: pointer;
  margin-bottom: 20px;
}

.plan-title {
  color: rgb(98, 0, 234);
  font-weight: bold;
}

.plan-dates {
  color: #666;
}
</style>
