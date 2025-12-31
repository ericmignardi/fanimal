import { useEffect } from "react";
// import { useShelter } from "../hooks/useShelter";
import { ShelterHeader } from "../components/shelter/ShelterHeader";
import { ShelterContent } from "../components/shelter/ShelterContent";

export const Shelters = () => {
  // const { shelters, isFindingAll, findAll } = useShelter();

  useEffect(() => {
    // findAll();
  }, []);

  return (
    <section>
      <ShelterHeader />
      <ShelterContent />
    </section>
  );
};
