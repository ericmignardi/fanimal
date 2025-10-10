import { AuthForm } from "../components/AuthForm";
import { Hero } from "../components/Hero";

export const Home = () => {
  return (
    <section className="h-full grid grid-cols-1 md:grid-cols-2 justify-center items-center p-4">
      <Hero />
      <AuthForm />
    </section>
  );
};
