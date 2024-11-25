<script setup>
import { ref, watch, computed } from "vue";
import { GoogleMap, Marker, Polyline } from "vue3-google-map";

const apiKey = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY;
const center = ref({ lat: 37.555946, lng: 126.97078780000001 });
const zoom = ref(16);
const selectedPlace = ref(null);

const props = defineProps({
  placesForSelectedDate: {
    type: Array,
    default: () => [],
  },
});

const emit = defineEmits(["select-place"]);

// 중심 이동 기능
const updateMapCenter = (location) => {
  if (location?.lat && location?.lng) {
    center.value = { lat: location.lat, lng: location.lng };
    zoom.value = 14; // 적절한 줌 레벨
  }
};

// 장소 선택 시 중심 이동 및 확대
const zoomToMarker = (place) => {
  updateMapCenter(place?.location);
  zoom.value = 18;
  selectedPlace.value = place;
};

// Prop 변경 감지
watch(
  () => props.placesForSelectedDate,
  (newPlaces) => {
    if (newPlaces.length) {
      updateMapCenter(newPlaces[0]?.location); // 첫 번째 장소로 이동
    }
  },
  { immediate: true }
);
</script>

<template>
  <GoogleMap
    :api-key="apiKey"
    :center="center"
    :zoom="zoom"
    style="height: 100vh; width: 100%"
  >
    <Marker
      v-for="(place, index) in props.placesForSelectedDate"
      :key="place.id"
      :options="{
        position: {
          lat: place.location?.lat ?? 0,
          lng: place.location?.lng ?? 0,
        },
        label: (index + 1).toString(),
        title: place.displayName ?? '',
      }"
      @click="zoomToMarker(place)"
    />
    <Polyline
      :options="{ path: props.placesForSelectedDate.map((p) => p.location) }"
    />
  </GoogleMap>
</template>

<style scoped>
.google-map-container {
  position: relative;
  height: 100vh;
  width: 100%;
}

.marker-label {
  position: absolute;
  top: -20px;
  left: -10px;
  background-color: white;
  border-radius: 50%;
  padding: 5px;
  font-size: 14px;
  font-weight: bold;
  color: black;
}

.place-details-overlay {
  position: absolute;
  bottom: 10px;
  right: 10px;
  z-index: 1;
}
</style>
