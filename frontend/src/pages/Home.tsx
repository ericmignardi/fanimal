import { AuthForm } from "../components/home/AuthForm";
import { Hero } from "../components/home/Hero";

export const Home = () => {
  return (
    <section className="grid grid-cols-1 md:grid-cols-2 justify-center items-center gap-8">
      <Hero />
      <AuthForm />
    </section>
  );
};
