import { createApp } from "vue";
import App from "./App.vue";
import { createPinia } from "pinia";
import "@/assets/styles/global.css"; // 전역 스타일 파일 추가
// Vuetify 관련 추가
import { createVuetify } from "vuetify";
import "vuetify/styles";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

import router from "./router";

const vuetify = createVuetify({
  components,
  directives,
});

const app = createApp(App);

app.use(createPinia());
app.use(vuetify);
app.use(router);

app.mount("#app");
