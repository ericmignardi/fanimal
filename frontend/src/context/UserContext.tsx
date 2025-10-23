import type { UpdateFormType, UserProviderPropsType } from "../types/UserTypes";
import { useState } from "react";
import { axiosInstance } from "../services/api";
import toast from "react-hot-toast";
import { useAuth } from "../hooks/useAuth";
import { UserContext } from "./UserContext";

export function UserProvider({ children }: UserProviderPropsType) {
  const { user, verify } = useAuth();
  const [isGettingCurrentUser, setIsGettingCurrentUser] =
    useState<boolean>(false);
  const [isUpdatingCurrentUser, setIsUpdatingCurrentUser] =
    useState<boolean>(false);
  const [isFindingById, setIsFindingById] = useState<boolean>(false);
  const [isDeleting, setIsDeleting] = useState<boolean>(false);

  const getCurrentUser = async () => {
    setIsGettingCurrentUser(true);
    try {
      await verify();
      toast.success("Get current user successful!");
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
        await verify();
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

  const findById = async (id: number) => {
    setIsFindingById(true);
    try {
      const response = await axiosInstance.get(`/users/${id}`);
      if (response.status === 200) {
        toast.success("Find by ID successful!");
      } else {
        toast.error("Find by ID failed.");
      }
    } catch (error) {
      console.error("Error in findById: ", error);
      if (error instanceof Error) {
        toast.error(`Unable to find by ID: ${error.message}`);
      } else {
        toast.error("Unable to find by ID");
      }
    } finally {
      setIsFindingById(false);
    }
  };

  const deleteById = async (id: number) => {
    setIsDeleting(true);
    try {
      const response = await axiosInstance.delete(`/users/${id}`);
      if (response.status === 204) {
        await verify();
        toast.success("Delete successful!");
      } else {
        toast.error("Delete failed.");
      }
    } catch (error) {
      console.error("Error in deleteById: ", error);
      if (error instanceof Error) {
        toast.error(`Unable to delete: ${error.message}`);
      } else {
        toast.error("Unable to delete");
      }
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <UserContext.Provider
      value={{
        user,
        getCurrentUser,
        updateCurrentUser,
        findById,
        deleteById,
        isGettingCurrentUser,
        isUpdatingCurrentUser,
        isFindingById,
        isDeleting,
      }}
    >
      {children}
    </UserContext.Provider>
  );
}
