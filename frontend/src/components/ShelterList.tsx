import { Loader } from "lucide-react";
import { ShelterCard } from "./ShelterCard";
import type { ShelterType, ShelterListPropsType } from "../types/ShelterTypes";

export const ShelterList = ({
  shelters,
  isFindingAll,
}: ShelterListPropsType) => {
  if (isFindingAll)
    return (
      <div className="flex justify-center items-center h-24">
        <Loader className="animate-spin w-8 h-8" />
      </div>
    );

  if (!shelters || shelters.length === 0) return <p>No shelters found.</p>;

  return (
    <div className="grid grid-rows-[auto] grid-cols-1 md:grid-cols-2 lg:grid-cols-3 justify-center items-center gap-2">
      {shelters.map((shelter: ShelterType) => (
        <ShelterCard key={shelter.id} shelter={shelter} />
      ))}
    </div>
  );
};
