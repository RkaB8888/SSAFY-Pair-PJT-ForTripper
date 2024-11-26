<script setup>
import { computed, defineProps, defineEmits } from "vue";
import { usePlanStore } from "@/stores/plan";

const planStore = usePlanStore();

// props 정의
const props = defineProps({
  sharePosts: Array,
});

// imageUrl을 처리하는 computed 속성
const processedPosts = computed(() => {
  return props.sharePosts.map((post) => {
    // 만약 이미지 URL이 없으면 기본 이미지를 제공할 수 있음
    if (!post.imageUrl) {
      post.imageUrl = "/path/to/default/image.jpg"; // 기본 이미지 경로
    }
    return post;
  });
});
</script>

<template>
  <div class="horizontal-list-container">
    <div class="horizontal-list">
      <div
        v-for="(post, index) in processedPosts"
        :key="index"
        class="post-item"
      >
        <v-card
          class="mx-auto custom-card"
          max-width="400"
          hover
          outlined
          :to="{ name: 'SharePostView', params: { plan_id: post.plan_id } }"
          style="cursor: pointer"
        >
          <v-img
            class="align-end text-white"
            height="200"
            :src="post.imageUrl"
            cover
          >
            <v-card-title>{{ post.title }}</v-card-title>
          </v-img>

          <v-card-subtitle class="pt-4">
            {{ post.created_time }}
          </v-card-subtitle>

          <v-card-text>
            {{ post.content }}
          </v-card-text>
        </v-card>
      </div>
    </div>
  </div>
</template>
<style scoped>
.custom-card {
  width: 300px; /* 원하는 고정 너비 설정 */
  max-width: 100%;
}

.v-card__text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.horizontal-list-container {
  display: flex;
  justify-content: center;
  width: 100%;
}

.horizontal-list {
  display: flex;
  overflow-x: auto;
  gap: 16px;
  padding: 16px;
}

.post-item {
  flex: 0 0 auto;
}
</style>
