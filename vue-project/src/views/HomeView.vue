<script setup>
import PopularFestivalsView from "@/views/HomeViews/PopularFestivalsView.vue";
import RecentBoardListView from "@/views/HomeViews/RecentBoardListView.vue";
import RecentReviewsView from "@/views/HomeViews/RecentReviewsView.vue";
import RecentFriendsView from "@/views/HomeViews/RecentFriendsView.vue";
</script>

<template>
  <div>
    <!-- <h1>홈페이지 화면 입니다.</h1> -->
    <PopularFestivalsView />
    <hr />
    <RecentFriendsView />
    <hr />
    <RecentBoardListView />
    <hr />
    <RecentReviewsView />
  </div>
</template>

<style scoped></style>
