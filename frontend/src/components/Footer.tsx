import { Facebook, Instagram, Twitter } from "lucide-react";
import type { ReactNode } from "react";
import { Link } from "react-router-dom";

export const Footer = (): ReactNode => {
  return (
    <footer className="h-20 border-t border-t-[var(--color-border)] flex justify-between items-center p-4">
      <div>&copy; {new Date().getFullYear()} fanimal. All rights reserved.</div>
      <div className="flex items-center gap-4">
        <Link to="/">Terms of Service</Link>
        <Link to="/">Privacy Policy</Link>
        <Link to="/">Contact Us</Link>
      </div>
      <div className="flex items-center gap-4">
        <Link to="/">
          <Twitter />
        </Link>
        <Link to="/">
          <Instagram />
        </Link>
        <Link to="/">
          <Facebook />
        </Link>
      </div>
    </footer>
  );
};
