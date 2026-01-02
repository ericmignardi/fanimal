import { createContext, useState } from "react";
import type {
  SubscriptionContextType,
  SubscriptionProviderPropsType,
  SubscriptionType,
} from "../types/SubscriptionTypes";
import toast from "react-hot-toast";
import { axiosInstance } from "../services/api";

export const SubscriptionContext = createContext<SubscriptionContextType>({
  subscription: null,
  subscriptions: null,
  subscribe: async () => {},
  findAllByUser: async () => {},
  unsubscribe: async () => {},
  isGettingOrCreatingCustomer: false,
  isSubscribing: false,
  isFindingAllByUser: false,
  isUnsubscribing: false,
});

export function SubscriptionProvider({
  children,
}: SubscriptionProviderPropsType) {
  const [subscription, setSubscription] = useState<SubscriptionType | null>(
    null
  );
  const [subscriptions, setSubscriptions] = useState<SubscriptionType[] | null>(
    null
  );
  const [isSubscribing, setIsSubscribing] = useState<boolean>(false);
  const [isFindingAllByUser, setIsFindingAllByUser] = useState<boolean>(false);
  const [isUnsubscribing, setIsUnsubscribing] = useState<boolean>(false);

  const subscribe = async () => {
    setIsSubscribing(true);
    try {
      const response = await axiosInstance.post("/subscriptions/subscribe");
      if (response.status === 200) {
        setSubscription(response.data);
        toast.success("Subscribe successful!");
      } else {
        toast.error("Subscribe failed.");
      }
    } catch (error) {
      console.error("Error in subscribe: ", error);
      if (error instanceof Error) {
        toast.error(`Unable to subscribe: ${error.message}`);
      } else {
        toast.error("Unable to subscribe");
      }
    } finally {
      setIsSubscribing(false);
    }
  };

  const findAllByUser = async () => {
    setIsFindingAllByUser(true);
    try {
      const response = await axiosInstance.get("/subscriptions");
      if (response.status === 200) {
        setSubscriptions(response.data);
        toast.success("Find all by user successful!");
      } else {
        toast.error("Find all by user failed.");
      }
    } catch (error) {
      console.error("Error in findAllByUser: ", error);
      if (error instanceof Error) {
        toast.error(`Unable to find all by user: ${error.message}`);
      } else {
        toast.error("Unable to find all by user");
      }
    } finally {
      setIsFindingAllByUser(false);
    }
  };

  const unsubscribe = async (id: number) => {
    setIsUnsubscribing(true);
    try {
      const response = await axiosInstance.delete(`/subscriptions/${id}`);
      if (response.status === 204) {
        setSubscriptions((prev) => prev?.filter((s) => s.id !== id) ?? null);
        toast.success("Unsubscribe successful!");
      } else {
        toast.error("Unsubscribe failed.");
      }
    } catch (error) {
      console.error("Error in unsubscribe: ", error);
      if (error instanceof Error) {
        toast.error(`Unable to unsubscribe: ${error.message}`);
      } else {
        toast.error("Unable to unsubscribe");
      }
    } finally {
      setIsUnsubscribing(false);
    }
  };

  return (
    <SubscriptionContext.Provider
      value={{
        subscription,
        subscriptions,
        subscribe,
        findAllByUser,
        unsubscribe,
        isGettingOrCreatingCustomer: false,
        isSubscribing,
        isFindingAllByUser,
        isUnsubscribing,
      }}
    >
      {children}
    </SubscriptionContext.Provider>
  );
}
