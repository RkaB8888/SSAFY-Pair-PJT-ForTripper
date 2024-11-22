<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { usePlanStore } from "@/stores/plan";
import { useAuthStore } from "@/stores/auth";

// npm install @vuepic/vue-datepicker
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';

const planStore = usePlanStore();
const router = useRouter();
const plans = ref([]);

//여행 계획 폼 데이터
const planForm = ref({
  plan_title: "",
  start_date: "",
  end_date: "",
  description: "",
});


//여행 계획 목록 불러오기
const fetchPlans = async () => {
  try {
    await planStore.fetchPlans();
    plans.value = planStore.plans;
    console.log(plans);
  } catch (error) {
    console.log("데이터 불러오기 실패: " , error);
  }
};


//여행 계획 추가
const add = async () => {
  try {

    const start = new Date(date.value[0]);
    const end = new Date(date.value[1]);
    planForm.value.start_date = start.toISOString().split('T')[0]; // '2024-11-07'
    planForm.value.end_date = end.toISOString().split('T')[0];

    await planStore.addPlan(planForm.value);
    // 계획 목록을 새로 불러오기
    await planStore.fetchPlans();
    router.push("/plans");
  } catch (error) {
    console.error("에러: ", error);
  }
};

//로컬스토리지에 Plan 저장
const storeSavePlan = async (plan) => {
  try {
    await planStore.savePlanToLocalStorage(plan);
  } catch (error) {
    console.error("에러: ", error)
  }
}

const date = ref();

// For demo purposes assign range from the current date
// 페이지 로드될 때 데이터 불러옴
onMounted(() => {
  const start_date = new Date();
  const end_date = new Date(new Date().setDate(start_date.getDate() + 7));
  date.value = [start_date, end_date];

  fetchPlans();

});


</script>

<template>
  <h1>Plan 메인 화면입니다</h1>
  <form @submit.prevent="add">
    <label><input type = "text" v-model.trim="planForm.plan_title"/>여행 계획 이름</label>
    <br />
    <VueDatePicker v-model="date" locale="ko" range :enable-time-picker="false"/>
    <br />
    <label><input type = "text" v-model.trim="planForm.description"/>설명</label>
    <br />
    <input type="submit" value="여행 생성"/>
  </form>

  <h2>여행 계획 목록</h2>
  <ul>
    <li v-for="(plan, index) in plans" :key="index">
      <router-link :to="{ 
        name: 'PlanDetail', 
        params: { plan_id: plan.plan_id },
      }" @click="storeSavePlan(plan)">
      <div class="plan-list">
        <h3>{{ plan.plan_title }}</h3>
        <p>시작일: {{ plan.start_date }}</p>
        <p>종료일: {{ plan.end_date }}</p>
        <p>{{ plan.total_date - 1 }}박 {{ plan.total_date }}일 여행</p>
        <p>{{ plan.description }}</p>
      </div>
    </router-link>
      <br />
    </li>
  </ul>
</template>

<style scoped>
  .plan-list {
    background-color: rgb(219, 219, 219);
    margin: 20px;
  }
</style>
