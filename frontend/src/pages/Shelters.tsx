import { useEffect } from "react";
import { ShelterList } from "../components/ShelterList";
import { useShelter } from "../hooks/useShelter";
import { ShelterHeader } from "../components/ShelterHeader";

export const Shelters = () => {
  const { shelters, isFindingAll, findAll } = useShelter();

  useEffect(() => {
    findAll();
  }, []);

  return (
    <section className="h-full p-4">
      <ShelterHeader />
      <ShelterList shelters={shelters} isFindingAll={isFindingAll} />
    </section>
  );
};
