import { useEffect } from "react";
import { ShelterList } from "../components/shelter/ShelterList";
import { useShelter } from "../hooks/useShelter";
import { ShelterHeader } from "../components/shelter/ShelterHeader";

export const Shelters = () => {
  const { shelters, isFindingAll, findAll } = useShelter();

  useEffect(() => {
    findAll();
  }, []);

  return (
    <section>
      <ShelterHeader />
      <ShelterList shelters={shelters} isFindingAll={isFindingAll} />
    </section>
  );
};
