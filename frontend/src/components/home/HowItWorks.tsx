import { Glasses, Heart, Mail } from "lucide-react";
import type { LucideIcon } from "lucide-react";

interface HowItWorksCardProps {
  icon: LucideIcon;
  iconBgClass: string;
  title: string;
  description: string;
}

const HowItWorksCard = ({
  icon: Icon,
  iconBgClass,
  title,
  description,
}: HowItWorksCardProps) => {
  return (
    <div className="flex flex-col justify-center gap-4 border-2 bg-white p-8 shadow-[4px_4px_0px_0px_#000] transition-all hover:-translate-y-1">
      <div
        className={`flex size-14 items-center justify-center border-2 p-2 ${iconBgClass}`}
      >
        <Icon className="size-8" />
      </div>
      <span className="text-xl font-semibold">{title}</span>
      <p>{description}</p>
    </div>
  );
};

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
          <HowItWorksCard
            icon={Glasses}
            iconBgClass="bg-amber-100"
            title="1. Find a Cause"
            description="Browse verified 501(c)(3) shelters by location, animal type, or urgency level."
          />
          <HowItWorksCard
            icon={Heart}
            iconBgClass="bg-purple-100"
            title="2. Direct Support"
            description="Subscribe monthly or give one-time. Funds go directly to the shelter's bank account."
          />
          <HowItWorksCard
            icon={Mail}
            iconBgClass="bg-green-100"
            title="3. Get Updates"
            description="Receive photos and stories about the specific animals your donation helped save."
          />
        </div>
      </div>
    </section>
  );
};

export default HowItWorks;
