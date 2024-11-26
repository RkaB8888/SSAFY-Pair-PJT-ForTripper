import { ref } from "vue";
import { defineStore } from "pinia";
import planApi from "@/api/planApi";
import shareApi from "@/api/shareApi";

export const usePlanStore = defineStore("plan", () => {
  //여행 계획 목록
  const plans = ref([]); //GET 요청을 통해 Plan List 데이터 저장
  //특정 여행 계획
  const plan = ref(null);
  //shareBoard 게시글 목록
  const sharePosts = ref([]);

  //여행 계획 추가
  const addPlan = async (planInfo) => {
    try {
      console.log(planInfo);
      // Authorization 헤더 추가
      const response = await planApi.post("/add", planInfo, {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error("Plan Add 오류 발생: ", error);
      throw error;
    }
  };

  //로컬 스토리지에 Plan 저장
  const savePlanToLocalStorage = async (plan) => {
    try {
      console.log("스토리지에 Plan 저장!");
      const data = {
        plan,
        timestamp: Date.now(),
      };
      localStorage.setItem("currentPlan", JSON.stringify(data));
    } catch (error) {
      console.error("savePlanToLocalStorage 오류 발생: ", error);
      throw error;
    }
  };

  //서버에서 Plan 데이터 전부 불러오기
  const fetchPlans = async () => {
    try {
      console.log("데이터 불러오기익");
      console.log("토큰", sessionStorage.getItem("accessToken"));
      const response = await planApi.get("/", {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
        },
      });
      plans.value = response.data;
    } catch (error) {
      console.log("데이터 불러오기 실패!!!!!!!!!: ", error);
    }
  };

  //서버에서 날짜별 방문 장소 가져오기
  const fetchVisitPlaceByDate = async (plan_id) => {
    try {
      const response = await planApi.get(`/${plan_id}`, {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error("날짜별 일정 불러오기 실패: ", error);
      throw error;
    }
  };

  //서버에 장소 저장하기
  const saveDailySchedules = async (dailySchedules, plan_id) => {
    try {
      console.log("Sending data:", JSON.stringify(dailySchedules.value));
      console.log("Plan ID:", plan_id);
      console.log("아니 왜안돼");
      const response = await planApi.post(
        `/${plan_id}/edit`,
        {
          dailySchedules: dailySchedules.value,
        },
        {
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
            "Content-Type": "application/json",
          },
        }
      );
      console.log("Server response:", response.data);
      return response.data;
    } catch (error) {
      console.error("서버 저장 실패:", error);
      if (error.response) {
        console.error("Response data:", error.response.data);
        console.error("Response status:", error.response.status);
      }
      throw error;
    }
  };

  const addSharePlan = async (formData, dailySchedules, totalDate, plan_id) => {
    try {
      console.log("플랜 아이디", plan_id);
      const postData = new FormData();
      postData.append("title", formData.get("title"));
      postData.append("content", formData.get("content"));
      if (formData.get("image")) {
        postData.append("image", formData.get("image"));
      }
      postData.append("dailySchedules", JSON.stringify(dailySchedules));
      postData.append("totalDate", totalDate);

      const response = await shareApi.post(`/add/${plan_id}`, postData, {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
          "Content-Type": "multipart/form-data",
        },
      });

      console.log("서버 응답:", response.data);
      return response;
    } catch (error) {
      console.error("서버 저장 실패:", error.response || error);
      throw error;
    }
  };

  const fetchShareBoard = async () => {
    try {
      console.log("ShareBoard 데이터 로드 시작");
      const response = await shareApi.get("/");
      sharePosts.value = response.data;
      console.log(sharePosts.value);
    } catch (error) {
      console.log("ShareBoard 데이터 로드 실패", error);
    }
  };

  //서버에서 날짜별 방문 장소 가져오기
  const fetchShareVisitPlaceByDate = async (plan_id) => {
    try {
      const response = await shareApi.get(`/${plan_id}`, {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error("날짜별 일정 불러오기 실패: ", error);
      throw error;
    }
  };

  const saveSharePlan = async (plan_id, planInfo) => {
    try {
      console.log("plan_id는 ", plan_id);
      console.log("planInfo는 ", planInfo);
      const response = await shareApi.post(`/share/${plan_id}`, planInfo, {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
        },
      });
      console.log("서버 응답", response.data);
      return response;
    } catch (error) {
      console.error("share plan 저장 실패: ", error);
      throw error;
    }
  };

  return {
    addPlan,
    fetchPlans,
    savePlanToLocalStorage,
    fetchVisitPlaceByDate,
    saveDailySchedules,
    addSharePlan,
    fetchShareBoard,
    fetchShareVisitPlaceByDate,
    saveSharePlan,
    plans,
    plan,
    sharePosts,
  };
});
