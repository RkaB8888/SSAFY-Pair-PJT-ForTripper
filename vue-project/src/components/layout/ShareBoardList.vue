<script setup>
import { computed, defineProps, defineEmits } from 'vue';

// props 정의
const props = defineProps({
  sharePosts: Array
});

// imageUrl을 처리하는 computed 속성
const processedPosts = computed(() => {
  return props.sharePosts.map(post => {
    // 만약 이미지 URL이 없으면 기본 이미지를 제공할 수 있음
    if (!post.imageUrl) {
      post.imageUrl = '/path/to/default/image.jpg';  // 기본 이미지 경로
    }
    return post;
  });
});

</script>

<template>
    <v-container>
        <v-col
            v-for="(post, index) in processedPosts"
            :key="index"
            cols="auto"
            sm="6"
            md="4"
        >
        <v-card
            class="mx-auto"
            max-width="400"
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

            <v-card-actions>
            <v-btn color="orange" text="Share"></v-btn>

            <v-btn color="orange" text="Explore"></v-btn>
            </v-card-actions>
        </v-card>
        </v-col>
    </v-container>
</template>