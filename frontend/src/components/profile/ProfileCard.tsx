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
    <div className="relative flex flex-col items-center justify-center gap-1">
      <button
        onClick={handleClick}
        className="cursor-pointer overflow-clip rounded-full border-2 border-[var(--color-primary)] hover:opacity-90"
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
        <div className="absolute flex h-full flex-col justify-center gap-2 rounded-2xl border border-[var(--color-border)] p-4 backdrop-blur-2xl">
          <h2 className="text-center text-lg font-semibold">Update User</h2>
          <form onSubmit={handleSubmit}>
            <label htmlFor="name">Name</label>
            <input
              className="rounded-2xl border border-[var(--color-border)] p-2 focus:border-none focus:outline focus:outline-[var(--color-primary)]"
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
