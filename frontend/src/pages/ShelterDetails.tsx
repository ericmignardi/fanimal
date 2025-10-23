import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useShelter } from "../hooks/useShelter";
import hero from "../assets/hero.jpg";

export const ShelterDetails = () => {
  const { id: idParam } = useParams<{ id: string }>();
  const id = Number(idParam);
  const { shelter, findById, isFindingById } = useShelter();
  const [activeTab, setActiveTab] = useState("about");

  const tabs = [
    { id: "about", tab: "About", content: "about content" },
    { id: "posts", tab: "Posts", content: "posts content" },
    { id: "animals", tab: "Animals", content: "animals content" },
    { id: "subscribers", tab: "Subscribers", content: "subscribers content" },
  ];

  const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    setActiveTab(e.currentTarget.id);
  };

  useEffect(() => {
    if (!isNaN(id)) findById(id);
  }, [id]);

  const activeContent =
    activeTab === "about"
      ? shelter?.description || "No description available."
      : tabs.find((t) => t.id === activeTab)?.content;

  return (
    <section className="flex flex-col justify-center gap-8">
      {/* Image */}
      <div className="relative">
        <img className="w-full rounded-xl" src={hero} alt={shelter?.name} />
        <h1 className="absolute bottom-2 left-4 text-xl sm:text-2xl md:text-3xl lg:text-4xl font-extrabold text-[var(--color-bg)]">
          {shelter?.name}
        </h1>
      </div>

      {/* Tabs */}
      <div className="flex flex-col justify-center gap-4">
        {/* Tabs Header */}
        <div className="border-b border-b-[var(--color-border)] flex items-center gap-2">
          {tabs.map((tab) => (
            <button
              key={tab.id}
              id={tab.id}
              onClick={handleClick}
              className={`p-2 text-xs md:text-sm ${
                activeTab === tab.id
                  ? "border-b-2 border-b-[var(--color-primary)] font-semibold"
                  : ""
              }`}
            >
              {tab.tab}
            </button>
          ))}
        </div>

        {/* Tabs Body */}
        <div className="flex flex-col justify-center gap-1">
          <h2 className="text-sm sm:text-base md:text-lg lg:text-xl font-semibold">
            About
          </h2>
          {isFindingById ? (
            <p className="text-xs md:text-sm">Loading...</p>
          ) : (
            <p className="text-xs md:text-sm">{activeContent}</p>
          )}
        </div>
      </div>

      {/* Posts */}
      {/* <div></div> */}

      {/* Animals */}
      {/* <div></div> */}
    </section>
  );
};
