import { createContext } from "react";
import type {
  UpdateFormType,
  UserContextType,
  UserProviderPropsType,
} from "../types/UserTypes";
import { useState } from "react";
import { axiosInstance } from "../services/api";
import type { UserType } from "../types/AuthTypes";
import toast from "react-hot-toast";

export const UserContext = createContext<UserContextType>({
  user: null,
  getCurrentUser: async () => {},
  updateCurrentUser: async () => {},
  isGettingCurrentUser: false,
  isUpdatingCurrentUser: false,
});

export function UserProvider({ children }: UserProviderPropsType) {
  const [user, setUser] = useState<UserType | null>(null);
  const [isGettingCurrentUser, setIsGettingCurrentUser] =
    useState<boolean>(false);
  const [isUpdatingCurrentUser, setIsUpdatingCurrentUser] =
    useState<boolean>(false);

  const getCurrentUser = async () => {
    setIsGettingCurrentUser(true);
    try {
      const response = await axiosInstance.get("/users/me");
      if (response.status === 200) {
        setUser(response.data);
        toast.success("Get current user successful!");
      } else {
        toast.error("Get current user failed.");
      }
    } catch (error) {
      console.error("Error in getCurrentUser: ", error);
      if (error instanceof Error) {
        toast.error(`Unable to get current user: ${error.message}`);
      } else {
        toast.error("Unable to get current user");
      }
    } finally {
      setIsGettingCurrentUser(false);
    }
  };

  const updateCurrentUser = async (formData: UpdateFormType) => {
    setIsUpdatingCurrentUser(true);
    try {
      const response = await axiosInstance.put("/users/me", formData);
      if (response.status === 200) {
        setUser(response.data);
        toast.success("Update current user successful!");
      } else {
        toast.error("Update current user failed.");
      }
    } catch (error) {
      console.error("Error in updateCurrentUser: ", error);
      if (error instanceof Error) {
        toast.error(`Unable to update current user: ${error.message}`);
      } else {
        toast.error("Unable to update current user");
      }
    } finally {
      setIsUpdatingCurrentUser(false);
    }
  };

  return (
    <UserContext.Provider
      value={{
        user,
        getCurrentUser,
        updateCurrentUser,
        isGettingCurrentUser,
        isUpdatingCurrentUser,
      }}
    >
      {children}
    </UserContext.Provider>
  );
}
