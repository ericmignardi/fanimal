// import { useParams } from "react-router-dom";
// import { useShelter } from "../hooks/useShelter";
import ShelterDetailsHeader from "../components/shelter-details/ShelterDetailsHeader";
import { ShelterDetailsContent } from "../components/shelter-details/ShelterDetailsContent";

export const ShelterDetails = () => {
  // const { id } = useParams<{ id: string }>();
  // const { shelter, findById, isFindingById } = useShelter();

  return (
    <section>
      <ShelterDetailsHeader />
      <ShelterDetailsContent />
    </section>
  );
};
