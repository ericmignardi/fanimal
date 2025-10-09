import type { ReactNode } from "react";
import { AuthForm } from "../components/AuthForm";
import { Hero } from "../components/Hero";

export const Home = (): ReactNode => {
  return (
    <section className="overflow-y-auto h-full grid grid-rows-1 grid-cols-2 justify-center items-center p-4">
      <Hero />
      <AuthForm />
    </section>
  );
};
