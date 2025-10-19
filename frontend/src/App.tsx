import { Routes, Route, Navigate } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import { Home } from "./pages/Home.tsx";
import { Header } from "./components/Header.tsx";
import { Footer } from "./components/Footer.tsx";
import { useAuth } from "./hooks/useAuth.tsx";
import { Shelters } from "./pages/Shelters.tsx";
import { Profile } from "./pages/Profile.tsx";
import { useEffect } from "react";
import { Loader } from "lucide-react";
import { Subscriptions } from "./pages/Subscriptions.tsx";

function App() {
  const { user, verify, isVerifying } = useAuth();

  useEffect(() => {
    verify();
  }, []);

  if (isVerifying) return <Loader className="animate-spin" />;

  return (
    <>
      <Header />
      <main className="w-full">
        <Routes>
          <Route
            path="/"
            element={!user ? <Home /> : <Navigate to="/shelters" />}
          />
          <Route path="/shelters" element={<Shelters />} />
          <Route
            path="/subscriptions"
            element={user ? <Subscriptions /> : <Navigate to="/" />}
          />
          <Route
            path="/profile"
            element={user ? <Profile /> : <Navigate to="/" />}
          />
        </Routes>
      </main>
      <Footer />
      <Toaster />
    </>
  );
}

export default App;
