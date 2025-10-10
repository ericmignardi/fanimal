import { Facebook, Instagram, Twitter } from "lucide-react";
import { Link } from "react-router-dom";

export const Footer = () => {
  return (
    <footer className="h-30 md:h-20 border-t border-t-[var(--color-border)] flex flex-col md:flex-row justify-center md:justify-between items-center gap-4 md:gap-0 p-4">
      <div className="text-xs sm:text-sm md:text-base font-medium text-[var(--color-text-secondary)] text-center md:text-left">
        &copy; {new Date().getFullYear()} fanimal. All rights reserved.
      </div>

      <div className="flex items-center gap-4 text-[var(--color-accent)]">
        <Link className="hover:rotate-6 transition-transform" to="/">
          <Twitter className="h-5 w-5 sm:h-6 sm:w-6" />
        </Link>
        <Link className="hover:rotate-6 transition-transform" to="/">
          <Instagram className="h-5 w-5 sm:h-6 sm:w-6" />
        </Link>
        <Link className="hover:rotate-6 transition-transform" to="/">
          <Facebook className="h-5 w-5 sm:h-6 sm:w-6" />
        </Link>
      </div>
    </footer>
  );
};
