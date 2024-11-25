<script setup>
// npm install vue-draggable-plus
import { ref, onMounted, computed, defineProps } from "vue";
import { useRoute } from "vue-router";
import { usePlanStore } from "@/stores/plan";
import { vDraggable } from "vue-draggable-plus";
import DailyScheduleJustView from "@/components/layout/DailyScheduleJustView.vue";
import GoogleMapJustView from "@/components/layout/GoogleMapJustView.vue";

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

function onUpdate() {
  console.log("update");
}

//로컬스토리지에 Plan 저장
const storeSavePlan = async (plan) => {
  try {
    await planStore.savePlanToLocalStorage(plan);
  } catch (error) {
    console.error("에러: ", error);
  }
};

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
  <!-- <h1>Detail 페이지</h1>
  <div>
    <h2>{{ plan.plan_title }}</h2>
    <p>시작일: {{ plan.start_date }}</p>
    <p>종료일: {{ plan.end_date }}</p>
    <p>{{ plan.total_date - 1 }}박 {{ plan.total_date }}일 여행</p>
    <p>{{ plan.description }}</p>
  </div> -->

  <!-- 날짜 및 장소 표시 -->
  <div>
    <v-row no-gutters>
      <v-col cols="12" md="3">
        <DailyScheduleJustView
          :dateWithSchedules="dateWithSchedules"
          :selectedDate="selectedDate"
          :plan_id="plan.plan_id"
          @select-date="selectDate"
          @update-map-center="updateMapCenter"
        />
      </v-col>
      <v-col cols="12" md="9">
        <GoogleMapJustView
          :placesForSelectedDate="dailySchedules[selectedDate]"
          @select-place="savePlaceToDate"
          ref="googleMap"
        />
      </v-col>
    </v-row>
  </div>
</template>

<style scoped>
.datePlan {
  background-color: aliceblue;
  margin-top: 10px;
  cursor: pointer;
  padding: 10px;
}

.datePlan.selected {
  background-color: lightblue;
  font-weight: bold;
}

.datePlan ul {
  margin: 0;
  padding-left: 20px;
}

.datePlan p {
  font-style: italic;
  color: gray;
}

.ghost {
  opacity: 0.5;
  background: #c8ebfb;
}
</style>
