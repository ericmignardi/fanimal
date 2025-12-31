import { ProfileCard } from "../components/profile/ProfileCard";
import { useUser } from "../hooks/useUser";

export const Profile = () => {
  const { user, updateCurrentUser, isUpdatingCurrentUser } = useUser();

  return (
    <section className="flex h-full items-center justify-center p-4">
      <ProfileCard
        user={user}
        updateCurrentUser={updateCurrentUser}
        isUpdatingCurrentUser={isUpdatingCurrentUser}
      />
    </section>
  );
};
