import type { ReactNode } from "react";
import { z } from "zod";

// =============================================================================
// Zod Schemas
// =============================================================================

export const registerSchema = z.object({
  name: z.string().min(1, "Name is required"),
  email: z.string().email("Must be valid email format"),
  username: z.string().min(1, "Username is required"),
  password: z.string().min(6, "Password must be at least 6 characters"),
});

export const loginSchema = z.object({
  username: z.string().min(1, "Username is required"),
  password: z.string().min(1, "Password is required"),
});

// =============================================================================
// Inferred Types from Schemas
// =============================================================================

export type RegisterFormData = z.infer<typeof registerSchema>;
export type LoginFormData = z.infer<typeof loginSchema>;

// =============================================================================
// Domain Types
// =============================================================================

export type RoleType = {
  id: number;
  name: string;
};

export type UserType = {
  name: string;
  email: string;
  username: string;
  roles: RoleType[];
};

// =============================================================================
// Context Types
// =============================================================================

export type AuthContextType = {
  user: UserType | null;
  register: (formData: RegisterFormData) => Promise<void>;
  login: (formData: LoginFormData) => Promise<void>;
  verify: () => Promise<void>;
  logout: () => Promise<void>;
  clearAuth: () => void;
  isRegistering: boolean;
  isLoggingIn: boolean;
  isVerifying: boolean;
};

export type AuthProviderPropsType = {
  children: ReactNode;
};

// =============================================================================
// Prop Types
// =============================================================================

export type AuthMenuProps = {
  mode: "login" | "register";
  onClose: () => void;
};
