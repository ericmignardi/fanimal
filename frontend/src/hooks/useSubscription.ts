import { useContext } from "react";
import { SubscriptionContext } from "../context/SubscriptionContext";
import type { SubscriptionContextType } from "../types/SubscriptionTypes";

export const useSubscription = (): SubscriptionContextType => {
  const context = useContext(SubscriptionContext);
  if (!context) {
    throw new Error(
      "useSubscription must be used within an SubscriptionProvider"
    );
  }
  return context;
};
