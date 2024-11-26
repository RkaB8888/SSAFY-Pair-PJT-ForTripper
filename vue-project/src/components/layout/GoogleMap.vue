<template>
  <div class="google-map-container">
    <GoogleMap
      :api-key="apiKey"
      :center="center"
      :zoom="zoom"
      style="height: 100vh; width: 100%"
    >
      <Marker
        v-for="place in visiblePlaces"
        :key="place?.id"
        :options="{
          position: {
            lat: place?.location?.lat?.() ?? 0,
            lng: place?.location?.lng?.() ?? 0,
          },
          clickable: true,
          title: place?.displayName ?? '',
        }"
        :icon="
          selectedPlace?.id === place?.id
            ? 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
            : 'http://maps.google.com/mapfiles/ms/icons/red-dot.png'
        "
        @click="showPlaceDetails(place)"
      />
    </GoogleMap>

    <v-card class="search-overlay" :style="{ width: '600px' }">
      <v-card-text>
        <v-text-field
          v-model="searchQuery"
          label="검색어를 입력하세요"
          append-icon="mdi-magnify"
          @click:append="findPlaces(searchQuery)"
          @keyup.enter="findPlaces(searchQuery)"
        ></v-text-field>
      </v-card-text>
    </v-card>

    <v-card
      v-if="places.length > 0"
      class="places-list-overlay ma-4"
      :style="{ height: 'calc(100vh - 100px)', width: '300px' }"
    >
      <v-list style="height: 100%; overflow-y: auto" class="scrollbar-hidden">
        <v-list-item @click="clearSelection">
          <v-list-item-title>모든 결과 보기</v-list-item-title>
        </v-list-item>
        <v-list-item
          v-for="place in places"
          :key="place?.id"
          :class="{ 'v-list-item--active': selectedPlace?.id === place?.id }"
          @click="showPlaceDetails(place)"
        >
          <v-list-item-title>{{
            place?.displayName ?? "Unknown Place"
          }}</v-list-item-title>
          <template v-slot:append>
            <v-btn
              icon
              @click.stop="$emit('select-place', place)"
              style="width: 24px; height: 24px"
            >
              <v-icon size="small">mdi-plus</v-icon>
            </v-btn>
          </template>
        </v-list-item>
      </v-list>
    </v-card>

    <v-card
      v-if="selectedPlace"
      class="place-details-overlay ma-4"
      max-width="400"
    >
      <v-card-title>{{ selectedPlace.displayName }}</v-card-title>
      <v-card-text>
        <p>주소: {{ selectedPlace.formattedAddress }}</p>
        <p>전화번호: {{ selectedPlace.internationalPhoneNumber }}</p>
        <v-carousel
          v-if="selectedPlace.photos && selectedPlace.photos.length > 0"
          height="200"
        >
          <v-carousel-item
            v-for="(photo, index) in selectedPlace.photos"
            :key="index"
            :src="photo.getURI({ maxHeight: 200 })"
            cover
          ></v-carousel-item>
        </v-carousel>
      </v-card-text>
    </v-card>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { GoogleMap, Marker } from "vue3-google-map";

const apiKey = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY;
const center = ref({ lat: 37.555946, lng: 126.97078780000001 });
const zoom = ref(16);
const places = ref([]);
const searchQuery = ref("");
const selectedPlace = ref(null);

const visiblePlaces = computed(() => {
  if (selectedPlace.value) {
    return [selectedPlace.value];
  } else {
    return places.value.filter((place) => place && place.location);
  }
});

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
    selectedPlace.value = null;

    const bounds = new google.maps.LatLngBounds();
    searchResults.forEach((place) => {
      bounds.extend(place.location);
    });
    center.value = bounds.getCenter().toJSON();
    zoom.value = 12;
  } else {
    console.log("검색 결과가 없습니다.");
    places.value = [];
  }
};

const showPlaceDetails = (place) => {
  if (!place) return;
  selectedPlace.value = place;
  center.value = {
    lat: place.location?.lat?.() ?? 37.555946,
    lng: place.location?.lng?.() ?? 126.97078780000001,
  };
  zoom.value = 18; // 줌 레벨을 더 높게 설정
};

const clearSelection = () => {
  selectedPlace.value = null;
  if (places.value.length > 0) {
    const bounds = new google.maps.LatLngBounds();
    places.value.forEach((place) => {
      if (place.location) {
        bounds.extend(place.location);
      }
    });
    center.value = bounds.getCenter().toJSON();
    zoom.value = 12;
  }
};
</script>

<style scoped>
.google-map-container {
  position: relative;
  height: 100vh;
  width: 100%;
}

.search-overlay {
  position: absolute;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1;
}

.places-list-overlay {
  position: absolute;
  top: 80px;
  left: 10px;
  bottom: 10px;
  z-index: 1;
}

.place-details-overlay {
  position: absolute;
  bottom: 10px;
  right: 10px;
  z-index: 1;
}

.scrollbar-hidden::-webkit-scrollbar {
  display: none;
}

.scrollbar-hidden {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>
