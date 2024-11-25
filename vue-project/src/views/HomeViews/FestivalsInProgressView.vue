<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import FestivalInProgressView from "@/views/HomeViews/FestivalInProgressView.vue"; // 컴포넌트 경로 확인

// 서비스 키 가져오기 (환경 변수 설정 필요)
const serviceKey = import.meta.env.VITE_SERVICE_KEY;

// API 엔드포인트
const FESTIVAL_SEARCH_DATE_API =
  "https://apis.data.go.kr/B551011/KorService1/searchFestival1";

// 오늘 날짜를 'yyyyMMdd' 형식으로 변환하는 함수
function formatDateToYYYYMMDD(date) {
  const yyyy = date.getFullYear().toString();
  const mm = (date.getMonth() + 1).toString().padStart(2, "0");
  const dd = date.getDate().toString().padStart(2, "0");
  return `${yyyy}${mm}${dd}`;
}

// 오늘 날짜
const today = new Date();
const formattedToday = formatDateToYYYYMMDD(today);

// 반응형 데이터 정의
const festivals = ref([]);
const loading = ref(false);
const error = ref(null);

// 축제 검색 함수
const fetchOngoingFestivals = async () => {
  loading.value = true;
  error.value = null;
  festivals.value = [];

  try {
    const params = {
      numOfRows: 100, // 원하는 개수로 조정 가능
      pageNo: 1,
      MobileOS: "ETC",
      MobileApp: "festivalInProgress",
      _type: "json",
      listYN: "Y",
      arrange: "A",
      eventStartDate: formattedToday, // 오늘 날짜
      eventEndDate: formattedToday, // 오늘 날짜
      serviceKey: serviceKey,
    };

    const response = await axios.get(FESTIVAL_SEARCH_DATE_API, { params });

    const items = response.data.response.body.items.item;
    if (items) {
      festivals.value = Array.isArray(items) ? items : [items];
      console.log("Fetched ongoing festivals:", festivals.value);
    } else {
      console.log("No ongoing festivals found.");
    }
  } catch (err) {
    console.error("진행 중인 축제 검색 오류:", err);
    error.value = "진행 중인 축제를 불러오는 중 오류가 발생했습니다.";
  } finally {
    loading.value = false;
  }
};

// 컴포넌트 마운트 시 축제 검색 실행
onMounted(() => {
  fetchOngoingFestivals();
});
</script>

<template>
  <div class="popular-festival-board">
    <h2>현재 진행 중인 축제</h2>

    <!-- 로딩 인디케이터 -->
    <div v-if="loading" class="text-center">
      <v-progress-circular indeterminate color="primary"></v-progress-circular>
    </div>

    <!-- 오류 메시지 -->
    <div v-if="error" class="text-center">
      <v-alert type="error" dismissible>
        {{ error }}
      </v-alert>
    </div>

    <!-- 축제 카드 표시 -->
    <div v-if="!loading && !error && festivals.length > 0">
      <v-slide-group show-arrows>
        <v-slide-group-item
          v-for="festival in festivals"
          :key="festival.contentid"
        >
          <FestivalInProgressView :festival="festival" />
        </v-slide-group-item>
      </v-slide-group>
    </div>

    <!-- 검색 결과 없음 메시지 -->
    <div
      v-if="!loading && !error && festivals.length === 0"
      class="text-center"
    >
      <v-alert type="info" dismissible>진행 중인 축제가 없습니다.</v-alert>
    </div>
  </div>
</template>

<style scoped>
.popular-festival-board {
  padding: 16px;
}
</style>
