<template>
  <v-dialog v-model="dialog" max-width="500px" max-height="80vh">
    <template v-slot:activator="{ props }">
      <v-btn
        color="purple"
        prepend-icon="mdi-pencil"
        text="새 여행 플랜 생성하기"
        variant="outlined"
        v-bind="props"
      ></v-btn>
    </template>
    <v-card v-card style="overflow-y: auto">
      <v-card-title>
        <span class="text-h5">새 여행 계획</span>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row>
            <v-col cols="12">
              <v-label class="mb-2">날짜 선택</v-label>
              <VueDatePicker
                v-model="date"
                locale="ko"
                range
                :enable-time-picker="false"
                auto-grow
              />
              <v-col cols="20"></v-col>
              <v-label class="mb-2">플랜 이름</v-label>
              <v-text-field
                v-model="planForm.plan_title"
                label="여행 플랜 타이틀"
                required
              ></v-text-field>
            </v-col>

            <v-col cols="12">
              <v-label class="mb-2">플랜 설명</v-label>
              <v-textarea
                v-model="planForm.description"
                label="설명"
                required
              ></v-textarea>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue-darken-1" variant="text" @click="dialog = false">
          취소
        </v-btn>
        <v-btn color="blue-darken-1" variant="text" @click="add"> 생성 </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref } from "vue";
import { usePlanStore } from "@/stores/plan";
import { useRouter } from "vue-router";
import VueDatePicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";

const planStore = usePlanStore();
const router = useRouter();
const dialog = ref(false);
const emit = defineEmits(["plan-created"]);

const planForm = ref({
  plan_title: "",
  start_date: "",
  end_date: "",
  description: "",
});

const date = ref();

const add = async () => {
  try {
    const start = new Date(date.value[0]);
    const end = new Date(date.value[1]);
    planForm.value.start_date = start.toISOString().split("T")[0];
    planForm.value.end_date = end.toISOString().split("T")[0];

    await planStore.addPlan(planForm.value);
    dialog.value = false;
    emit("plan-created");
  } catch (error) {
    console.error("에러: ", error);
  }
};
</script>
