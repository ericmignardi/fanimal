import { useContext } from "react";
import { ShelterContext } from "../context/ShelterContext";
import type { ShelterContextType } from "../types/ShelterTypes";

export const useShelter = (): ShelterContextType => {
  const context = useContext(ShelterContext);
  if (!context) {
    throw new Error("useShelter must be used within an ShelterProvider");
  }
  return context;
};
