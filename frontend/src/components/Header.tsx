import { useState, type ReactNode } from "react";
import logo from "../assets/logo.svg";
import { NavLink } from "react-router-dom";
import { Menu, X } from "lucide-react";

export const Header = (): ReactNode => {
  const [mobileMenu, setMobileMenu] = useState<boolean>(false);

  const handleClick = () => {
    setMobileMenu(!mobileMenu);
  };

  return (
    <div>
      {/* Desktop */}
      <header className="h-20 bg-[var(--color-bg)] flex justify-between items-center p-4">
        <NavLink to="/">
          <img className="w-20 md:w-24 xl:w-28" src={logo} alt="fanimal logo" />
        </NavLink>
        <div className="hidden lg:flex items-center">
          <nav className="flex items-center gap-6">
            <NavLink
              className={({ isActive }) =>
                isActive ? "text-[var(--color-accent)] font-semibold" : ""
              }
              to="/"
            >
              Home
            </NavLink>
            <NavLink
              className={({ isActive }) =>
                isActive ? "text-[var(--color-accent)] font-semibold" : ""
              }
              to="/explore"
            >
              Explore
            </NavLink>
            <NavLink
              className={({ isActive }) =>
                isActive ? "text-[var(--color-accent)] font-semibold" : ""
              }
              to="/about"
            >
              About
            </NavLink>
            <div className="flex items-center gap-4">
              <button className="btn-primary">
                <NavLink to="/">Log In</NavLink>
              </button>
              <button className="btn-secondary">
                <NavLink to="/">Sign Up</NavLink>
              </button>
            </div>
          </nav>
        </div>
        <button onClick={handleClick} className="lg:hidden cursor-pointer z-20">
          {mobileMenu ? (
            <X className="h-4 w-4 md:h-5 md:w-5" />
          ) : (
            <Menu className="h-4 w-4 md:h-5 md:w-5" />
          )}
        </button>
      </header>

      {/* Mobile */}
      {mobileMenu && <div>Mobile</div>}
    </div>
  );
};
