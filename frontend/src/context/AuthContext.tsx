import { useState, useEffect } from "react";
import { axiosInstance, setTokenGetter } from "../services/api";
import toast from "react-hot-toast";
import type {
  RegisterFormType,
  LoginFormType,
  AuthProviderPropsType,
  UserType,
} from "../types/AuthTypes";
import { AuthContext } from "./AuthContext";

export function AuthProvider({ children }: AuthProviderPropsType) {
  const [user, setUser] = useState<UserType | null>(null);
  const [isRegistering, setIsRegistering] = useState(false);
  const [isLoggingIn, setIsLoggingIn] = useState(false);
  const [isVerifying, setIsVerifying] = useState(false);

  const [token, setToken] = useState<string | null>(() => {
    return localStorage.getItem("token");
  });

  useEffect(() => {
    setTokenGetter(() => token);
  }, [token]);

  const register = async (formData: RegisterFormType) => {
    setIsRegistering(true);
    try {
      const response = await axiosInstance.post("/auth/register", formData);
      if (response.status === 201) {
        setUser(response.data.user);
        toast.success("Registration successful!");
      } else {
        toast.error("Registration failed.");
      }
    } catch (error: unknown) {
      console.error("Error in register: ", error);
      if (error instanceof Error) {
        toast.error(`Unable to register: ${error.message}`);
      } else {
        toast.error("Unable to register");
      }
    } finally {
      setIsRegistering(false);
    }
  };

  const login = async (formData: LoginFormType) => {
    setIsLoggingIn(true);
    try {
      const response = await axiosInstance.post("/auth/login", formData);
      if (response.status === 200) {
        const { token: newToken, user } = response.data;
        if (newToken) {
          setToken(newToken);
          localStorage.setItem("token", newToken);
        }
        setUser(user);
        toast.success("Login successful!");
      } else {
        toast.error("Login failed.");
      }
    } catch (error: unknown) {
      console.error("Error in login: ", error);
      if (error instanceof Error) {
        toast.error(`Unable to login: ${error.message}`);
      } else {
        toast.error("Unable to login");
      }
    } finally {
      setIsLoggingIn(false);
    }
  };

  const verify = async () => {
    setIsVerifying(true);
    try {
      const response = await axiosInstance.get("/auth/verify");
      if (response.status === 200) {
        const { token: newToken, user } = response.data;
        if (newToken) {
          setToken(newToken);
          localStorage.setItem("token", newToken);
        }
        setUser(user);
      } else {
        setToken(null);
        localStorage.removeItem("token");
        setUser(null);
      }
    } catch {
      setToken(null);
      localStorage.removeItem("token");
      setUser(null);
    } finally {
      setIsVerifying(false);
    }
  };

  const logout = async () => {
    try {
      const response = await axiosInstance.post("/auth/logout");
      if (response.status === 200) {
        toast.success("Logout successful!");
      } else {
        toast.error("Logout failed.");
      }
    } catch (error: unknown) {
      console.error("Error in logout: ", error);
      if (error instanceof Error) {
        toast.error(`Unable to logout: ${error.message}`);
      } else {
        toast.error("Unable to logout");
      }
    } finally {
      // Always clear local auth state, even if backend call fails
      setToken(null);
      localStorage.removeItem("token");
      setUser(null);
    }
  };

  const clearAuth = () => {
    setToken(null);
    localStorage.removeItem("token");
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        register,
        login,
        verify,
        logout,
        clearAuth,
        isRegistering,
        isLoggingIn,
        isVerifying,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}
