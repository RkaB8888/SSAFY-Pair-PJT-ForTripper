<script setup>
// npm install vue-draggable-plus
import { ref, onMounted, computed } from "vue";
import { useRoute } from "vue-router";
import { usePlanStore } from "@/stores/plan";
import { vDraggable } from "vue-draggable-plus";
import PlanInfo from "@/components/layout/PlanInfo.vue";
import DailySchedule from "@/components/layout/DailySchedule.vue";
import GoogleMap from "@/components/layout/GoogleMap.vue";

const props = defineProps({
  plan_id: {
    type: String,
    required: true,
  },
});

const planStore = usePlanStore();
const route = useRoute();
const plan_id = route.params.plan_id;

const plan = ref({});
const dateList = ref([]); // 모든 날짜 리스트
const dailySchedules = ref({}); // 날짜별 일정 데이터
const selectedDate = ref(null); // 선택된 날짜

function onStart() {
  console.log("start");
}

function onUpdate(evt) {
  console.log("update", evt);
  const { oldIndex, newIndex } = evt;
  const date = evt.target.getAttribute("data-date");
  if (date && dailySchedules.value[date]) {
    const updatedPlaces = [...dailySchedules.value[date]];
    const [movedItem] = updatedPlaces.splice(oldIndex, 1);
    updatedPlaces.splice(newIndex, 0, movedItem);
    updateSchedule(date, updatedPlaces); // updateSchedule 호출
  }
}

// 날짜 리스트 생성 함수
const generateDateList = (start, end) => {
  const startDate = new Date(start);
  const endDate = new Date(end);
  const dates = [];

  while (startDate <= endDate) {
    dates.push(new Date(startDate).toISOString().split("T")[0]); // 'YYYY-MM-DD' 포맷
    startDate.setDate(startDate.getDate() + 1);
  }

  return dates;
};

// 서버에서 날짜별 방문 장소 데이터 가져오기
const fetchSchedulesByDate = async (plan_id) => {
  try {
    const response = await planStore.fetchVisitPlaceByDate(plan_id);
    console.log("가져온 데이터:", response.dailySchedules);
    dailySchedules.value = { ...response.dailySchedules };
    console.log("업데이트된 dailySchedules:", dailySchedules.value);
  } catch (error) {
    console.error("일정 불러오기 실패: ", error);
  }
};

// 날짜 클릭 시 호출
const selectDate = (date) => {
  selectedDate.value = date;
};

// 장소 저장 함수
const savePlaceToDate = (place) => {
  if (!selectedDate.value) {
    alert("날짜를 먼저 선택해주세요.");
    return;
  }
  if (!dailySchedules.value[selectedDate.value]) {
    dailySchedules.value[selectedDate.value] = [];
  }
  dailySchedules.value[selectedDate.value].push(place);
  alert(`장소가 ${selectedDate.value}에 저장되었습니다.`);
};

//장소 서버에 저장
const handleSave = () => {
  planStore.saveDailySchedules(dailySchedules, plan_id);
};

// 장소 삭제 함수
const removePlaceFromDate = (date, place) => {
  const index = dailySchedules.value[date].findIndex((p) => p === place);
  if (index !== -1) {
    dailySchedules.value[date].splice(index, 1);
  }
};

// 날짜별 데이터 생성 (날짜 + 장소)
const dateWithSchedules = computed(() => {
  return dateList.value.map((date) => ({
    date,
    places: dailySchedules.value[date] || [],
  }));
});

const updateSchedule = (date, updatedPlaces) => {
  if (dailySchedules.value[date]) {
    dailySchedules.value[date] = [...updatedPlaces];
    // 강제로 reactive 상태를 재할당
    dailySchedules.value = { ...dailySchedules.value };
  }
};

//페이지 로딩되면 자동 실행
onMounted(() => {
  const storePlan = localStorage.getItem("currentPlan");
  if (storePlan) {
    const parsedPlan = JSON.parse(storePlan);
    if (String(parsedPlan.plan.plan_id) === String(plan_id)) {
      plan.value = parsedPlan.plan;
      // 날짜 리스트 생성
      dateList.value = generateDateList(
        plan.value.start_date,
        plan.value.end_date
      );
      // 서버에서 날짜별 일정 가져오기
      fetchSchedulesByDate(plan_id);
    } else {
      console.warn("로컬 스토리지의 plan 데이터와 URL의 plan_id 불일치");
    }
  } else {
    console.warn("로컬 스토리지에 저장된 plan 데이터가 없습니다.");
  }
});
</script>

<template>
  <v-container fluid class="pa-0">
    <v-row justify="center">
      <v-col cols="12">
        <h1 class="text-h3 text-center mb-6">Detail 페이지</h1>
      </v-col>
    </v-row>
    <v-row justify="center">
      <v-col cols="12" md="8">
        <PlanInfo :plan="plan" />
      </v-col>
    </v-row>
    <v-row no-gutters>
      <v-col cols="12" md="3">
        <DailySchedule
          :dateWithSchedules="dateWithSchedules"
          :selectedDate="selectedDate"
          @save="handleSave"
          @select-date="selectDate"
          @remove-place="removePlaceFromDate"
          @drag-start="onStart"
          @drag-end="onUpdate"
          @update-schedule="updateSchedule"
          class="fill-height"
        />
      </v-col>
      <v-col cols="12" md="9">
        <GoogleMap @select-place="savePlaceToDate" class="fill-height" />
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.fill-height {
  height: calc(100vh - 200px); /* 예시 높이, 필요에 따라 조정 */
}
</style>
