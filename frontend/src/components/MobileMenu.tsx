import { Home, Search, User } from "lucide-react";
import { NavLink } from "react-router-dom";
import type { Dispatch, SetStateAction } from "react";

type MobileMenuProps = {
  mobileMenu: boolean;
  setMobileMenu: Dispatch<SetStateAction<boolean>>;
};

export const MobileMenu = ({ mobileMenu, setMobileMenu }: MobileMenuProps) => {
  const handleClick = () => {
    setMobileMenu(!mobileMenu);
  };

  return (
    <div className="absolute top-0 right-0 w-[75%] h-[100vh] z-10 bg-[var(--color-bg)] p-12">
      <div className="flex flex-col gap-4">
        <div className="flex justify-between items-center border-b border-b-[var(--color-border)] p-2">
          <NavLink
            onClick={handleClick}
            to="/"
            className={({ isActive }) =>
              isActive
                ? "text-[var(--color-accent)] font-semibold hover:text-[var(--color-accent)]/80"
                : "hover:text-[var(--color-text)]/80 transition-colors"
            }
          >
            Home
          </NavLink>
          <Home className="text-[var(--color-primary)]" />
        </div>
        <div className="flex justify-between items-center border-b border-b-[var(--color-border)] p-2">
          <NavLink
            onClick={handleClick}
            to="/shelters"
            className={({ isActive }) =>
              isActive
                ? "text-[var(--color-accent)] font-semibold hover:text-[var(--color-accent)]/80"
                : "hover:text-[var(--color-text)]/80 transition-colors"
            }
          >
            Shelters
          </NavLink>
          <Search className="text-[var(--color-primary)]" />
        </div>
        <div className="flex justify-between items-center border-b border-b-[var(--color-border)] p-2">
          <NavLink
            onClick={handleClick}
            to="/subscriptions"
            className={({ isActive }) =>
              isActive
                ? "text-[var(--color-accent)] font-semibold hover:text-[var(--color-accent)]/80"
                : "hover:text-[var(--color-text)]/80 transition-colors"
            }
          >
            Subscriptions
          </NavLink>
          <Search className="text-[var(--color-primary)]" />
        </div>
        <div className="flex justify-between items-center border-b border-b-[var(--color-border)] p-2">
          <NavLink
            onClick={handleClick}
            to="/profile"
            className={({ isActive }) =>
              isActive
                ? "text-[var(--color-accent)] font-semibold hover:text-[var(--color-accent)]/80"
                : "hover:text-[var(--color-text)]/80 transition-colors"
            }
          >
            Profile
          </NavLink>
          <User className="text-[var(--color-primary)]" />
        </div>
      </div>
    </div>
  );
};
