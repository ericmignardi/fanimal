import { Facebook, Instagram, Twitter } from "lucide-react";
import { Link } from "react-router-dom";

export const Footer = () => {
  const socialLinks = [
    { to: "/", icon: Twitter },
    { to: "/", icon: Instagram },
    { to: "/", icon: Facebook },
  ];

  return (
    <footer className="h-20 md:h-12 bg-[var(--color-bg)] border-t border-t-[var(--color-border)] flex flex-col md:flex-row justify-center md:justify-between items-center gap-4 md:gap-0 p-4 z-50">
      <div className="text-sm sm:text-base md:text-base font-normal text-[var(--color-text-secondary)] text-center md:text-left">
        &copy; {new Date().getFullYear()} fanimal. All rights reserved.
      </div>
      <div className="flex items-center gap-4 text-[var(--color-accent)]">
        {socialLinks.map(({ to, icon: Icon }, i) => (
          <Link key={i} className="hover:rotate-6 transition-transform" to={to}>
            <Icon className="h-5 w-5 sm:h-6 sm:w-6" />
          </Link>
        ))}
      </div>
    </footer>
  );
};
