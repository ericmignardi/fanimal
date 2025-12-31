interface WhoWillYouSaveCardType {
  emoji: string;
  title: string;
}

const WhoWillYouSaveCard = ({ emoji, title }: WhoWillYouSaveCardType) => {
  return (
    <div className="group flex aspect-square cursor-pointer flex-col items-center justify-center gap-4 border-2 bg-white shadow-[4px_4px_0px_0px_#000] transition-all">
      <span
        className="text-6xl transition-transform group-hover:scale-110"
        aria-hidden="true"
      >
        {emoji}
      </span>
      <h3 className="font-bold uppercase">{title}</h3>
    </div>
  );
};

export default WhoWillYouSaveCard;
