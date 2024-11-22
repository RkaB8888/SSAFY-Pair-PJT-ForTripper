<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

// 카테고리 목록
const categories = [
  { text: "전체", value: "all" },
  { text: "여행후기", value: "review" },
  { text: "인원모집", value: "recruit" },
  { text: "여행계획", value: "plan" },
  { text: "자유글", value: "free" },
  { text: "축제정보", value: "festival" },
];

// 정렬 옵션
const sortOptions = [
  { text: "최신순", value: "latest" },
  { text: "오래된순", value: "oldest" },
  { text: "조회순", value: "views" },
  { text: "좋아요순", value: "likes" },
];

// 데이터 및 상태 변수
const selectedCategory = ref("all");
const searchQuery = ref("");
const sortOption = ref("latest");
const viewMode = ref("list"); // 'list' or 'card'
const itemsPerPage = ref(10);
const page = ref(1);

// 게시글 데이터 (예시로 목업 데이터 사용)
const posts = ref([]);

// 테이블 헤더
const headers = [
  { text: "카테고리", value: "category", align: "start" },
  { text: "제목", value: "title" },
  { text: "작성자", value: "author" },
  { text: "날짜", value: "date" },
  { text: "조회수", value: "views" },
  { text: "좋아요", value: "likes" },
];

// 게시글 가져오기 (API 호출 대체)
const fetchPosts = () => {
  // 실제로는 API 호출을 통해 데이터를 가져와야 합니다.
  // 여기서는 목업 데이터를 생성합니다.
  const samplePosts = [];
  for (let i = 1; i <= 100; i++) {
    samplePosts.push({
      id: i,
      category:
        categories[Math.floor(Math.random() * (categories.length - 1)) + 1]
          .text,
      title: `게시글 제목 ${i}`,
      author: `작성자 ${i}`,
      date: new Date().toLocaleDateString(),
      views: Math.floor(Math.random() * 1000),
      likes: Math.floor(Math.random() * 100),
      summary: `게시글 요약 내용 ${i}`,
    });
  }
  posts.value = samplePosts;
};

// 선택된 카테고리에 따라 게시글 필터링
const filteredPosts = computed(() => {
  let filtered = posts.value;

  if (selectedCategory.value !== "all") {
    filtered = filtered.filter(
      (post) =>
        post.category ===
        categories.find((c) => c.value === selectedCategory.value).text
    );
  }

  if (searchQuery.value) {
    filtered = filtered.filter(
      (post) =>
        post.title.includes(searchQuery.value) ||
        post.author.includes(searchQuery.value)
    );
  }

  // 정렬
  if (sortOption.value === "latest") {
    filtered = filtered.sort((a, b) => b.id - a.id);
  } else if (sortOption.value === "oldest") {
    filtered = filtered.sort((a, b) => a.id - b.id);
  } else if (sortOption.value === "views") {
    filtered = filtered.sort((a, b) => b.views - a.views);
  } else if (sortOption.value === "likes") {
    filtered = filtered.sort((a, b) => b.likes - a.likes);
  }

  return filtered;
});

// 현재 페이지의 게시글 (카드형 보기용)
const paginatedPosts = computed(() => {
  const start = (page.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return filteredPosts.value.slice(start, end);
});

// 총 페이지 수
const totalPages = computed(() => {
  return Math.ceil(filteredPosts.value.length / itemsPerPage.value);
});

// 카테고리 선택 함수
const selectCategory = (category) => {
  selectedCategory.value = category;
  page.value = 1; // 페이지 초기화
};

// 컴포넌트 마운트 시 게시글 가져오기
onMounted(() => {
  fetchPosts();
});
</script>
<template>
  <v-container fluid>
    <!-- 상단 검색바 및 정렬, 보기 방식 선택 -->
    <v-row class="align-center mb-4">
      <v-col cols="8">
        <v-text-field
          v-model="searchQuery"
          label="검색어를 입력하세요"
          prepend-inner-icon="mdi-magnify"
          @keyup.enter="fetchPosts"
        ></v-text-field>
      </v-col>
      <v-col cols="4" class="text-right">
        <v-btn
          :color="viewMode === 'card' ? 'primary' : ''"
          icon
          @click="viewMode = 'card'"
        >
          <v-icon>mdi-view-grid</v-icon>
        </v-btn>
        <v-btn
          :color="viewMode === 'list' ? 'primary' : ''"
          icon
          @click="viewMode = 'list'"
        >
          <v-icon>mdi-format-list-bulleted</v-icon>
        </v-btn>
      </v-col>
    </v-row>

    <v-row>
      <!-- 좌측 카테고리 선택 -->
      <v-col cols="12" md="3">
        <v-list>
          <v-list-item
            v-for="category in categories"
            :key="category.value"
            :value="category.value"
            @click="selectCategory(category.value)"
            :active="selectedCategory === category.value"
          >
            <v-list-item-title>{{ category.text }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-col>

      <!-- 우측 게시글 목록 -->
      <v-col cols="12" md="9">
        <!-- 정렬 및 페이지당 아이템 수 선택 -->
        <v-row class="align-center mb-2">
          <v-col cols="6">
            <v-select
              v-model="sortOption"
              :items="sortOptions"
              item-title="text"
              item-value="value"
              label="정렬 기준"
              dense
            ></v-select>
          </v-col>
          <v-col cols="6" class="text-right">
            <v-select
              v-model="itemsPerPage"
              :items="[10, 30, 50]"
              label="페이지당 아이템 수"
              dense
            ></v-select>
          </v-col>
        </v-row>

        <!-- 게시글 목록 -->
        <div v-if="viewMode === 'list'">
          <!-- 자세히 보기 형태 -->
          <v-data-table
            :headers="headers"
            :items="filteredPosts"
            :items-per-page="itemsPerPage"
            v-model:page="page"
            :footer-props="{
              itemsPerPageOptions: [10, 30, 50],
              showFirstLastPage: true,
              firstIcon: 'mdi-arrow-collapse-left',
              lastIcon: 'mdi-arrow-collapse-right',
              prevIcon: 'mdi-minus',
              nextIcon: 'mdi-plus',
              maxButtons: 10,
            }"
          ></v-data-table>
        </div>
        <div v-else>
          <!-- 카드형 보기 -->
          <v-row>
            <v-col
              cols="12"
              sm="6"
              md="4"
              v-for="post in paginatedPosts"
              :key="post.id"
            >
              <v-card class="mb-4">
                <v-card-title>{{ post.title }}</v-card-title>
                <v-card-subtitle
                  >{{ post.author }} | {{ post.date }}</v-card-subtitle
                >
                <v-card-text>
                  {{ post.summary }}
                </v-card-text>
                <v-card-actions>
                  <v-btn icon>
                    <v-icon>mdi-eye</v-icon>
                    {{ post.views }}
                  </v-btn>
                  <v-btn icon>
                    <v-icon>mdi-thumb-up</v-icon>
                    {{ post.likes }}
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-col>
          </v-row>
          <!-- 페이지네이션 -->
          <v-pagination
            v-model="page"
            :length="totalPages"
            :total-visible="10"
          ></v-pagination>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
/* 필요한 스타일을 여기에 추가하세요 */
</style>
