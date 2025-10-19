import { useContext } from "react";
import { AuthContext } from "../context/AuthContext.tsx";
import type { AuthContextType } from "../types/AuthTypes.ts";

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
