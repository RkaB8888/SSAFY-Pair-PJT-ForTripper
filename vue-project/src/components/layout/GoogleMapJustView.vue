<script setup>
import { ref, onMounted, watch, computed } from "vue";
import { GoogleMap, Marker, Polyline } from "vue3-google-map";

const apiKey = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY;
const center = ref({ lat: 37.555946, lng: 126.97078780000001 });
const zoom = ref(16);
const placesForSelectedDate = ref([]); // 부모로부터 전달받은 장소 리스트
const selectedPlace = ref(null);
const polyline = ref(null); // Polyline 객체를 저장할 변수

const props = defineProps({
  placesForSelectedDate: {
    type: Array,
    default: () => [],
  },
});

const visiblePlaces = computed(() => props.placesForSelectedDate);

// 장소 리스트가 변경되면 지도 중앙을 갱신
watch(
  placesForSelectedDate,
  (newPlaces) => {
    if (newPlaces.length) {
      const bounds = new google.maps.LatLngBounds();
      newPlaces.forEach((place) => {
        if (place.location) {
          bounds.extend(place.location);
        }
      });
      center.value = bounds.getCenter().toJSON();
      zoom.value = 12;
      // 지도에 경로 그리기
      drawPolyline(newPlaces);
    }
  },
  { immediate: true }
);

// 마커 클릭 시 장소의 상세 정보를 보여줌
const showPlaceDetails = (place) => {
  selectedPlace.value = place;
  center.value = {
    lat: place.location?.lat ?? 37.555946,
    lng: place.location?.lng ?? 126.97078780000001,
  };
  zoom.value = 16;
};

const polylineOptions = computed(() => ({
  path: visiblePlaces.value.map((place) => ({
    lat: place.location?.lat ?? 0,
    lng: place.location?.lng ?? 0,
  })),
  geodesic: true,
  strokeColor: "#FF0000",
  strokeOpacity: 1.0,
  strokeWeight: 2,
}));

const zoomToMarker = (place) => {
  center.value = {
    lat: place.location?.lat ?? 37.555946,
    lng: place.location?.lng ?? 126.97078780000001,
  };
  zoom.value = 18; // 줌 레벨을 더 높게 설정
  selectedPlace.value = place;
};
</script>

<template>
  <div class="google-map-container">
    <GoogleMap
      :api-key="apiKey"
      :center="center"
      :zoom="zoom"
      style="height: 100vh; width: 100%"
      ref="map"
    >
      <Marker
        v-for="(place, index) in visiblePlaces"
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
      >
      </Marker>
      <Polyline :options="polylineOptions" />
    </GoogleMap>

    <v-card
      v-if="selectedPlace"
      class="place-details-overlay ma-4"
      max-width="400"
    >
      <v-card-title>{{ selectedPlace.displayName }}</v-card-title>
      <v-card-text>
        <p>주소: {{ selectedPlace.formattedAddress }}</p>
        <p>
          위치: {{ selectedPlace.location?.lat() }},
          {{ selectedPlace.location?.lng() }}
        </p>
        <v-carousel
          v-if="selectedPlace.photos && selectedPlace.photos.length > 0"
          height="200"
        >
          <v-carousel-item
            v-for="(photo, index) in selectedPlace.photos"
            :key="index"
            :src="photo.getURI({ maxHeight: 200 })"
            cover
          />
        </v-carousel>
      </v-card-text>
    </v-card>
  </div>
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
