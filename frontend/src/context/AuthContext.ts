import { createContext } from "react";
import type { AuthContextType } from "../types/AuthTypes";

export const AuthContext = createContext<AuthContextType>({
  user: null,
  register: async () => {},
  login: async () => {},
  verify: async () => {},
  logout: async () => {},
  clearAuth: () => {},
  isRegistering: false,
  isLoggingIn: false,
  isVerifying: false,
});
