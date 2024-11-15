import axios from "axios";

const authApi = axios.create({
  baseURL: "http://localhost:9000/",
});

export default authApi;
