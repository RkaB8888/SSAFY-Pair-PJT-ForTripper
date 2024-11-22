import { createApp } from "vue";
import App from "./App.vue";
import { createPinia } from "pinia";
import "@/assets/styles/global.css"; // 전역 스타일 파일 추가
import "@mdi/font/css/materialdesignicons.css"; // MDI 아이콘 CSS 추가

// Vuetify 관련 추가
import { createVuetify } from "vuetify";
import "vuetify/styles";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

import router from "./router";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";

const vuetify = createVuetify({
  components,
  directives,
});

const app = createApp(App);

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
app.use(pinia);
app.use(vuetify);
app.use(router);

app.mount("#app");
