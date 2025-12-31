import WhoWillYouSaveCard from "./WhoWillYouSaveCard";

const whoWillYouSaveCards = [
  { emoji: "🐶", title: "Dogs" },
  { emoji: "🐱", title: "Cats" },
  { emoji: "🐢", title: "Reptiles" },
  { emoji: "🦜", title: "Birds" },
];

const WhoWillYouSave = () => {
  return (
    <section className="w-full bg-blue-100">
      <div className="mx-auto flex max-w-7xl flex-col justify-center gap-12 px-4 py-20 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="flex items-center justify-center text-center">
          <h3 className="text-4xl font-bold uppercase">Who Will You Save?</h3>
        </div>

        {/* Main */}
        <div className="grid grid-cols-2 gap-4 md:grid-cols-4">
          {/* Card */}
          {whoWillYouSaveCards.map((item) => (
            <WhoWillYouSaveCard
              key={item.title}
              emoji={item.emoji}
              title={item.title}
            />
          ))}
        </div>
      </div>
    </section>
  );
};

export default WhoWillYouSave;
