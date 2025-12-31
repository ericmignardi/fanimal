import { Routes, Route, Navigate } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import { Home } from "./pages/Home.tsx";
import { Header } from "./components/navigation/Header.tsx";
import { Footer } from "./components/navigation/Footer.tsx";
import { useAuth } from "./hooks/useAuth.ts";
import { Shelters } from "./pages/Shelters.tsx";
import { Profile } from "./pages/Profile.tsx";
import { useEffect } from "react";
import { Loader } from "lucide-react";
import { Subscriptions } from "./pages/Subscriptions.tsx";
import { ShelterDetails } from "./pages/ShelterDetails.tsx";

function App() {
  const { user, verify, isVerifying, logout } = useAuth();

  useEffect(() => {
    verify();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (isVerifying) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <Loader className="h-8 w-8 animate-spin" />
      </div>
    );
  }

  return (
    <div className="flex min-h-screen flex-col">
      <Header user={user} logout={logout} />
      <main className="flex-1 pt-[72px]">
        <Routes>
          <Route
            path="/"
            element={!user ? <Home /> : <Navigate to="/shelters" />}
          />
          <Route path="/shelters" element={<Shelters />} />
          <Route path="/shelters/:id" element={<ShelterDetails />} />
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
      <Toaster position="top-right" />
    </div>
  );
}

export default App;
