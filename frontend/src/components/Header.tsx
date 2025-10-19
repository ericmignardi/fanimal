import { useState } from "react";
import logo from "../assets/logo.svg";
import { NavLink } from "react-router-dom";
import { Menu, X } from "lucide-react";
import { MobileMenu } from "./MobileMenu";

export const Header = () => {
  const [mobileMenu, setMobileMenu] = useState(false);

  const handleClick = () => {
    setMobileMenu(!mobileMenu);
  };

  return (
    <div className="relative border-b border-b-[var(--color-border)]">
      <header className="h-20 bg-[var(--color-bg)] flex justify-between items-center p-4">
        <NavLink to="/">
          <img className="w-24 md:w-28 xl:w-32" src={logo} alt="fanimal logo" />
        </NavLink>
        <div className="hidden lg:flex items-center">
          <nav className="flex items-center gap-6 text-base sm:text-lg font-medium">
            <NavLink
              className={({ isActive }) =>
                isActive
                  ? "text-[var(--color-accent)] font-semibold hover:text-[var(--color-accent)]/80"
                  : "hover:text-[var(--color-text)]/80 transition-colors"
              }
              to="/"
            >
              Home
            </NavLink>
            <NavLink
              className={({ isActive }) =>
                isActive
                  ? "text-[var(--color-accent)] font-semibold hover:text-[var(--color-accent)]/80"
                  : "hover:text-[var(--color-text)]/80 transition-colors"
              }
              to="/shelters"
            >
              Shelters
            </NavLink>
            <NavLink
              className={({ isActive }) =>
                isActive
                  ? "text-[var(--color-accent)] font-semibold hover:text-[var(--color-accent)]/80"
                  : "hover:text-[var(--color-text)]/80 transition-colors"
              }
              to="/subscriptions"
            >
              Subscriptions
            </NavLink>
            <NavLink
              className={({ isActive }) =>
                isActive
                  ? "text-[var(--color-accent)] font-semibold hover:text-[var(--color-accent)]/80"
                  : "hover:text-[var(--color-text)]/80 transition-colors"
              }
              to="/profile"
            >
              Profile
            </NavLink>
            <div className="flex items-center gap-4 text-base sm:text-lg font-semibold">
              <button className="btn-primary">
                <NavLink to="/">Log In</NavLink>
              </button>
              <button className="btn-secondary">
                <NavLink to="/">Sign Up</NavLink>
              </button>
            </div>
          </nav>
        </div>
        <button
          onClick={handleClick}
          className="lg:hidden cursor-pointer z-20"
          aria-label="Toggle menu"
        >
          {mobileMenu ? (
            <X className="h-5 w-5 md:h-6 md:w-6" />
          ) : (
            <Menu className="h-5 w-5 md:h-6 md:w-6" />
          )}
        </button>
      </header>

      {/* Mobile Menu */}
      {mobileMenu && (
        <MobileMenu mobileMenu={mobileMenu} setMobileMenu={setMobileMenu} />
      )}
    </div>
  );
};
