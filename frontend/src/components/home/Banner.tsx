const trustBadges = [
  "100% Goes To Charity",
  "Verified Non-Profits",
  "Real-Time Updates",
  "Tax Deductible",
  "Community Driven",
];

const Banner = () => {
  return (
    <div className="w-full overflow-hidden bg-black py-4 text-white">
      <div>
        <ul className="flex items-center justify-center gap-8 text-xl font-bold whitespace-nowrap uppercase">
          {trustBadges.map((badge, index) => (
            <>
              <li key={badge}>{badge}</li>
              {index < trustBadges.length - 1 && (
                <span className="text-2xl text-yellow-400" aria-hidden="true">
                  •
                </span>
              )}
            </>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default Banner;
