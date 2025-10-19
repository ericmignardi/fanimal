import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import { BrowserRouter } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext.tsx";
import { ShelterProvider } from "./context/ShelterContext.tsx";
import { UserProvider } from "./context/UserContext.tsx";
import { SubscriptionProvider } from "./context/SubscriptionContext.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <ShelterProvider>
          <UserProvider>
            <SubscriptionProvider>
              <App />
            </SubscriptionProvider>
          </UserProvider>
        </ShelterProvider>
      </AuthProvider>
    </BrowserRouter>
  </StrictMode>
);
