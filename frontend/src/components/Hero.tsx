import type { JSX } from "react";
import hero from "../assets/hero.jpg";

export const Hero = (): JSX.Element => {
  return (
    <div className="flex flex-col gap-16 p-8">
      <div className="flex flex-col items-start gap-4 w-full">
        <h1 className="text-5xl font-bold">
          Support Your <br /> Favourite Animal <br /> Shelters
        </h1>
        <p className="text-lg font-normal">
          Join fanimal and directly support animal shelters and humane
          societies. Subscribe to your favourite shelters at different tier
          levels to help them continue their vital work.
        </p>
        <b className="btn-secondary">Get Started</b>
      </div>
      <div className="w-full">
        <img
          className="rounded-2xl"
          src={hero}
          alt="Women facing upwards holding a white and gray cat"
        />
      </div>
    </div>
  );
};
