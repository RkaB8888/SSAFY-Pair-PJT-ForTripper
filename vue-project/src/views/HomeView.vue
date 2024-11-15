<script setup>
import PopularFestivalView from "@/views/HomeViews/PopularFestivalView.vue";
import RecentBoardListView from "@/views/HomeViews/RecentBoardListView.vue";
import RecentReviewView from "@/views/HomeViews/RecentReviewView.vue";
import RecentFriendsView from "@/views/HomeViews/RecentFriendsView.vue";
</script>

<template>
  <div>
    <h1>홈페이지 화면 입니다.</h1>
    <PopularFestivalView />
    <RecentFriendsView />
    <RecentBoardListView />
    <RecentReviewView />
  </div>
</template>

<style scoped></style>
