<script setup>
import { ref } from "vue";
import { GoogleMap, Marker } from "vue3-google-map";

const apiKey = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY;
const center = ref({ lat: 37.555946, lng: 126.97078780000001 });
const zoom = ref(16);
const places = ref([]);
const searchQuery = ref("");
const selectedPlace = ref(null); // 선택된 장소를 추적

// 장소 검색 함수
const findPlaces = async (keyword) => {
  const { Place } = await google.maps.importLibrary("places");
  const request = {
    textQuery: keyword,
    fields: [
      "displayName",
      "location",
      "photos",
      "formattedAddress",
      "internationalPhoneNumber",
    ],
    language: "ko-KR",
  };

  const { places: searchResults } = await Place.searchByText(request);

  if (searchResults.length) {
    places.value = searchResults;

    // 지도 중심을 첫 번째 검색 결과로 이동
    const firstPlace = searchResults[0];
    if (firstPlace?.location) {
      center.value = {
        lat: firstPlace.location.lat(),
        lng: firstPlace.location.lng(),
      };
    }
  } else {
    console.log("검색 결과가 없습니다.");
    places.value = [];
  }
};

// 선택된 장소의 상세 정보를 표시
const showPlaceDetails = (place) => {
  selectedPlace.value = place;

  // 지도 중심을 선택된 장소로 이동
  if (place?.location) {
    center.value = {
      lat: place.location.lat(),
      lng: place.location.lng(),
    };
  }
};
</script>

<template>
  <div class="google-map-container">
    <!-- 검색창 -->
    <div id="searchContainer" class="d-flex justify-content-center my-3">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="검색어를 입력하세요."
      />
      <button @click="findPlaces(searchQuery)">Search</button>
    </div>

    <!-- 지도와 장소 목록 컨테이너 -->
    <div id="mapContainer" class="d-flex">
      <ul class="list-group" id="placesContainer">
        <li
          v-for="(place, index) in places"
          :key="index"
          class="list-group-item"
          :class="{ active: selectedPlace?.id === place.id }"
          @click="showPlaceDetails(place)"
        >
          {{ place.displayName }}
          <button @click="$emit('select-place', place)">추가</button>
        </li>
      </ul>

      <!-- Google Maps -->
      <GoogleMap
        :api-key="apiKey"
        :center="center"
        :zoom="zoom"
        style="height: 80vh; width: 80%"
      >
        <Marker
          v-for="(place, index) in places"
          :key="index"
          :position="{ lat: place.location.lat(), lng: place.location.lng() }"
          :clickable="true"
          :title="place.displayName"
          :icon="
            selectedPlace?.id === place.id
              ? 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
              : 'http://maps.google.com/mapfiles/ms/icons/red-dot.png'
          "
          :options="{
            position: { lat: place.location.lat(), lng: place.location.lng() },
            title: place.displayName,
          }"
          @click="showPlaceDetails(place)"
        />
      </GoogleMap>
    </div>

    <!-- 선택된 장소의 상세 정보 -->
    <div id="placeDetails" v-if="selectedPlace">
      <div v-if="selectedPlace">
        <h2>{{ selectedPlace.displayName }}</h2>
        <p>주소: {{ selectedPlace.formattedAddress }}</p>
        <p>
          위치: {{ selectedPlace.location.lat() }},
          {{ selectedPlace.location.lng() }}
        </p>
        <div v-if="selectedPlace.photos">
          <img
            v-for="(photo, index) in selectedPlace.photos"
            :key="index"
            :src="photo.getURI({ maxHeight: 200 })"
            alt="Place Photo"
            style="margin: 5px; max-width: 100%"
          />
        </div>
      </div>
    </div>
    <div v-else>
      <p>선택된 장소가 없습니다.</p>
    </div>
  </div>
</template>

<style scoped>
#mapContainer {
  display: flex;
  gap: 20px;
}

.list-group-item.active {
  background-color: #007bff;
  color: white;
}

#placeDetails {
  margin-top: 20px;
}

.google-map-container {
  height: 600px; /* 또는 원하는 높이 */
  width: 100%;
}
</style>
