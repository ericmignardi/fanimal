import { Check, MapPin, Globe, Users, Share2 } from "lucide-react";

const ShelterDetailsHeader = () => {
  return (
    <section className="w-full bg-blue-100">
      <div className="relative mx-auto flex max-w-7xl items-center justify-between gap-12 px-4 py-12 sm:px-6 sm:py-20 lg:px-8">
        <div
          className="absolute inset-0 opacity-10"
          style={{
            backgroundImage: "radial-gradient(#000 1px, transparent 1px)",
            backgroundSize: "20px 20px",
          }}
        ></div>
        <div className="flex items-center gap-8">
          {/* Image */}
          <div className="flex size-32 shrink-0 items-center justify-center border-2 bg-white p-12 shadow-[2px_2px_0px_0px_#000] sm:size-40">
            <span className="text-5xl">🐶</span>
          </div>
          {/* Title */}
          <div className="flex flex-col justify-center gap-2">
            <div className="flex items-center gap-2">
              <h1 className="text-4xl font-semibold uppercase sm:text-5xl">
                Paws & Claws
              </h1>
              <div className="flex items-center gap-1 border-2 bg-green-400 px-2 uppercase shadow-[2px_2px_0px_0px_#000]">
                <Check />
                <span className="text-xs font-semibold">Verified</span>
              </div>
            </div>
            <p className="text-lg font-medium text-slate-800">
              Dedicated to rehabilitating stray dogs in the greater metro area.
              We focus on medical cases that others turn away.
            </p>
            <div className="flex items-center gap-4 text-sm font-medium">
              <div className="flex items-center gap-1">
                <MapPin /> Brooklyn, NY
              </div>
              <div className="flex items-center gap-1">
                <Globe />
                <a href="https://www.pawsandclaws.org">pawsandclaws.org</a>
              </div>
              <div className="flex items-center gap-1">
                <Users />
                1240 Monthly Donors
              </div>
            </div>
          </div>
        </div>

        <button className="cursor-pointer self-end border-2 bg-white p-3 shadow-[4px_4px_0px_0px_#000]">
          <Share2 className="text-xl" />
        </button>
      </div>
    </section>
  );
};

export default ShelterDetailsHeader;
