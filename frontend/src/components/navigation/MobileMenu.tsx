import { Home, Search, Package } from "lucide-react";
import { NavLink, useNavigate } from "react-router-dom";
import type { Dispatch, SetStateAction } from "react";
import type { UserType } from "../../types/AuthTypes";

type MobileMenuProps = {
  setMobileMenu: Dispatch<SetStateAction<boolean>>;
  user: UserType | null;
  logout: () => Promise<void>;
};

export const MobileMenu = ({
  setMobileMenu,
  user,
  logout,
}: MobileMenuProps) => {
  const toggleMenu = () => setMobileMenu((prev) => !prev);
  const navigate = useNavigate();

  const handleClick = async () => {
    await logout();
    navigate("/");
  };

  const navLinks = [
    { to: "/", name: "Home", icon: Home },
    { to: "/shelters", name: "Shelters", icon: Search },
    { to: "/subscriptions", name: "Subscriptions", icon: Package },
  ];

  return (
    <div className="absolute top-0 right-0 z-50 h-screen w-[75%] bg-[var(--color-bg)] p-12 shadow-lg">
      <nav className="flex flex-col gap-6 text-base font-normal">
        {navLinks.map(({ to, name, icon: Icon }) => (
          <NavLink
            key={to}
            to={to}
            onClick={toggleMenu}
            className={({ isActive }) =>
              `flex items-center justify-between gap-4 rounded-md p-2 transition-colors hover:bg-[var(--color-text)]/10 ${
                isActive
                  ? "font-semibold text-[var(--color-accent)]"
                  : "hover:text-[var(--color-text)]/80"
              }`
            }
          >
            {name}
            <Icon className="text-[var(--color-primary)]" aria-hidden="true" />
          </NavLink>
        ))}

        {/* Auth Buttons */}
        <div className="mt-4 flex flex-col gap-4 border-t border-[var(--color-border)] pt-8">
          {!user ? (
            <>
              <NavLink
                to="/"
                onClick={toggleMenu}
                className="btn-primary w-full text-center"
              >
                Log In
              </NavLink>
              <NavLink
                to="/"
                onClick={toggleMenu}
                className="btn-secondary w-full text-center"
              >
                Sign Up
              </NavLink>
            </>
          ) : (
            <>
              <NavLink
                to="/profile"
                onClick={toggleMenu}
                className="btn-primary w-full text-center"
              >
                Profile
              </NavLink>
              <button
                onClick={handleClick}
                className="btn-secondary w-full text-center"
              >
                Logout
              </button>
            </>
          )}
        </div>
      </nav>
    </div>
  );
};
