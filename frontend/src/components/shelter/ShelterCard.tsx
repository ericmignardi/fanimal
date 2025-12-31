import { ArrowRight } from "lucide-react";
import type { ShelterCardType } from "../../types/ShelterTypes";
import { useNavigate } from "react-router-dom";

export const ShelterCard = ({
  id,
  verified,
  image: Icon,
  imageBackground,
  title,
  badge,
  description,
  tags,
  supporters,
}: ShelterCardType) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/shelters/${id}`);
  };

  return (
    <article
      onClick={handleClick}
      className="relative flex h-full cursor-pointer flex-col border-2 shadow-[4px_4px_0px_0px_#000]"
    >
      {/* Badge */}
      {verified && (
        <div className="absolute top-3 left-3 flex items-center gap-1 border-2 bg-white p-2">
          <span
            className="h-2 w-2 animate-pulse rounded-full bg-green-500"
            aria-hidden="true"
          ></span>
          <span className="text-xs font-bold">Verified</span>
        </div>
      )}

      {/* Image */}
      <div
        className={`flex items-center justify-center ${imageBackground} p-12`}
      >
        <Icon size={130} aria-hidden="true" />
      </div>

      {/* Details */}
      <div className="flex flex-1 flex-col justify-between gap-4 border-t-2 p-6">
        {/* Title */}
        <div className="flex items-center justify-between">
          <h3 className="text-xl font-semibold">{title}</h3>
          {badge && (
            <div className="border bg-blue-200 p-1 text-xs font-semibold">
              {badge}
            </div>
          )}
        </div>
        {/* Description */}
        <p>{description}</p>
        {/* Tags */}
        <div className="flex items-center gap-2 text-xs font-semibold">
          {tags.map((item) => (
            <div key={item} className="border px-2 py-1">
              {item}
            </div>
          ))}
        </div>
        {/* Details */}
        <div className="flex items-center justify-between border-t-2 border-dashed pt-4">
          <span className="text-xs text-gray-800">{supporters}</span>
          <div className="flex items-center gap-1">
            <span className="text-sm font-semibold">View Details</span>
            <ArrowRight aria-hidden="true" />
          </div>
        </div>
      </div>
    </article>
  );
};
