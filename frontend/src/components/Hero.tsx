import hero from "../assets/hero.jpg";

export const Hero = () => {
  return (
    <div className="flex flex-col gap-8 md:gap-16 p-8">
      <div className="flex flex-col items-start gap-4">
        <h1 className="text-4xl sm:text-5xl md:text-6xl lg:text-7xl font-extrabold">
          Support Your <br />
          <span className="text-[var(--color-primary)]">f</span>avourite
          <br />
          <span className="text-[var(--color-primary)]">animal</span> Shelters
        </h1>
        <p className="text-base sm:text-lg md:text-xl font-medium">
          Join fanimal and directly support animal shelters and humane
          societies. Subscribe to your favourite shelters at different tier
          levels to help them continue their vital work.
        </p>
        <button className="btn-secondary">Get Started</button>
      </div>
      <div>
        <img
          className="w-full rounded-2xl"
          src={hero}
          alt="Woman facing upwards holding a white and gray cat"
        />
      </div>
    </div>
  );
};
