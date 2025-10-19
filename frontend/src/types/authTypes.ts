import type { ReactNode } from "react";

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

export type AuthContextType = {
  user: UserType | null;
  register: (formData: RegisterFormType) => Promise<void>;
  login: (formData: LoginFormType) => Promise<void>;
  verify: () => Promise<void>;
  logout: () => Promise<void>;
  isRegistering: boolean;
  isLoggingIn: boolean;
  isVerifying: boolean;
};

export type AuthProviderPropsType = {
  children: ReactNode;
};

export type RegisterFormType = {
  name: string;
  email: string;
  username: string;
  password: string;
};

export type LoginFormType = {
  username: string;
  password: string;
};
