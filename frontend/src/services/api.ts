import axios from "axios";

export const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_BACKEND_URL,
  withCredentials: true,
});

let getToken: (() => string | null) | null = null;

export const setTokenGetter = (tokenGetter: () => string | null) => {
  getToken = tokenGetter;
};

axiosInstance.interceptors.request.use((config) => {
  const token = getToken ? getToken() : localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Clear auth state
      localStorage.removeItem("token");

      // Only redirect if it's not the verify endpoint (to avoid infinite loops)
      const url = error.config?.url || "";
      if (!url.includes("/auth/verify")) {
        window.location.href = "/";
      }
    }
    return Promise.reject(error);
  }
);
