import { PawPrint } from "lucide-react";
import { Link } from "react-router-dom";

interface FooterLink {
  to: string;
  label: string;
}

interface FooterSection {
  title: string;
  links: FooterLink[];
}

const footerLinks: FooterSection[] = [
  {
    title: "Platform",
    links: [
      { to: "/shelters", label: "Browse Shelters" },
      { to: "#", label: "Gift Cards" },
    ],
  },
  {
    title: "Shelters",
    links: [
      { to: "#", label: "Partner With Us" },
      { to: "#", label: "Login" },
    ],
  },
  {
    title: "Legal",
    links: [
      { to: "#", label: "Privacy Policy" },
      { to: "#", label: "Terms" },
    ],
  },
];

const FooterColumn = ({ title, links }: FooterSection) => (
  <div className="flex flex-col justify-center gap-4">
    <h4 className="text-xl font-bold text-yellow-400 uppercase">{title}</h4>
    <ul className="flex flex-col gap-2 text-sm font-medium">
      {links.map((link) => (
        <li key={link.label}>
          <Link to={link.to}>{link.label}</Link>
        </li>
      ))}
    </ul>
  </div>
);

export const Footer = () => {
  return (
    <footer className="w-full bg-black pt-16 pb-8 text-white">
      <div className="mx-auto flex max-w-7xl flex-col justify-center gap-12 px-4">
        {/* Top */}
        <div className="grid grid-cols-1 gap-12 border-b-2 border-b-gray-800 pb-12 md:grid-cols-4">
          {/* Brand */}
          <div className="flex flex-col justify-center gap-4">
            <div className="flex items-center gap-2">
              <PawPrint
                className="text-yellow-400"
                size={36}
                aria-hidden="true"
              />
              <span className="text-2xl font-bold uppercase">fanimal</span>
            </div>
            <p className="text-sm text-gray-400">
              Connecting animal lovers with the rescues that need them most.
            </p>
          </div>

          {/* Link Columns */}
          {footerLinks.map((section) => (
            <FooterColumn key={section.title} {...section} />
          ))}
        </div>

        {/* Bottom */}
        <div>
          <small className="text-xs text-gray-500">
            &copy; {new Date().getFullYear()} fanimal
          </small>
        </div>
      </div>
    </footer>
  );
};
