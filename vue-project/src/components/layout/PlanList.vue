<script setup>
import { defineProps, defineEmits } from "vue";
import NewPlanModal from "@/components/layout/NewPlanModal.vue";

const props = defineProps(["plans"]);
const emit = defineEmits(["storeSavePlan", "plan-created"]);

const storeSavePlan = (plan) => {
  emit("storeSavePlan", plan);
};
</script>

<style scoped>
.custom-card {
  background-color: #f4e3fd !important;
  border-color: #aa90f9 !important;
}
</style>

<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <NewPlanModal @plan-created="$emit('plan-created')" />
      </v-col>
    </v-row>
    <v-row>
      <v-col
        v-for="(plan, index) in plans"
        :key="index"
        cols="auto"
        sm="6"
        md="4"
      >
        <v-card
          class="mb-4 custom-card"
          variant="outlined"
          :to="{ name: 'PlanDetail', params: { plan_id: plan.plan_id } }"
          @click="storeSavePlan(plan)"
        >
          <v-card-title>{{ plan.plan_title }}</v-card-title>
          <v-card-text>
            <v-row no-gutters>
              <v-col cols="6">
                <p>출발일: {{ plan.start_date }}</p>
              </v-col>
              <v-col cols="6">
                <p>도착일: {{ plan.end_date }}</p>
              </v-col>
            </v-row>
            <p>{{ plan.total_date - 1 }}박 {{ plan.total_date }}일 여행</p>
            <p>{{ plan.description }}</p>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
