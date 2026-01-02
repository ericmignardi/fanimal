import { Smile, ArrowDown, Loader } from "lucide-react";
import { ShelterCard } from "./ShelterCard";
import { useEffect } from "react";
import { useShelter } from "../../hooks/useShelter";

// const shelterCards: ShelterCardType[] = [
//   {
//     id: "paws-and-claws",
//     verified: true,
//     image: Smile,
//     imageBackground: "bg-blue-100",
//     title: "Paws & Claws Rescue",
//     badge: "501(c)(3)",
//     description:
//       "Dedicated to rehabilitating stray dogs in the greater metro area. We focus on medical cases that others turn away.",
//     tags: ["Dogs", "Medical", "Foster Based"],
//     supporters: 1240,
//   },
//   {
//     id: "the-cats-meow",
//     verified: false,
//     image: Cat,
//     imageBackground: "bg-pink-100",
//     title: "The Cat's Meow",
//     badge: null,
//     description:
//       "A cage-free sanctuary for cats waiting for their forever homes.",
//     tags: ["Cats", "Sanctuary"],
//     supporters: 850,
//   },
//   {
//     id: "reptile-heaven",
//     verified: false,
//     image: Turtle,
//     imageBackground: "bg-green-100",
//     title: "Reptile Heaven",
//     badge: null,
//     description:
//       "Rescuing, rehabilitating and releasing native turtles and lizards.",
//     tags: ["Exotic", "Rehab"],
//     supporters: 320,
//   },
//   {
//     id: "second-chance-farm",
//     verified: false,
//     image: Bird,
//     imageBackground: "bg-yellow-100",
//     title: "Second Chance Farm",
//     badge: null,
//     description:
//       "Providing a safe landing for retired work horses and farm animals.",
//     tags: ["Farm", "Senior Care"],
//     supporters: 2100,
//   },
// ];

export const ShelterContent = () => {
  const { shelters, isFindingAll, findAll } = useShelter();

  useEffect(() => {
    findAll();
  }, []);

  return (
    <section className="mx-auto w-full max-w-7xl px-4 py-16 sm:px-6 lg:px-8">
      <div className="mx-auto flex max-w-7xl flex-col justify-center gap-12">
        {/* Header */}
        <div className="flex items-center justify-between text-sm font-medium">
          <div>
            <p>
              Showing <span className="font-bold">{shelters?.length}</span>{" "}
              shelters near you
            </p>
          </div>

          <div className="flex items-center gap-2">
            <label htmlFor="sort">Sort: </label>
            <select
              id="sort"
              name="sort"
              className="cursor-pointer border-b-2 bg-transparent font-bold"
            >
              <option value={"recommended"}>Recommended</option>
              <option value={"distance"}>Distance</option>
              <option value={"most-urgent"}>Most Urgent</option>
            </select>
          </div>
        </div>

        {/* Content */}
        <div className="grid grid-cols-1 gap-8 md:grid-cols-2 lg:grid-cols-3">
          {isFindingAll ? (
            <div className="col-span-full flex min-h-[200px] items-center justify-center">
              <Loader className="h-8 w-8 animate-spin" />
            </div>
          ) : shelters && shelters.length > 0 ? (
            shelters.map((shelter) => (
              // modify card to match dto response or add fields to shelter model, currently hardcoding missing values
              <ShelterCard
                key={shelter.id}
                id={String(shelter.id)}
                verified={false} // or add to ShelterType
                image={Smile} // default icon or add to ShelterType
                imageBackground="bg-blue-100" // default or add to ShelterType
                title={shelter.name}
                badge={null} // or add to ShelterType
                description={shelter.description}
                tags={[]} // or add to ShelterType
                supporters={0} // or add to ShelterType
              />
            ))
          ) : (
            <div className="col-span-full flex min-h-[200px] flex-col items-center justify-center gap-2 text-gray-500">
              <Smile className="h-12 w-12" />
              <p className="text-lg font-medium">No shelters found</p>
              <p className="text-sm">
                Try adjusting your search or check back later.
              </p>
            </div>
          )}
        </div>

        {/* Load */}
        <div className="flex justify-center">
          <button className="btn-primary flex items-center gap-2 text-lg">
            Load More Shelters <ArrowDown aria-hidden="true" />
          </button>
        </div>
      </div>
    </section>
  );
};
