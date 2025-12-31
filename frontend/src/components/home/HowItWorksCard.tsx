import type { LucideIcon } from "lucide-react";

interface HowItWorksCardType {
  icon: LucideIcon;
  iconBackground: string;
  title: string;
  description: string;
}

const HowItWorksCard = ({
  icon: Icon,
  iconBackground,
  title,
  description,
}: HowItWorksCardType) => {
  return (
    <div className="flex flex-col justify-center gap-4 border-2 p-8 shadow-[4px_4px_0px_0px_#000] transition-all hover:-translate-y-1">
      <div
        className={`flex size-14 items-center justify-center border-2 p-2 ${iconBackground}`}
      >
        <Icon className="text-3xl" />
      </div>
      <span className="text-xl font-semibold">{title}</span>
      <p>{description}</p>
    </div>
  );
};

export default HowItWorksCard;
