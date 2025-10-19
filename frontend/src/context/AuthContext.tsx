import { createContext, useState } from "react";
import { axiosInstance } from "../services/api.ts";
import toast from "react-hot-toast";
import type {
  AuthContextType,
  RegisterFormType,
  LoginFormType,
  AuthProviderPropsType,
  UserType,
} from "../types/AuthTypes.ts";

export const AuthContext = createContext<AuthContextType>({
  user: null,
  register: async () => {},
  login: async () => {},
  verify: async () => {},
  logout: async () => {},
  isRegistering: false,
  isLoggingIn: false,
  isVerifying: false,
});

export function AuthProvider({ children }: AuthProviderPropsType) {
  const [user, setUser] = useState<UserType | null>(null);
  const [isRegistering, setIsRegistering] = useState(false);
  const [isLoggingIn, setIsLoggingIn] = useState(false);
  const [isVerifying, setIsVerifying] = useState(false);

  const register = async (formData: RegisterFormType) => {
    setIsRegistering(true);
    try {
      const response = await axiosInstance.post<{ user: UserType }>(
        "/auth/register",
        formData
      );
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
      const response = await axiosInstance.post<{ user: UserType }>(
        "/auth/login",
        formData
      );
      if (response.status === 200) {
        setUser(response.data.user);
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
        setUser(response.data.user);
        toast.success("Verify successful!");
      } else {
        toast.error("Verify failed.");
      }
    } catch (error: unknown) {
      if (error instanceof Error) {
        toast.error(`Unable to verify: ${error.message}`);
      } else {
        toast.error("Unable to verify");
      }
    } finally {
      setIsVerifying(false);
    }
  };

  const logout = async () => {
    try {
      const response = await axiosInstance.post("/auth/logout");
      if (response.status === 200) {
        setUser(null);
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
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        register,
        login,
        verify,
        logout,
        isRegistering,
        isLoggingIn,
        isVerifying,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}
