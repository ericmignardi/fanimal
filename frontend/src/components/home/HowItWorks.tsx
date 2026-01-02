import { Glasses, Heart, Mail } from "lucide-react";
import type { LucideIcon } from "lucide-react";
import HowItWorksCard from "./HowItWorksCard";

interface HowItWorksItem {
  id: number;
  icon: LucideIcon;
  iconBackground: string;
  title: string;
  description: string;
}

const howItWorksData: HowItWorksItem[] = [
  {
    id: 1,
    icon: Glasses,
    iconBackground: "bg-amber-100",
    title: "1. Find a Cause",
    description:
      "Browse verified 501(c)(3) shelters by location, animal type, or urgency level.",
  },
  {
    id: 2,
    icon: Heart,
    iconBackground: "bg-purple-100",
    title: "2. Direct Support",
    description:
      "Subscribe monthly or give one-time. Funds go directly to the shelter's bank account.",
  },
  {
    id: 3,
    icon: Mail,
    iconBackground: "bg-green-100",
    title: "3. Get Updates",
    description:
      "Receive photos and stories about the specific animals your donation helped save.",
  },
];

const HowItWorks = () => {
  return (
    <section className="w-full border-y-2 bg-amber-50">
      <div className="mx-auto flex max-w-7xl flex-col justify-center gap-12 px-4 py-20 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="flex flex-col items-center justify-center gap-4 text-center">
          <h3 className="text-4xl font-bold uppercase">How It Works</h3>
          <p className="text-lg text-gray-600">
            Transparency is our love language. See exactly how your support
            changes lives.
          </p>
        </div>

        {/* Main */}
        <div className="grid grid-cols-1 gap-8 md:grid-cols-3">
          {howItWorksData.map((item) => (
            <HowItWorksCard
              key={item.id}
              icon={item.icon}
              iconBackground={item.iconBackground}
              title={item.title}
              description={item.description}
            />
          ))}
        </div>
      </div>
    </section>
  );
};

export default HowItWorks;
