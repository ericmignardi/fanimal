import hero from "../../assets/hero.jpg";
import { useNavigate } from "react-router-dom";

export const Hero = () => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate("/shelters");
  };

  return (
    <div className="flex flex-col gap-y-6">
      <div className="flex flex-col items-start gap-y-3">
        <h1 className="text-4xl sm:text-5xl md:text-6xl lg:text-6xl font-extrabold leading-tight">
          Support Your <br />
          <span className="text-[var(--color-primary)]">favourite</span>
          <br />
          animal shelters
        </h1>
        <p className="text-base sm:text-lg md:text-xl font-normal leading-relaxed max-w-lg">
          Join fanimal and directly support animal shelters and humane
          societies. Subscribe to your favourite shelters at different tier
          levels to help them continue their vital work.
        </p>
        <button
          onClick={handleClick}
          className="btn-secondary text-base font-semibold"
        >
          Explore
        </button>
      </div>
      <div>
        <img
          className="w-full rounded-xl"
          src={hero}
          alt="Woman facing upwards holding a white and gray cat"
        />
      </div>
    </div>
  );
};
