import { DollarSign, Heart } from "lucide-react";
import { useState } from "react";
import type { ShelterType } from "../../types/ShelterTypes";

type ShelterDetailsContentProps = {
  shelter: ShelterType;
};

export const ShelterDetailsContent = ({
  shelter,
}: ShelterDetailsContentProps) => {
  const [freq, setFreq] = useState<string>("monthly");

  return (
    <section className="w-full bg-amber-50">
      <div className="mx-auto grid max-w-7xl grid-cols-1 gap-12 px-4 py-12 sm:px-6 lg:grid-cols-2 lg:px-8">
        {/* Left */}
        <div className="flex flex-col justify-center gap-12">
          {/* Stats */}
          <div className="grid grid-cols-3 gap-4">
            <div className="flex flex-col items-center justify-center border-2 bg-white px-12 py-8 shadow-[4px_4px_0px_0px_#000]">
              <data value="852" className="text-3xl font-bold">
                852
              </data>
              <span className="text-xs font-bold text-gray-500 uppercase">
                Lives Saved
              </span>
            </div>
            <div className="flex flex-col items-center justify-center border-2 bg-white px-12 py-8 shadow-[4px_4px_0px_0px_#000]">
              <data value="45" className="text-3xl font-bold">
                45
              </data>
              <span className="text-xs font-bold text-gray-500 uppercase">
                In Care
              </span>
            </div>
            <div className="flex flex-col items-center justify-center border-2 bg-white px-12 py-8 shadow-[4px_4px_0px_0px_#000]">
              <data value="2018" className="text-3xl font-bold">
                2018
              </data>
              <span className="text-xs font-bold text-gray-500 uppercase">
                Founded
              </span>
            </div>
          </div>

          {/* Mission */}
          <div className="flex flex-col justify-center gap-4">
            <div className="flex items-center gap-4">
              <span
                className="text-4xl font-bold text-yellow-400"
                aria-hidden="true"
              >
                |
              </span>
              <h2 className="text-2xl font-semibold uppercase">Our Mission</h2>
            </div>
            <p className="text-base font-medium text-slate-800">
              At {shelter.name}, we believe every animal deserves a second
              chance.
              <br />
              <br />
              Your monthly subscription goes directly towards veterinary bills,
              high-quality food, and rehabilitation. We are 100% volunteer-run,
              meaning every dollar helps an animal.
            </p>
          </div>

          {/* Meet The Pack */}
          <div className="flex flex-col justify-center gap-4">
            <div className="flex items-center gap-4">
              <span
                className="text-4xl font-bold text-pink-400"
                aria-hidden="true"
              >
                |
              </span>
              <div className="flex w-full items-center justify-between">
                <h2 className="text-2xl font-semibold uppercase">
                  Meet The Pack
                </h2>
                <a
                  className="text-sm font-bold underline decoration-2"
                  href="https://www.pawsandclaws.org"
                  target="_blank"
                >
                  View All
                </a>
              </div>
            </div>
            <div className="grid grid-cols-2 gap-4 sm:grid-cols-3">
              <div className="group relative flex aspect-square items-center justify-center overflow-hidden border-2 bg-slate-200">
                <span className="text-4xl transition-transform group-hover:scale-110">
                  🐕
                </span>
                <span className="absolute bottom-0 left-0 z-10 w-full translate-y-full bg-black p-2 text-xs text-white transition-transform group-hover:translate-y-0">
                  Barnaby
                </span>
              </div>
              <div className="group relative flex aspect-square items-center justify-center overflow-hidden border-2 bg-slate-200">
                <span className="text-4xl transition-transform group-hover:scale-110">
                  🐩
                </span>
                <span className="absolute bottom-0 left-0 z-10 w-full translate-y-full bg-black p-2 text-xs text-white transition-transform group-hover:translate-y-0">
                  Luna
                </span>
              </div>
              <div className="group relative flex aspect-square items-center justify-center overflow-hidden border-2 bg-slate-200">
                <span className="text-4xl transition-transform group-hover:scale-110">
                  🐕‍🦺
                </span>
                <span className="absolute bottom-0 left-0 z-10 w-full translate-y-full bg-black p-2 text-xs text-white transition-transform group-hover:translate-y-0">
                  Max
                </span>
              </div>
            </div>
          </div>
        </div>

        {/* Right */}
        <div className="flex flex-col gap-8">
          {/* Subscription */}
          <div className="flex flex-col justify-center gap-6 border-2 bg-white p-6 shadow-[4px_4px_0px_0px_#000]">
            <h3 className="text-xl font-bold">Subscribe To This Shelter</h3>
            <p className="text-sm text-gray-600">
              Your monthly support keeps their doors open. Cancel anytime.
            </p>
            <div className="grid grid-cols-3 gap-2">
              <div className="flex cursor-pointer items-center justify-center border-2 p-4 font-bold">
                $5
              </div>
              <div className="flex cursor-pointer items-center justify-center border-2 bg-black p-4 font-bold text-white">
                $15
              </div>
              <div className="flex cursor-pointer items-center justify-center border-2 p-4 font-bold">
                $50
              </div>
            </div>
            <div className="flex items-center gap-2 border-2 p-4">
              <DollarSign className="size-4 text-gray-600" aria-hidden="true" />
              <input
                className="w-full focus:outline-none"
                type="number"
                name="price"
                id="price"
                placeholder="Other amount"
              />
            </div>
            <div className="grid grid-cols-2 gap-2 border-2 p-2 text-sm font-bold">
              <label
                onClick={() => setFreq("monthly")}
                className={`flex cursor-pointer items-center justify-center p-1 transition-colors ${freq === "monthly" ? "border-2 p-1 shadow-[2px_2px_0px_0px_#000]" : ""}`}
              >
                <input className="sr-only" name="freq" type="radio" />
                <span>Monthly</span>
              </label>
              <label
                onClick={() => setFreq("yearly")}
                className={`flex cursor-pointer items-center justify-center p-1 transition-colors ${freq === "yearly" ? "border-2 p-1 shadow-[2px_2px_0px_0px_#000]" : ""}`}
              >
                <input className="sr-only" name="freq" type="radio" />
                <span>Yearly</span>
              </label>
            </div>
            <button className="btn-primary flex items-center gap-2 text-lg">
              Start Donating
              <Heart aria-hidden="true" />
            </button>
            <small className="text-center text-xs text-gray-500">
              100% of donations go directly to the shelter minus standard
              processing fees.
            </small>
          </div>

          {/* Recent Heroes */}
          <div className="flex flex-col justify-center gap-4">
            <h4 className="text-sm font-semibold uppercase">Recent Heroes</h4>
            <ul className="flex flex-col justify-center gap-2 text-sm">
              <li className="flex items-center gap-4">
                <div className="size-8 rounded-full border bg-blue-200"></div>
                <p>
                  <span className="font-semibold">Alex M.</span> donated $15
                </p>
              </li>
              <li className="flex items-center gap-4">
                <div className="size-8 rounded-full border bg-green-200"></div>
                <p>
                  <span className="font-semibold">Jordan T.</span> donated $50
                </p>
              </li>
              <li className="flex items-center gap-4">
                <div className="size-8 rounded-full border bg-red-200"></div>
                <p>
                  <span className="font-semibold">Casey R.</span> subscribed
                  $10/mo
                </p>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </section>
  );
};
