import Banner from "../components/home/Banner";
import { Hero } from "../components/home/Hero";
import HowItWorks from "../components/home/HowItWorks";
import WhoWillYouSave from "../components/home/WhoWillYouSave";

export const Home = () => {
  return (
    <section>
      <Hero />
      <Banner />
      <HowItWorks />
      <WhoWillYouSave />
    </section>
  );
};
