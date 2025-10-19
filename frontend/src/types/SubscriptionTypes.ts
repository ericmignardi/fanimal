import type { ReactNode } from "react";
import type { UserType } from "./AuthTypes";
import type { ShelterType } from "./ShelterTypes";

export type TierType = {
  price: number;
};

export type SubscriptionType = {
  id: number;
  user: UserType;
  shelter: ShelterType;
  amount: number;
  startDate: Date;
  endDate: Date;
  tier: TierType;
};

export type SubscriptionContextType = {
  subscription: SubscriptionType | null;
  subscriptions: SubscriptionType[] | null;
  subscribe: () => Promise<void>;
  findAllByUser: () => Promise<void>;
  unsubscribe: (id: number) => Promise<void>;
  isGettingOrCreatingCustomer: boolean;
  isSubscribing: boolean;
  isFindingAllByUser: boolean;
  isUnsubscribing: boolean;
};

export type SubscriptionProviderPropsType = {
  children: ReactNode;
};
