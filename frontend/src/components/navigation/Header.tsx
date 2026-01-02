import { useState, useEffect } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { Menu, PawPrint, X } from "lucide-react";
import { MobileMenu } from "./MobileMenu";
import type { UserType } from "../../types/AuthTypes";
import { useAuth } from "../../hooks/useAuth";
import AuthMenu from "./AuthMenu";

type HeaderProps = {
  user: UserType | null;
};

export const Header = ({ user }: HeaderProps) => {
  const [mobileMenu, setMobileMenu] = useState(false);
  const [authMode, setAuthMode] = useState<"login" | "register" | null>(null);

  const navigate = useNavigate();
  const { logout } = useAuth();

  const toggleMenu = () => setMobileMenu((prev) => !prev);

  useEffect(() => {
    document.body.style.overflow = mobileMenu ? "hidden" : "auto";
  }, [mobileMenu]);

  useEffect(() => {
    document.body.style.overflow = authMode ? "hidden" : "auto";
  }, [authMode]);

  const openLogin = () => setAuthMode("login");
  const openRegister = () => setAuthMode("register");
  const closeAuth = () => setAuthMode(null);

  const handleLogout = async () => {
    await logout();
    navigate("/");
  };

  const navLinks = [
    { to: "/shelters", name: "Find Shelters" },
    { to: "#how-it-works", name: "How It Works" },
  ];

  return (
    <header className="fixed top-0 right-0 left-0 z-50 w-full border-b-2 bg-white">
      <div className="relative mx-auto flex max-w-[1440px] items-center justify-between p-4">
        {/* Logo */}
        <div className="flex items-center gap-2">
          <PawPrint size={36} aria-hidden="true" />
          <NavLink to={"/"}>
            {" "}
            <h1 className="text-2xl font-bold uppercase">fanimal</h1>
          </NavLink>
        </div>

        {/* Navigation */}
        <nav className="hidden md:flex">
          <ul className="flex items-center gap-8 text-sm font-semibold">
            {navLinks.map((link) => (
              <li key={link.name}>
                <NavLink to={link.to}>{link.name}</NavLink>
              </li>
            ))}
          </ul>
        </nav>

        {/* Actions */}
        <div className="hidden items-center gap-4 md:flex">
          {user ? (
            <>
              <button className="btn-primary">Profile</button>
              <button onClick={handleLogout} className="btn-secondary">
                Logout
              </button>
            </>
          ) : (
            <>
              <button onClick={openLogin} className="btn-primary h-10">
                Login
              </button>
              <button onClick={openRegister} className="btn-secondary h-10">
                Register
              </button>
            </>
          )}
        </div>

        <div className="flex md:hidden">
          <button
            onClick={toggleMenu}
            aria-label={mobileMenu ? "Close menu" : "Open menu"}
            className="translate-y-[2px] cursor-pointer border-2 p-2 transition-all hover:translate-x-[2px] hover:shadow-[2px_2px_0px_0px_#000]"
          >
            {mobileMenu ? (
              <X aria-hidden="true" />
            ) : (
              <Menu aria-hidden="true" />
            )}
          </button>
        </div>
      </div>

      {/* Mobile Navigation */}
      {mobileMenu && (
        <MobileMenu setMobileMenu={setMobileMenu} user={user} logout={logout} />
      )}

      {/* Auth Menu */}
      {authMode && <AuthMenu mode={authMode} onClose={closeAuth} />}
    </header>
  );
};
