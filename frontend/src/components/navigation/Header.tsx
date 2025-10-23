import { useState, useEffect } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { Menu, X } from "lucide-react";
import logo from "../../assets/logo.svg";
import { MobileMenu } from "./MobileMenu";
import type { UserType } from "../../types/AuthTypes";

type HeaderProps = {
  user: UserType | null;
  logout: () => Promise<void>;
};

export const Header = ({ user, logout }: HeaderProps) => {
  const [mobileMenu, setMobileMenu] = useState(false);
  const navigate = useNavigate();

  const toggleMenu = () => setMobileMenu((prev) => !prev);

  useEffect(() => {
    document.body.style.overflow = mobileMenu ? "hidden" : "auto";
  }, [mobileMenu]);

  const handleClick = async () => {
    await logout();
    navigate("/");
  };

  const navLinks = [
    { to: "/", name: "Home" },
    { to: "/shelters", name: "Shelters" },
    { to: "/subscriptions", name: "Subscriptions" },
  ];

  return (
    <header className="fixed top-0 left-0 w-full border-b border-b-[var(--color-border)] bg-[var(--color-bg)] z-50">
      <div className="max-w-[1440px] mx-auto h-16 md:h-20 flex justify-between items-center p-4">
        {/* Logo */}
        <NavLink to="/" aria-label="Fanimal Home">
          <img className="w-24 md:w-28 lg:w-32" src={logo} alt="Fanimal logo" />
        </NavLink>

        {/* Desktop Navigation */}
        <nav className="hidden lg:flex items-center gap-6 text-base font-normal">
          {navLinks.map(({ to, name }) => (
            <NavLink
              key={to}
              to={to}
              className={({ isActive }) =>
                isActive
                  ? "text-[var(--color-accent)] font-semibold hover:text-[var(--color-accent)]/80"
                  : "hover:text-[var(--color-text)]/80 transition-colors"
              }
            >
              {name}
            </NavLink>
          ))}

          {/* Auth Buttons */}
          <div className="flex items-center gap-4">
            {!user ? (
              <>
                <NavLink to="/" className="btn-primary">
                  Log In
                </NavLink>
                <NavLink to="/" className="btn-secondary">
                  Sign Up
                </NavLink>
              </>
            ) : (
              <>
                <NavLink to="/profile" className="btn-primary">
                  Profile
                </NavLink>
                <button onClick={handleClick} className="btn-secondary">
                  Logout
                </button>
              </>
            )}
          </div>
        </nav>

        {/* Mobile Menu Toggle */}
        <button
          onClick={toggleMenu}
          className="lg:hidden cursor-pointer"
          aria-label="Toggle menu"
        >
          {mobileMenu ? (
            <X className="h-5 w-5 md:h-6 md:w-6" />
          ) : (
            <Menu className="h-5 w-5 md:h-6 md:w-6" />
          )}
        </button>
      </div>

      {/* Mobile Navigation */}
      {mobileMenu && (
        <MobileMenu setMobileMenu={setMobileMenu} user={user} logout={logout} />
      )}
    </header>
  );
};
