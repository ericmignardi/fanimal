import { Routes, Route, Navigate } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import { Home } from "./pages/Home.tsx";
import { Header } from "./components/Header.tsx";
import { Footer } from "./components/Footer.tsx";
import { Protected } from "./pages/Protected.tsx";
import { useAuth } from "./hooks/useAuth.tsx";

function App() {
  const { user } = useAuth();

  return (
    <div className="min-h-screen grid grid-rows-[auto_1fr_auto] grid-cols-1">
      <Header />
      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route
            path="/protected"
            element={user ? <Protected /> : <Navigate to="/" />}
          />
        </Routes>
      </main>
      <Footer />
      <Toaster />
    </div>
  );
}

export default App;
