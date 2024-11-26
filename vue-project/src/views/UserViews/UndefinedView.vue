<script setup></script>

<template>
  <v-card class="undefined-view-card">
    <v-card-text class="text-center">
      <v-icon size="64" color="grey">mdi-lock</v-icon>
      <h2>비공개된 페이지입니다.</h2>
      <p>이 유저의 여행 계획과 친구 목록은 비공개되어 있습니다.</p>
    </v-card-text>
  </v-card>
</template>

<style scoped>
.undefined-view-card {
  padding: 40px;
  margin-top: 20px;
}

.undefined-view-card h2 {
  margin-top: 20px;
  color: #333;
}

.undefined-view-card p {
  color: #666;
}
</style>
