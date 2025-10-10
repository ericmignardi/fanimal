import { Routes, Route, Navigate } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import { Home } from "./pages/Home.tsx";
import { Header } from "./components/Header.tsx";
import { Footer } from "./components/Footer.tsx";
import { useAuth } from "./hooks/useAuth.tsx";
import { Explore } from "./pages/Explore.tsx";
import { Profile } from "./pages/Profile.tsx";

function App() {
  const { user } = useAuth();

  return (
    <div className="min-h-screen grid grid-rows-[auto_1fr_auto] grid-cols-1">
      <Header />
      <main className="w-full">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/explore" element={<Explore />} />
          <Route
            path="/profile"
            element={user ? <Profile /> : <Navigate to="/" />}
          />
        </Routes>
      </main>
      <Footer />
      <Toaster />
    </div>
  );
}

export default App;
