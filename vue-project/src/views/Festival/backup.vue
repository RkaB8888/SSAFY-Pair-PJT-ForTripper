<script setup>
import { ref, onMounted, watch, computed } from "vue";
import axios from "axios";

// 서비스 키 가져오기 (환경 변수 설정 필요)
const serviceKey = import.meta.env.VITE_SERVICE_KEY;

// API 엔드포인트
const AREA_CODE_API = "https://apis.data.go.kr/B551011/KorService1/areaCode1";
const FESTIVAL_SEARCH_KEYWORD_API =
  "https://apis.data.go.kr/B551011/KorService1/searchKeyword1";
const FESTIVAL_SEARCH_DATE_API =
  "https://apis.data.go.kr/B551011/KorService1/searchFestival1";

// 탭 활성 상태 관리
const activeTab = ref("date"); // 초기 탭을 '일정 기반 검색'으로 설정

// 날짜 기반 검색 관련 데이터
const startDateMenu = ref(false);
const endDateMenu = ref(false);
const eventStartDate = ref(getCurrentDate()); // 초기 시작일을 현재 날짜로 설정
const eventEndDate = ref(""); // 종료일은 빈 값으로 설정

// 반응형 데이터 정의
const areaCodes = ref([]); // 지역 코드 목록
const sigunguCodes = ref([]); // 시군구 코드 목록 (키워드 검색)
const sigunguCodesDate = ref([]); // 시군구 코드 목록 (일정 기반 검색)
const selectedAreaCode = ref(null); // 선택된 지역 코드 (키워드 검색)
const selectedSigunguCode = ref(null); // 선택된 시군구 코드 (키워드 검색)
const selectedAreaCodeDate = ref(null); // 선택된 지역 코드 (일정 기반 검색)
const selectedSigunguCodeDate = ref(null); // 선택된 시군구 코드 (일정 기반 검색)
const keyword = ref(""); // 검색 키워드
const festivals = ref([]); // 검색된 축제 목록
const numOfRowsOptions = [10, 30, 50]; // 페이지 당 개수 옵션
const numOfRows = ref(10); // 선택된 페이지 당 개수
const pageNo = ref(1); // 현재 페이지 번호
const totalCount = ref(0); // 총 검색 결과 개수
const totalPages = computed(() =>
  Math.ceil(totalCount.value / numOfRows.value)
); // 총 페이지 수
const loadingAreaCodes = ref(false); // 지역 코드 로딩 상태
const loadingSigunguCodes = ref(false); // 시군구 코드 로딩 상태 (키워드 검색)
const loadingSigunguCodesDate = ref(false); // 시군구 코드 로딩 상태 (일정 기반 검색)
const loadingFestivals = ref(false); // 축제 검색 로딩 상태
const error = ref(null); // 오류 메시지

// 현재 날짜를 YYYYMMDD 형식으로 반환하는 함수
function getCurrentDate() {
  const today = new Date();
  const yyyy = today.getFullYear().toString();
  const mm = (today.getMonth() + 1).toString().padStart(2, "0");
  const dd = today.getDate().toString().padStart(2, "0");
  return `${yyyy}${mm}${dd}`;
}

// 지역 코드 목록 가져오기
const fetchAreaCodes = async () => {
  loadingAreaCodes.value = true;
  error.value = null;
  try {
    const response = await axios.get(AREA_CODE_API, {
      params: {
        numOfRows: 100,
        pageNo: 1,
        MobileOS: "ETC",
        MobileApp: "areaCodeSearch",
        _type: "json",
        serviceKey: serviceKey,
      },
    });
    const items = response.data.response.body.items.item;
    // items가 배열인지 단일 객체인지 확인하고, 올바른 형식으로 매핑
    areaCodes.value = Array.isArray(items)
      ? items.map((item) => ({
          code: item.code,
          name: item.name,
        }))
      : [
          {
            code: items.code,
            name: items.name,
          },
        ];
    console.log("Fetched areaCodes:", areaCodes.value);
  } catch (err) {
    console.error("지역 코드 가져오기 오류:", err);
    error.value = "지역 코드를 불러오는 중 오류가 발생했습니다.";
  } finally {
    loadingAreaCodes.value = false;
  }
};

// 시군구 코드 목록 가져오기 (키워드 검색)
const fetchSigunguCodes = async (areaCode) => {
  if (!areaCode) {
    sigunguCodes.value = [];
    return;
  }
  loadingSigunguCodes.value = true;
  error.value = null;
  try {
    const response = await axios.get(AREA_CODE_API, {
      params: {
        numOfRows: 100,
        pageNo: 1,
        MobileOS: "ETC",
        MobileApp: "areaCodeSearch",
        _type: "json",
        serviceKey: serviceKey,
        areaCode: areaCode,
      },
    });
    const items = response.data.response.body.items.item;
    sigunguCodes.value = Array.isArray(items)
      ? items.map((item) => ({
          code: item.code,
          name: item.name,
        }))
      : [
          {
            code: items.code,
            name: items.name,
          },
        ];
    console.log("Fetched sigunguCodes (Keyword):", sigunguCodes.value);
  } catch (err) {
    console.error("시군구 코드 가져오기 오류 (Keyword):", err);
    error.value = "시군구 코드를 불러오는 중 오류가 발생했습니다.";
  } finally {
    loadingSigunguCodes.value = false;
  }
};

// 시군구 코드 목록 가져오기 (일정 기반 검색)
const fetchSigunguCodesDate = async (areaCode) => {
  if (!areaCode) {
    sigunguCodesDate.value = [];
    return;
  }
  loadingSigunguCodesDate.value = true;
  error.value = null;
  try {
    const response = await axios.get(AREA_CODE_API, {
      params: {
        numOfRows: 100,
        pageNo: 1,
        MobileOS: "ETC",
        MobileApp: "areaCodeSearch",
        _type: "json",
        serviceKey: serviceKey,
        areaCode: areaCode,
      },
    });
    const items = response.data.response.body.items.item;
    sigunguCodesDate.value = Array.isArray(items)
      ? items.map((item) => ({
          code: item.code,
          name: item.name,
        }))
      : [
          {
            code: items.code,
            name: items.name,
          },
        ];
    console.log("Fetched sigunguCodes (Date):", sigunguCodesDate.value);
  } catch (err) {
    console.error("시군구 코드 가져오기 오류 (Date):", err);
    error.value = "시군구 코드를 불러오는 중 오류가 발생했습니다.";
  } finally {
    loadingSigunguCodesDate.value = false;
  }
};

// 지역 코드 변경 시 시군구 코드 목록 갱신 (키워드 검색)
const handleAreaCodeChange = (newAreaCode) => {
  console.log("handleAreaCodeChange 호출됨:", newAreaCode);
  selectedSigunguCode.value = null; // 시군구 코드 초기화
  fetchSigunguCodes(newAreaCode);
  // 검색 결과 및 페이지 초기화
  festivals.value = [];
  pageNo.value = 1;
  totalCount.value = 0;
};

// 지역 코드 변경 시 시군구 코드 목록 갱신 (일정 기반 검색)
const handleAreaCodeChangeDate = (newAreaCode) => {
  console.log("handleAreaCodeChangeDate 호출됨:", newAreaCode);
  selectedSigunguCodeDate.value = null; // 시군구 코드 초기화
  fetchSigunguCodesDate(newAreaCode);
  // 검색 결과 및 페이지 초기화
  festivals.value = [];
  pageNo.value = 1;
  totalCount.value = 0;
};

// 축제 검색 함수
const searchFestivals = async () => {
  loadingFestivals.value = true;
  error.value = null;
  festivals.value = [];
  totalCount.value = 0;

  try {
    let response;
    if (activeTab.value === "keyword") {
      // 키워드 기반 검색
      // 필수 조건 확인
      // if (!selectedAreaCode.value) {
      //   error.value = "지역을 선택해주세요.";
      //   loadingFestivals.value = false;
      //   return;
      // }
      // if (!selectedSigunguCode.value) {
      //   error.value = "시군구를 선택해주세요.";
      //   loadingFestivals.value = false;
      //   return;
      // }
      if (!keyword.value.trim()) {
        error.value = "키워드를 입력해주세요.";
        loadingFestivals.value = false;
        return;
      }

      response = await axios.get(FESTIVAL_SEARCH_KEYWORD_API, {
        params: {
          numOfRows: numOfRows.value,
          pageNo: pageNo.value,
          MobileOS: "ETC",
          MobileApp: "Search",
          _type: "json",
          listYN: "Y",
          arrange: "A",
          keyword: keyword.value,
          contentTypeId: 15, // 축제 콘텐츠 타입 ID
          areaCode: selectedAreaCode.value || undefined,
          sigunguCode: selectedSigunguCode.value || undefined,
          serviceKey: serviceKey,
        },
      });
    } else if (activeTab.value === "date") {
      // 일정 기반 검색
      // 필수 조건 확인
      if (!eventStartDate.value) {
        error.value = "행사 시작일을 선택해주세요.";
        loadingFestivals.value = false;
        return;
      }
      // if (!selectedAreaCodeDate.value) {
      //   error.value = "지역을 선택해주세요.";
      //   loadingFestivals.value = false;
      //   return;
      // }
      // 행사 종료일은 선택 사항이므로 필수 조건이 아님

      response = await axios.get(FESTIVAL_SEARCH_DATE_API, {
        params: {
          numOfRows: numOfRows.value,
          pageNo: pageNo.value,
          MobileOS: "ETC",
          MobileApp: "searchFestiuval",
          _type: "json",
          listYN: "Y",
          arrange: "A",
          eventStartDate: eventStartDate.value,
          eventEndDate: eventEndDate.value || undefined, // 종료일이 비어있으면 제외
          areaCode: selectedAreaCodeDate.value || undefined,
          sigunguCode: selectedSigunguCodeDate.value || undefined, // 시군구 코드가 비어있으면 제외
          serviceKey: serviceKey,
        },
      });
    }

    const items = response.data.response.body.items.item;
    const total = response.data.response.body.totalCount;

    festivals.value = Array.isArray(items) ? items : [items];
    totalCount.value = total;
    console.log("Fetched festivals:", festivals.value);
  } catch (err) {
    console.error("축제 검색 오류:", err);
    error.value = "축제 검색 중 오류가 발생했습니다.";
  } finally {
    loadingFestivals.value = false;
  }
};

// 이미지 URL 처리 함수
const getImageUrl = (imagePath) => {
  if (!imagePath) return "/img/default_festival.png"; // 기본 이미지 경로
  // imagePath가 절대 경로인지 확인하고, 상대 경로라면 백엔드 도메인을 추가
  if (imagePath.startsWith("http")) {
    return imagePath;
  }
  return `https://your-backend-domain${imagePath}`; // 'your-backend-domain'을 실제 백엔드 도메인으로 교체(쓸 일 없을 듯)
};

// 이미지 로딩 실패 시 기본 이미지로 대체
const onImageError = (event) => {
  event.target.src = "/img/default_festival.png"; // 기본 이미지 경로
};

// 컴포넌트 마운트 시 초기화
onMounted(() => {
  fetchAreaCodes();
  if (activeTab.value === "date") {
    // 초기 화면에서 진행 중인 행사 보여주기 (현재 날짜를 시작일로 설정)
    searchFestivals();
  }
});

// Watcher를 사용하여 selectedAreaCode가 변경될 때 handleAreaCodeChange 호출 (키워드 검색)
watch(selectedAreaCode, (newVal) => {
  handleAreaCodeChange(newVal);
});

// Watcher를 사용하여 selectedAreaCodeDate가 변경될 때 handleAreaCodeChangeDate 호출 (일정 기반 검색)
watch(selectedAreaCodeDate, (newVal) => {
  handleAreaCodeChangeDate(newVal);
});

// Watcher를 사용하여 numOfRows 또는 pageNo가 변경될 때 축제 검색
watch([numOfRows, pageNo], () => {
  if (festivals.value.length > 0) {
    searchFestivals();
  }
});

// Watcher를 사용하여 탭 변경 시 초기 검색 수행
watch(activeTab, (newTab, oldTab) => {
  if (newTab === "date") {
    // 날짜 기반 검색으로 변경 시 초기 검색 수행
    searchFestivals();
  } else if (newTab === "keyword") {
    // 키워드 기반 검색으로 변경 시 이전 검색 결과 초기화
    festivals.value = [];
    totalCount.value = 0;
    pageNo.value = 1;
  }
});
</script>

<template>
  <v-container>
    <h1>축제 검색</h1>

    <!-- 검색 방법 선택 탭 -->
    <v-tabs
      v-model="activeTab"
      background-color="primary"
      slider-color="secondary"
      class="mb-4"
    >
      <v-tab key="keyword">키워드 기반 검색</v-tab>
      <v-tab key="date">일정 기반 검색</v-tab>
    </v-tabs>

    <!-- 검색 조건 설정 -->
    <v-row>
      <!-- 키워드 기반 검색 폼 -->
      <v-col cols="12" v-if="activeTab === 'keyword'">
        <v-row>
          <!-- 지역 코드 선택 -->
          <v-col cols="12" sm="4">
            <v-select
              :items="areaCodes"
              item-title="name"
              item-value="code"
              label="지역 선택"
              v-model="selectedAreaCode"
              :loading="loadingAreaCodes"
              :disabled="loadingAreaCodes"
              clearable
              :rules="[(v) => !!v || '지역을 선택해주세요.']"
              required
            ></v-select>
          </v-col>

          <!-- 시군구 코드 선택 -->
          <v-col cols="12" sm="4">
            <v-select
              :items="sigunguCodes"
              item-title="name"
              item-value="code"
              label="시군구 선택"
              v-model="selectedSigunguCode"
              :loading="loadingSigunguCodesDate"
              :disabled="!selectedAreaCodeDate || loadingSigunguCodesDate"
              clearable
              :rules="[(v) => !!v || '시군구를 선택해주세요.']"
              required
            ></v-select>
          </v-col>

          <!-- 키워드 입력 -->
          <v-col cols="12" sm="4">
            <v-text-field
              v-model="keyword"
              label="키워드 입력"
              @keyup.enter="searchFestivals"
              :rules="[(v) => !!v.trim() || '키워드를 입력해주세요.']"
              required
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>

      <!-- 일정 기반 검색 폼 -->
      <v-col cols="12" v-if="activeTab === 'date'">
        <v-row>
          <!-- 행사 시작일 입력 -->
          <v-col cols="12" sm="4">
            <v-menu
              ref="startDateMenu"
              v-model="startDateMenu"
              :close-on-content-click="false"
              transition="scale-transition"
              offset-y
              min-width="290px"
            >
              <template #activator="{ on, attrs }">
                <v-text-field
                  v-model="eventStartDate"
                  label="행사 시작일 (필수)"
                  prepend-icon="mdi-calendar"
                  readonly
                  v-bind="attrs"
                  v-on="on"
                  :rules="[(v) => !!v || '행사 시작일을 선택해주세요.']"
                  required
                ></v-text-field>
              </template>
              <v-date-picker
                v-model="eventStartDate"
                @input="startDateMenu = false"
              ></v-date-picker>
            </v-menu>
          </v-col>

          <!-- 행사 종료일 입력 -->
          <v-col cols="12" sm="4">
            <v-menu
              ref="endDateMenu"
              v-model="endDateMenu"
              :close-on-content-click="false"
              transition="scale-transition"
              offset-y
              min-width="290px"
            >
              <template #activator="{ on, attrs }">
                <v-text-field
                  v-model="eventEndDate"
                  label="행사 종료일"
                  prepend-icon="mdi-calendar"
                  readonly
                  v-bind="attrs"
                  v-on="on"
                ></v-text-field>
              </template>
              <v-date-picker
                v-model="eventEndDate"
                @input="endDateMenu = false"
              ></v-date-picker>
            </v-menu>
          </v-col>

          <!-- 지역 코드 선택 -->
          <v-col cols="12" sm="4">
            <v-select
              :items="areaCodes"
              item-title="name"
              item-value="code"
              label="지역 선택"
              v-model="selectedAreaCodeDate"
              :loading="loadingAreaCodes"
              :disabled="loadingAreaCodes"
              clearable
              :rules="[(v) => !!v || '지역을 선택해주세요.']"
              required
            ></v-select>
          </v-col>

          <!-- 시군구 코드 선택 -->
          <v-col cols="12" sm="4">
            <v-select
              :items="sigunguCodesDate"
              item-title="name"
              item-value="code"
              label="시군구 선택"
              v-model="selectedSigunguCodeDate"
              :loading="loadingSigunguCodes"
              :disabled="!selectedAreaCodeDate || loadingSigunguCodes"
              clearable
            ></v-select>
          </v-col>
        </v-row>
      </v-col>
    </v-row>

    <!-- 검색 버튼 -->
    <v-row class="mb-4">
      <v-col cols="12" class="text-right">
        <v-btn
          color="primary"
          @click="searchFestivals"
          :disabled="loadingFestivals"
        >
          검색
        </v-btn>
      </v-col>
    </v-row>

    <!-- 표시할 개수 및 페이징 컨트롤 -->
    <v-row class="mb-4">
      <!-- 페이지 당 개수 선택 -->
      <v-col cols="12" sm="3">
        <v-select
          :items="numOfRowsOptions"
          label="표시할 개수"
          v-model="numOfRows"
          @change="searchFestivals"
        ></v-select>
      </v-col>

      <!-- 페이징 컨트롤 -->
      <v-col cols="12" sm="9" class="text-right">
        <v-pagination
          v-model="pageNo"
          :length="totalPages"
          @input="searchFestivals"
          :disabled="loadingFestivals"
        ></v-pagination>
      </v-col>
    </v-row>

    <!-- 로딩 인디케이터 -->
    <v-row>
      <v-col cols="12" class="text-center" v-if="loadingFestivals">
        <v-progress-circular
          indeterminate
          color="primary"
        ></v-progress-circular>
      </v-col>

      <!-- 오류 메시지 -->
      <v-col cols="12" v-if="error">
        <v-alert type="error" dismissible>
          {{ error }}
        </v-alert>
      </v-col>

      <!-- 축제 결과 -->
      <v-col cols="12" v-if="festivals.length > 0">
        <v-row>
          <v-col
            cols="12"
            sm="6"
            md="4"
            v-for="festival in festivals"
            :key="festival.contentid"
          >
            <v-card class="mb-4">
              <v-responsive aspect-ratio="16/9">
                <v-img
                  :src="getImageUrl(festival.firstimage)"
                  cover
                  style="object-position: bottom right"
                  alt="Festival Image"
                  @error="onImageError($event)"
                ></v-img>
              </v-responsive>
              <v-card-title>{{ festival.title }}</v-card-title>
              <v-card-subtitle
                >{{ festival.addr1 }}, {{ festival.addr2 }}</v-card-subtitle
              >
              <v-card-text>
                <p><strong>전화번호:</strong> {{ festival.tel || "N/A" }}</p>
                <p>
                  <strong>카테고리:</strong> {{ festival.cat1 }} >
                  {{ festival.cat2 }} > {{ festival.cat3 }}
                </p>
                <p>
                  <strong>지역 코드:</strong> {{ festival.areacode }},
                  {{ festival.sigungucode }}
                </p>
                <p>
                  <strong>지도 좌표:</strong> X: {{ festival.mapx }}, Y:
                  {{ festival.mapy }}
                </p>
                <p><strong>수정 시간:</strong> {{ festival.modifiedtime }}</p>
                <p><strong>생성 시간:</strong> {{ festival.createdtime }}</p>
                <p><strong>저작권:</strong> {{ festival.cpyrhtDivCd }}</p>
                <p><strong>여행 도서:</strong> {{ festival.booktour }}</p>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-col>

      <!-- 검색 결과 없음 메시지 -->
      <v-col cols="12" v-if="!loadingFestivals && festivals.length === 0">
        <v-alert type="info" dismissible> 검색 결과가 없습니다. </v-alert>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.mb-4 {
  margin-bottom: 16px;
}

.festival-title {
  background-color: aquamarine;
}

/* 축제 이미지 우측 하단 정렬 */

.v-img {
  object-position: bottom right;
}
/* 필수 입력 필드 표시 (Vuetify 기본 스타일 활용) */

input:required:invalid {
  border-color: red;
}
</style>
