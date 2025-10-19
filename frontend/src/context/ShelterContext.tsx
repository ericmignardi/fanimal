import { createContext, useState } from "react";
import {
  type ShelterType,
  type ShelterContextType,
  type ShelterProviderPropsType,
  type UpdateFormType,
  type CreateFormType,
} from "../types/ShelterTypes";
import toast from "react-hot-toast";
import { axiosInstance } from "../services/api";

export const ShelterContext = createContext<ShelterContextType>({
  shelters: null,
  shelter: null,
  create: async () => {},
  findAll: async () => {},
  findById: async () => {},
  update: async () => {},
  deleteById: async () => {},
  isCreating: false,
  isFindingAll: false,
  isFindingById: false,
  isUpdating: false,
  isDeleting: false,
});

export function ShelterProvider({ children }: ShelterProviderPropsType) {
  const [shelters, setShelters] = useState<ShelterType[] | null>(null);
  const [shelter, setShelter] = useState<ShelterType | null>(null);
  const [isCreating, setIsCreating] = useState(false);
  const [isFindingAll, setIsFindingAll] = useState(false);
  const [isFindingById, setIsFindingById] = useState(false);
  const [isUpdating, setIsUpdating] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);

  const findAll = async () => {
    setIsFindingAll(true);
    try {
      const response = await axiosInstance.get("/shelters");
      if (response.status === 200) {
        setShelters(response.data);
        toast.success("Find all successful!");
      } else {
        toast.error("Find all failed.");
      }
    } catch (error) {
      console.error("Error in findAll:", error);
      toast.error(
        error instanceof Error
          ? `Unable to find all: ${error.message}`
          : "Unable to find all"
      );
    } finally {
      setIsFindingAll(false);
    }
  };

  const findById = async (id: number) => {
    setIsFindingById(true);
    try {
      const response = await axiosInstance.get(`/shelters/${id}`);
      if (response.status === 200) {
        setShelter(response.data);
        toast.success("Find by ID successful!");
      } else {
        toast.error("Find by ID failed.");
      }
    } catch (error) {
      console.error("Error in findById:", error);
      toast.error(
        error instanceof Error
          ? `Unable to find by ID: ${error.message}`
          : "Unable to find by ID"
      );
    } finally {
      setIsFindingById(false);
    }
  };

  const create = async (formData: CreateFormType) => {
    setIsCreating(true);
    try {
      const response = await axiosInstance.post("/shelters", formData);
      if (response.status === 201) {
        setShelters((prev) =>
          prev ? [...prev, response.data] : [response.data]
        );
        toast.success("Create successful!");
      } else {
        toast.error("Create failed.");
      }
    } catch (error) {
      console.error("Error in create:", error);
      toast.error(
        error instanceof Error
          ? `Unable to create: ${error.message}`
          : "Unable to create"
      );
    } finally {
      setIsCreating(false);
    }
  };

  const update = async (id: number, formData: UpdateFormType) => {
    setIsUpdating(true);
    try {
      const response = await axiosInstance.put(`/shelters/${id}`, formData);
      if (response.status === 200) {
        setShelters(
          (prev) =>
            prev?.map((s) => (s.id === response.data.id ? response.data : s)) ??
            null
        );
        toast.success("Update successful!");
      } else {
        toast.error("Update failed.");
      }
    } catch (error) {
      console.error("Error in update:", error);
      toast.error("Unable to update shelter.");
    } finally {
      setIsUpdating(false);
    }
  };

  const deleteById = async (id: number) => {
    setIsDeleting(true);
    try {
      const response = await axiosInstance.delete(`/shelters/${id}`);
      if (response.status === 204) {
        setShelters((prev) => prev?.filter((s) => s.id !== id) ?? null);
        toast.success("Delete successful!");
      } else {
        toast.error("Delete failed.");
      }
    } catch (error) {
      console.error("Error in delete:", error);
      toast.error("Unable to delete shelter.");
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <ShelterContext.Provider
      value={{
        shelters,
        shelter,
        findAll,
        findById,
        create,
        update,
        deleteById,
        isFindingAll,
        isFindingById,
        isCreating,
        isUpdating,
        isDeleting,
      }}
    >
      {children}
    </ShelterContext.Provider>
  );
}
