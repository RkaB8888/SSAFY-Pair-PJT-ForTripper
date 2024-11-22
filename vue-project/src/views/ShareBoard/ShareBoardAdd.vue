<script setup>
import { ref, onMounted, watch, computed } from "vue";
import axios from "axios";
import { usePlanStore } from "@/stores/plan";

const planStore = usePlanStore();
const plans = ref([]);
const imageFile = ref(null);
const selectedPlan = ref(null);
const dailySchedules = ref({});
const dateList = ref([]);

// 여행 계획 목록 불러오기
const fetchPlans = async () => {
  try {
    await planStore.fetchPlans();
    plans.value = planStore.plans;
    console.log(plans);
  } catch (error) {
    console.log("데이터 불러오기 실패: ", error);
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

// 날짜별 데이터 생성 (날짜 + 장소)
const dateWithSchedules = computed(() => {
  return dateList.value.map((date) => ({
    date,
    places: dailySchedules.value[date] || [],
  }));
});

// 게시글 데이터
const postForm = ref({
  title: "",
  content: "",
  planId: null,
});

// 이미지 파일 선택
const onFileChange = (file) => {
  imageFile.value = file;
};

// 서버에서 날짜별 방문 장소 데이터 가져오기
const fetchSchedulesByDate = async (planId) => {
  try {
    const response = await planStore.fetchVisitPlaceByDate(planId);
    console.log("가져온 데이터:", response.dailySchedules);
    dailySchedules.value = { ...response.dailySchedules };
    console.log("업데이트된 dailySchedules:", dailySchedules.value);
  } catch (error) {
    console.error("일정 불러오기 실패: ", error);
  }
};

// 선택된 계획 정보 설정
const setSelectedPlan = (planId) => {
  selectedPlan.value = plans.value.find(plan => plan.plan_id === planId);
  if (selectedPlan.value) {
    dateList.value = generateDateList(selectedPlan.value.start_date, selectedPlan.value.end_date);
  }
};

// 선택된 계획의 상세 정보 가져오기
const fetchSelectedPlanDetails = async (planId) => {
  try {
    const response = await planStore.fetchPlanById(planId);
    selectedPlan.value = response;
    dateList.value = generateDateList(response.start_date, response.end_date);
    await fetchSchedulesByDate(planId);
  } catch (error) {
    console.error("계획 상세 정보 불러오기 실패: ", error);
  }
};


// 게시글 전송
const submitPost = async () => {
  try {
    const formData = new FormData();
    formData.append("title", postForm.value.title);
    formData.append("content", postForm.value.content);
    formData.append("planId", postForm.value.planId);
    if (imageFile.value) {
      formData.append("image", imageFile.value);
    }

    const response = await axios.post("/api/posts", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });

    alert("게시글 작성 완료!");
    console.log(response.data);
  } catch (error) {
    console.error("Error submitting post:", error);
  }
};

onMounted(() => {
  fetchPlans();
});

watch(() => postForm.value.planId, (newPlanId) => {
  if (newPlanId) {
    setSelectedPlan(newPlanId);
    fetchSchedulesByDate(newPlanId);
  } else {
    selectedPlan.value = null;
    dailySchedules.value = {};
    dateList.value = [];
  }
});
</script>

<template>
    <v-container>
      <v-row justify="center">
        <v-col cols="12" md="8" lg="6">
          <v-card>
            <v-card-title>게시글 작성</v-card-title>
            <v-card-text>
              <v-form ref="form">
                <!-- 제목 입력 -->
                <v-text-field
                  v-model="postForm.title"
                  label="제목"
                  outlined
                  required
                ></v-text-field>
  
                <!-- 내용 입력 -->
                <v-textarea
                  v-model="postForm.content"
                  label="내용"
                  outlined
                  rows="5"
                  required
                ></v-textarea>
  
                <!-- 이미지 업로드 -->
                <v-file-input
                  label="이미지 업로드"
                  outlined
                  show-size
                  accept="image/*"
                  @change="onFileChange"
                ></v-file-input>
  
                <!-- 여행 계획 선택 -->
                <v-select
                  v-model="postForm.planId"
                  :items="plans"
                  item-value="plan_id"
                  item-title="plan_title"
                  label="여행 계획 선택"
                  outlined
                  required
                ></v-select>
              </v-form>
            </v-card-text>
            <v-card-actions>
              <v-btn color="primary" @click="submitPost">작성</v-btn>
            </v-card-actions>
          </v-card>
  
          <!-- 선택된 여행 계획 정보 표시 -->
          <v-card v-if="selectedPlan" class="mt-4">
            <v-card-title>선택된 여행 계획 정보</v-card-title>
            <v-card-text>
              <p><strong>제목:</strong> {{ selectedPlan.plan_title }}</p>
              <p><strong>시작일:</strong> {{ selectedPlan.start_date }}</p>
              <p><strong>종료일:</strong> {{ selectedPlan.end_date }}</p>
              <p><strong>총 일수:</strong> {{ selectedPlan.total_date }}일</p>
              
              <v-expansion-panels>
                <v-expansion-panel
                  v-for="(daySchedule, index) in dateWithSchedules"
                  :key="index"
                >
                  <v-expansion-panel-title>
                    {{ daySchedule.date }}
                  </v-expansion-panel-title>
                  <v-expansion-panel-text>
                    <v-list v-if="daySchedule.places.length > 0">
                      <v-list-item v-for="place in daySchedule.places" :key="place.id">
                        {{ place.displayName }}
                      </v-list-item>
                    </v-list>
                    <p v-else>이 날의 일정이 없습니다.</p>
                  </v-expansion-panel-text>
                </v-expansion-panel>
              </v-expansion-panels>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
  <style scoped>
  .v-card {
    margin-top: 20px;
  }
  </style>