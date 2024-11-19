<script setup></script>

<template>
  <v-container class="auth-container fill-height" fluid>
    <v-row justify="center" align="center"> <RouterView /> </v-row>
  </v-container>
</template>

<style scoped>
.auth-container {
  background-color: #665e5e;
  /* height: 100%; */
  /* padding: 24px; */
}

.auth-card {
  max-width: 800px; /* 카드의 최대 너비 제한 */
}

.signup-section {
  background-color: #f4f4f4;
  padding: 16px;
  border-right: 1px solid #ddd; /* 가운데 분리선 */
}

.login-section {
  background-color: #ffffff;
  padding: 16px;
}
</style>
