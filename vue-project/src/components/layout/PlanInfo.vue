<template>
  <v-card class="mx-auto my-6" max-width="600">
    <v-card-title class="text-h4 font-weight-bold">{{
      plan.plan_title
    }}</v-card-title>
    <v-card-text>
      <v-row>
        <v-col cols="6">
          <p class="text-subtitle-1">시작일: {{ plan.start_date }}</p>
        </v-col>
        <v-col cols="6">
          <p class="text-subtitle-1">종료일: {{ plan.end_date }}</p>
        </v-col>
      </v-row>
      <p class="text-h6">
        {{ plan.total_date - 1 }}박 {{ plan.total_date }}일 여행
      </p>
      <p class="text-body-1">{{ plan.description }}</p>
    </v-card-text>
  </v-card>
</template>

<script setup>
defineProps(["plan"]);
</script>

<style></style>
