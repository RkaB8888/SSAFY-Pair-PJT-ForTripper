<template>
  <v-row no-gutters>
    <!-- 좌측 날짜 버튼 -->
    <v-col cols="2" class="pa-0">
      <v-list dense class="h-100">
        <v-list-item
          v-for="(schedule, index) in dateWithSchedules"
          :key="index"
          class="mb-1 d-flex justify-center align-center date-button"
          :class="{ 'selected-button': selectedDate === schedule.date }"
          @click="
            $emit('select-date', schedule.date);
            $emit('update-map-center', schedule.places[0]?.location);
          "
        >
          <v-list-item-title class="text-center"
            >{{ index + 1 }}일</v-list-item-title
          >
        </v-list-item>
      </v-list>
    </v-col>

    <!-- 우측 리스트 -->
    <v-col cols="9" class="pa-0">
      <v-card flat>
        <v-card-title>
          <span>{{ selectedDate || "날짜를 선택하세요" }}</span>
          <!-- 수정 버튼 추가 -->
          <router-link
            :to="{
              name: 'PlanDetailEdit',
              params: { plan_id: plan_id },
            }"
          >
            <v-btn>수정</v-btn>
          </router-link>
        </v-card-title>
        <v-card-text>
          <template
            v-if="selectedSchedule && selectedSchedule.places.length > 0"
          >
            <div>
              <v-list-item
                v-for="place in selectedSchedule.places"
                :key="place.id"
                class="rounded border p-2 d-flex justify-between align-center"
              >
                <v-list-item-title>{{ place.displayName }}</v-list-item-title>
              </v-list-item>
            </div>
          </template>
          <v-alert v-else type="info" text> 장소가 없습니다. </v-alert>
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</template>

<script setup>
import { defineProps, defineEmits, computed } from "vue";
import { usePlanStore } from "@/stores/plan";

const props = defineProps(["dateWithSchedules", "selectedDate", "plan_id"]);
const emit = defineEmits(["save", "select-date", "remove-place"]);
const planStore = usePlanStore();

const selectedSchedule = computed(() =>
  props.dateWithSchedules.find(
    (schedule) => schedule.date === props.selectedDate
  )
);

// 저장 함수
const storeSavePlan = async (plan) => {
  try {
    await planStore.savePlanToLocalStorage(plan);
  } catch (error) {
    console.error("에러: ", error);
  }
};
</script>

<style scoped>
.selected-button {
  background-color: lightblue;
  font-weight: bold;
  border-radius: 4px;
}

.v-list-item {
  cursor: pointer;
  padding: 8px;
  transition: background-color 0.2s;
}

.v-list-item:hover {
  background-color: aliceblue;
}

.date-button {
  width: 100%;
  height: 60px;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f0f0;
  border-radius: 10px;
  font-size: 16px;
}

.date-button.selected-button {
  background-color: lightblue;
}

.v-list {
  padding: 0;
}

.v-row.no-gutters {
  margin: 0;
  padding: 0;
}

.v-col.pa-0 {
  padding: 0 !important;
}

.v-card-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.v-btn[disabled] {
  opacity: 0.5;
}

.v-list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.delete-button {
  padding: 0;
  min-width: auto;
  height: 15px;
  width: 15px;
  border-radius: 50%;
}

.delete-button .v-icon {
  font-size: 20px;
}
</style>
