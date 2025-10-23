import { createContext } from "react";
import type { UserContextType } from "../types/UserTypes";

export const UserContext = createContext<UserContextType>({
  user: null,
  getCurrentUser: async () => {},
  updateCurrentUser: async () => {},
  findById: async () => {},
  deleteById: async () => {},
  isGettingCurrentUser: false,
  isUpdatingCurrentUser: false,
  isFindingById: false,
  isDeleting: false,
});
