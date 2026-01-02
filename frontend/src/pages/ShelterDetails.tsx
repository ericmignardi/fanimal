import { useParams } from "react-router-dom";
import { useEffect } from "react";
import { useShelter } from "../hooks/useShelter";
import ShelterDetailsHeader from "../components/shelter-details/ShelterDetailsHeader";
import { ShelterDetailsContent } from "../components/shelter-details/ShelterDetailsContent";
import { Loader } from "lucide-react";

export const ShelterDetails = () => {
  const { id } = useParams<{ id: string }>();
  const { shelter, findById, isFindingById } = useShelter();

  useEffect(() => {
    if (id) {
      findById(Number(id));
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  if (isFindingById) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <Loader className="h-8 w-8 animate-spin" />
      </div>
    );
  }

  if (!shelter) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <p>Shelter not found</p>
      </div>
    );
  }

  return (
    <section>
      <ShelterDetailsHeader shelter={shelter} />
      <ShelterDetailsContent shelter={shelter} />
    </section>
  );
};
