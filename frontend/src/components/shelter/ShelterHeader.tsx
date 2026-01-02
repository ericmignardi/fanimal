import { Lightbulb, Search } from "lucide-react";

const filters = [
  { label: "Dogs", emoji: "🐶" },
  { label: "Cats", emoji: "🐱" },
  { label: "Farm", emoji: "🐮" },
  { label: "Exotic", emoji: "🦎" },
];

export const ShelterHeader = () => {
  return (
    <section className="w-full bg-yellow-300 py-16">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        {/* Search */}
        <div className="flex flex-col justify-center gap-8">
          {/* Title */}
          <h2 className="text-4xl font-semibold uppercase sm:text-6xl">
            Find A Cause. <br />
            Be A Hero.
          </h2>

          {/* Input */}
          <div className="flex items-center gap-4">
            <div className="flex flex-1 items-center gap-2 border-2 bg-white p-4 shadow-[4px_4px_0px_0px_#000]">
              <Search aria-hidden="true" />
              <input
                type="text"
                placeholder="Search by name, city, or zip code..."
                className="w-full focus:outline-0"
                aria-label="Search shelters by name, city, or zip code"
              />
            </div>
            <button className="btn-primary text-lg uppercase">Search</button>
          </div>

          {/* Filters */}
          <div className="flex flex-wrap items-center gap-4">
            <span className="text-sm font-semibold uppercase">Filter By: </span>

            <div className="flex items-center gap-3">
              {filters.map((filter) => (
                <button
                  key={filter.label}
                  className="flex cursor-pointer items-center gap-1 border-2 bg-white px-4 py-1 shadow-[2px_2px_0px_0px_#000]"
                >
                  <span className="text-sm font-semibold">{filter.label}</span>
                  <span aria-hidden="true">{filter.emoji}</span>
                </button>
              ))}
            </div>

            <span
              className="mx-2 hidden h-8 w-[1px] bg-black sm:block"
              aria-hidden="true"
            ></span>

            <button className="flex items-center gap-1 border-2 bg-red-400 px-4 py-1 shadow-[2px_2px_0px_0px_#000]">
              <Lightbulb aria-hidden="true" />
              <span className="text-sm font-semibold">Urgent Needs</span>
            </button>
          </div>
        </div>
      </div>
    </section>
  );
};
