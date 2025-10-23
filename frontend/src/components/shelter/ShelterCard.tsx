import type { ShelterType } from "../../types/ShelterTypes";
import { useNavigate } from "react-router-dom";

export const ShelterCard = ({ shelter }: { shelter: ShelterType }) => {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col justify-center gap-4 p-4 shadow-md rounded-xl">
      <img src="https://placehold.co/400x300" alt={`${shelter.name}`} />
      <div>
        <h2 className="text-sm sm:text-base md:text-lg lg:text-xl font-semibold">
          {shelter.name}
        </h2>
        <p className="text-xs sm:text-sm md:text-base text-[var(--color-text)]/50">
          {shelter.description}
        </p>
      </div>
      <div className="flex items-center gap-2">
        <button
          onClick={() => navigate(`/shelters/${shelter.id}`)}
          className="btn-primary"
        >
          View Details
        </button>
        <button className="btn-secondary">Subscribe</button>
      </div>
    </div>
  );
};
