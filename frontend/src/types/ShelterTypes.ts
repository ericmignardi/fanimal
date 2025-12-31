import type { ReactNode } from "react";
import type { UserType } from "./AuthTypes";
import type { LucideIcon } from "lucide-react";

export type ShelterType = {
  id: number;
  name: string;
  description: string;
  address: string;
  owner: UserType;
};

export type CreateFormType = {
  name: string;
  description: string;
  address: string;
};

export type UpdateFormType = {
  name: string;
  description: string;
  address: string;
};

export type ShelterContextType = {
  shelters: ShelterType[] | null;
  shelter: ShelterType | null;
  create: (formData: CreateFormType) => Promise<void>;
  findAll: () => Promise<void>;
  findById: (id: number) => Promise<void>;
  update: (id: number, formData: UpdateFormType) => Promise<void>;
  deleteById: (id: number) => Promise<void>;
  isCreating: boolean;
  isFindingAll: boolean;
  isFindingById: boolean;
  isUpdating: boolean;
  isDeleting: boolean;
};

export type ShelterProviderPropsType = {
  children: ReactNode;
};

export type ShelterListPropsType = {
  shelters: ShelterType[] | null;
  isFindingAll: boolean;
};

export type ShelterCardType = {
  verified: boolean;
  image: LucideIcon;
  imageBackground: string;
  title: string;
  badge: string | null;
  description: string;
  tags: string[];
  supporters: number;
};
