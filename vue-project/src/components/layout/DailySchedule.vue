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
          @click="$emit('select-date', schedule.date)"
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
          {{ selectedDate || "날짜를 선택하세요" }}
          <v-btn
            icon
            color="primary"
            class="ml-auto"
            @click="$emit('save')"
            :disabled="!selectedDate"
          >
            저장
          </v-btn>
        </v-card-title>
        <v-card-text>
          <template
            v-if="selectedSchedule && selectedSchedule.places.length > 0"
          >
            <div
              v-draggable="[
                selectedSchedule.places,
                { animation: 150, ghostClass: 'ghost', onUpdate, onStart },
              ]"
              class="flex flex-col gap-2"
            >
              <v-list-item
                v-for="place in selectedSchedule.places"
                :key="place.id"
                class="rounded border p-2 d-flex justify-between align-center"
              >
                <template v-slot:prepend>
                  <v-icon class="mr-2 cursor-move">mdi-drag</v-icon>
                </template>
                <v-list-item-title>{{ place.displayName }}</v-list-item-title>
                <template v-slot:append>
                  <!-- 삭제 버튼을 오른쪽에 배치 -->
                  <v-icon
                    size="small"
                    @click.stop="$emit('remove-place', selectedDate, place)"
                    >mdi-delete</v-icon
                  >
                </template>
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
import { vDraggable } from "vue-draggable-plus";

const props = defineProps(["dateWithSchedules", "selectedDate"]);
const emit = defineEmits(["save", "select-date", "remove-place"]);

const selectedSchedule = computed(() =>
  props.dateWithSchedules.find(
    (schedule) => schedule.date === props.selectedDate
  )
);

function onStart() {
  console.log("start");
}

function onUpdate() {
  console.log("update");
}
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

/* 좌측 날짜 버튼 스타일 수정 */
.date-button {
  width: 100%;
  height: 70px; /* 정사각형 크기 */
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

/* 우측 리스트와 버튼 간 간격 제거 */
.v-row.no-gutters {
  margin: 0;
  padding: 0;
}

.v-col.pa-0 {
  padding: 0 !important; /* 좌우 여백 제거 */
}

/* 저장 버튼 스타일 */
.v-card-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.v-btn[disabled] {
  opacity: 0.5;
}

/* v-list-item에서 삭제 버튼 오른쪽에 위치 */
.v-list-item {
  display: flex;
  justify-content: space-between; /* 항목과 버튼을 양 끝으로 배치 */
  align-items: center;
}

/* 삭제 버튼 스타일 수정 */
.delete-button {
  padding: 0; /* 버튼의 여백 없애기 */
  min-width: auto; /* 최소 크기 없애기 */
  height: 15px; /* 버튼 높이 설정 */
  width: 15px; /* 버튼 너비 설정 */
  border-radius: 50%; /* 원형 버튼으로 만들기 */
}

.delete-button .v-icon {
  font-size: 20px; /* 아이콘 크기 설정 */
}
</style>
