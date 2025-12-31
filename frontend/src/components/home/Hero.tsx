import { ArrowRight } from "lucide-react";

interface DecorativeCircle {
  emoji: string;
  bgColor: string;
  position: string;
  size: string;
  emojiSize: string;
  zIndex: string;
  isRounded?: boolean;
  rotate?: string;
}

const decorativeCircles: DecorativeCircle[] = [
  {
    emoji: "🐕",
    bgColor: "bg-pink-300",
    position: "top-0 right-0",
    size: "size-80",
    emojiSize: "text-8xl",
    zIndex: "z-10",
    isRounded: true,
  },
  {
    emoji: "🐈",
    bgColor: "bg-blue-300",
    position: "left-10 bottom-0",
    size: "size-64",
    emojiSize: "text-7xl",
    zIndex: "z-20",
    isRounded: true,
  },
  {
    emoji: "🐇",
    bgColor: "bg-green-300",
    position: "right-20 bottom-20",
    size: "size-32",
    emojiSize: "text-4xl",
    zIndex: "z-30",
    isRounded: false,
    rotate: "rotate-12",
  },
];

export const Hero = () => {
  return (
    <section className="w-full bg-yellow-300">
      <div className="mx-auto grid max-w-7xl grid-cols-1 items-center gap-12 px-4 py-20 sm:px-6 md:grid-cols-2 lg:px-8">
        {/* Left */}
        <div className="flex flex-col justify-center gap-8">
          {/* Badge */}
          <div>
            <span className="inline-block rotate-[-2deg] border-2 bg-white p-2 text-sm font-bold uppercase shadow-[4px_4px_0px_0px_#000]">
              New: Direct Donations
            </span>
          </div>

          {/* Title */}
          <h2 className="text-5xl font-semibold uppercase sm:text-7xl">
            Don't Just <br />
            Like. <span className="text-white">Save A Life.</span>
          </h2>

          {/* Description */}
          <p className="max-w-md text-xl font-medium text-slate-800">
            The direct-to-shelter platform connecting you with local rescues. No
            middlemen, just wagging tails.
          </p>

          {/* Actions */}
          <div className="flex flex-col gap-4 sm:flex-row">
            <button className="btn-primary gap-2 text-lg">
              Find a Shelter <ArrowRight />
            </button>
            <button className="btn-secondary text-lg">
              Are you a Shelter?
            </button>
          </div>
        </div>

        {/* Right - Decorative Circles */}
        <div className="relative hidden h-96 md:block">
          {decorativeCircles.map((circle) => (
            <div
              key={circle.emoji}
              className={`absolute ${
                circle.bgColor
              } flex items-center justify-center ${circle.position} ${
                circle.zIndex
              } ${circle.size} ${circle.isRounded ? "rounded-full" : ""} ${
                circle.rotate || ""
              } border-2 shadow-[4px_4px_0px_0px_#000]`}
            >
              <span className={circle.emojiSize}>{circle.emoji}</span>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};
