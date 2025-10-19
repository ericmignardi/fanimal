import type { ReactNode } from "react";
import { type UserType } from "./AuthTypes";

export type UserContextType = {
  user: UserType | null;
  getCurrentUser: () => Promise<void>;
  updateCurrentUser: (formData: UpdateFormType) => Promise<void>;
  isGettingCurrentUser: boolean;
  isUpdatingCurrentUser: boolean;
};

export type UpdateFormType = {
  name: string;
};

export type UserProviderPropsType = {
  children: ReactNode;
};
