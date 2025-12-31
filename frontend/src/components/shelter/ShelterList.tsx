import { Loader } from "lucide-react";
import { ShelterCard } from "./ShelterCard";
import type {
  ShelterType,
  ShelterListPropsType,
} from "../../types/ShelterTypes";

export const ShelterList = ({
  shelters,
  isFindingAll,
}: ShelterListPropsType) => {
  if (isFindingAll)
    return (
      <div className="flex h-24 items-center justify-center">
        <Loader className="h-8 w-8 animate-spin" />
      </div>
    );

  if (!shelters || shelters.length === 0) return <p>No shelters found.</p>;

  return (
    <div className="grid grid-cols-1 items-center justify-center gap-4 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
      {shelters.map((shelter: ShelterType) => (
        <ShelterCard key={shelter.id} shelter={shelter} />
      ))}
    </div>
  );
};
