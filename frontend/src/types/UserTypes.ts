import type { ReactNode } from "react";
import { type UserType } from "./AuthTypes";

export type UserContextType = {
  user: UserType | null;
  getCurrentUser: () => Promise<void>;
  updateCurrentUser: (formData: UpdateFormType) => Promise<void>;
  findById: (id: number) => Promise<void>;
  deleteById: (id: number) => Promise<void>;
  isGettingCurrentUser: boolean;
  isUpdatingCurrentUser: boolean;
  isFindingById: boolean;
  isDeleting: boolean;
};

export type UpdateFormType = {
  name: string;
};

export type UserProviderPropsType = {
  children: ReactNode;
};

export type ProfileCardProps = {
  user: UserType | null;
  updateCurrentUser: (formData: UpdateFormType) => Promise<void>;
  isUpdatingCurrentUser: boolean;
};
