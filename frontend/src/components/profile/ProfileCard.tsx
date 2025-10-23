import { useState } from "react";
import type { ProfileCardProps } from "../../types/UserTypes";

export const ProfileCard = ({
  user,
  updateCurrentUser,
  isUpdatingCurrentUser,
}: ProfileCardProps) => {
  const [updateModal, setUpdateModal] = useState<boolean>(false);
  const [formData, setFormData] = useState<{ name: string }>({
    name: "",
  });

  const handleClick = () => {
    setFormData({ name: user?.name || "" });
    setUpdateModal(!updateModal);
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    await updateCurrentUser(formData);
    setUpdateModal(false);
  };

  return (
    <div className="flex flex-col justify-center items-center gap-1 relative">
      <button
        onClick={handleClick}
        className="rounded-full border-2 border-[var(--color-primary)] cursor-pointer hover:opacity-90 overflow-clip"
      >
        <img
          className="h-20 hover:scale-110"
          src="https://thispersondoesnotexist.com/"
          alt={`${user?.name} 's profile picture`}
        />
      </button>
      <h1 className="text-xl font-bold">{user?.name}</h1>
      <span className="text-sm font-light text-[var(--color-primary)]">
        @{user?.username}
      </span>
      {updateModal && (
        <div className="absolute backdrop-blur-2xl border border-[var(--color-border)] h-full rounded-2xl p-4 flex flex-col justify-center gap-2">
          <h2 className="text-lg font-semibold text-center">Update User</h2>
          <form onSubmit={handleSubmit}>
            <label htmlFor="name">Name</label>
            <input
              className="border border-[var(--color-border)] rounded-2xl p-2 focus:border-none focus:outline focus:outline-[var(--color-primary)]"
              type="text"
              name="name"
              id="name"
              onChange={(e) => setFormData({ name: e.target.value })}
              value={formData.name}
            />
          </form>
          <button
            className="btn-primary"
            type="submit"
            disabled={isUpdatingCurrentUser}
          >
            {isUpdatingCurrentUser ? "Submitting..." : "Submit"}
          </button>
        </div>
      )}
    </div>
  );
};
