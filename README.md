# 🐾 fanimal - Animal Shelter Subscription Platform

<div align="center">

[![Live Demo](https://img.shields.io/badge/demo-live-success?style=for-the-badge)](https://fanimal-fui5.onrender.com)
[![GitHub](https://img.shields.io/badge/github-source-blue?style=for-the-badge&logo=github)](https://github.com/ericmignardi/fanimal)

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-6DB33F?style=flat&logo=spring&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?style=flat&logo=react&logoColor=black)
![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6?style=flat&logo=typescript&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon-4169E1?style=flat&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat&logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Deployed-Render-46E3B7?style=flat&logo=render&logoColor=white)

</div>

> **📌 Portfolio Demo** — This is a production-ready full-stack application showcasing Java/Spring Boot backend with React frontend. Uses Stripe test mode for payments.

---

## 🎯 What It Does

**fanimal** connects animal lovers with local shelters through subscription-based support. Users can browse shelters, choose subscription tiers (Basic, Standard, Premium), and provide recurring financial support to their favorite shelters.

### The Problem I Solved

Many animal shelters struggle with consistent funding. **fanimal** provides a platform where supporters can commit to monthly donations across multiple tiers, giving shelters predictable income while donors get a seamless subscription management experience.

---

## 🔥 Technical Highlights

### JWT Authentication with Spring Security

Secure authentication using JWT tokens with BCrypt password hashing:

```java
// From: security/SecurityConfiguration.java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(req -> {
                req.requestMatchers("/api/auth/**").permitAll();
                req.requestMatchers("/api/shelters/**").permitAll();
                req.requestMatchers("/api/webhooks/stripe").permitAll();
                req.anyRequest().authenticated();
            })
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
}
```

### Stripe Subscription Integration

Full subscription lifecycle management with Stripe:

```java
// From: service/SubscriptionService.java
public SubscriptionResponse subscribe(UserDetails userDetails, SubscriptionRequest request)
        throws StripeException {
    // Get or create Stripe customer
    Customer customer = getOrCreateCustomer(userDetails);

    // Attach payment method to customer
    PaymentMethod pm = PaymentMethod.retrieve(request.getPaymentMethodId());
    pm.attach(PaymentMethodAttachParams.builder()
            .setCustomer(customer.getId())
            .build());

    // Determine Stripe price ID based on selected tier
    String priceId = switch (request.getTier()) {
        case BASIC -> shelter.getStripeBasicPriceId();
        case STANDARD -> shelter.getStripeStandardPriceId();
        case PREMIUM -> shelter.getStripePremiumPriceId();
    };

    // Create Stripe subscription
    Subscription stripeSubscription = Subscription.create(
        SubscriptionCreateParams.builder()
            .setCustomer(customer.getId())
            .addItem(SubscriptionCreateParams.Item.builder()
                    .setPrice(priceId)
                    .build())
            .setPaymentBehavior(PaymentBehavior.DEFAULT_INCOMPLETE)
            .addExpand("latest_invoice.payment_intent")
            .build()
    );

    // Save subscription locally and return response
    return saveAndReturnSubscription(stripeSubscription, user, shelter);
}
```

### React Context for Global State

Type-safe context providers for authentication and subscriptions:

```typescript
// From: context/AuthContext.tsx
export const AuthContext = createContext<AuthContextType>({
  user: null,
  token: null,
  login: async () => {},
  register: async () => {},
  logout: () => {},
  verify: async () => {},
  isLoading: false,
});

export function AuthProvider({ children }: AuthProviderPropsType) {
  const [user, setUser] = useState<UserType | null>(null);
  const [token, setToken] = useState<string | null>(() =>
    localStorage.getItem("token")
  );

  const login = async (username: string, password: string) => {
    const response = await axiosInstance.post("/auth/login", {
      username,
      password,
    });
    setUser(response.data.user);
    setToken(response.data.token);
    localStorage.setItem("token", response.data.token);
  };

  // ... verify, register, logout implementations
}
```

---

## 🛠️ Technology Stack

<table>
<tr>
<td width="50%" valign="top">

### Frontend

- **Framework:** React 19 + Vite
- **Language:** TypeScript
- **Styling:** Tailwind CSS
- **Routing:** React Router DOM
- **Forms:** React Hook Form + Zod
- **HTTP Client:** Axios

</td>
<td width="50%" valign="top">

### Backend

- **Framework:** Spring Boot 3
- **Language:** Java 21
- **Database:** PostgreSQL (Neon)
- **ORM:** Spring Data JPA / Hibernate
- **Authentication:** JWT + Spring Security
- **Payments:** Stripe API

</td>
</tr>
</table>

### Database Schema

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│      User       │     │   Subscription  │     │     Shelter     │
├─────────────────┤     ├─────────────────┤     ├─────────────────┤
│ id              │     │ id              │     │ id              │
│ name            │     │ user_id (FK)    │────▶│ name            │
│ email           │     │ shelter_id (FK) │     │ description     │
│ username        │     │ tier            │     │ address         │
│ password        │     │ status          │     │ owner_id (FK)   │
│ stripe_customer │     │ start_date      │     │ stripe_basic_id │
│ roles           │     │ stripe_sub_id   │     │ stripe_std_id   │
└─────────────────┘     └─────────────────┘     │ stripe_prem_id  │
                                                └─────────────────┘
```

---

## 💼 Skills Demonstrated

| Category         | Technologies & Patterns                                                |
| ---------------- | ---------------------------------------------------------------------- |
| **Backend**      | Spring Boot, REST APIs, JPA/Hibernate, DTO pattern, Service layer      |
| **Frontend**     | React, TypeScript, Context API, Custom Hooks, Form validation with Zod |
| **Database**     | PostgreSQL, Relational schema design, JPA relationships                |
| **Security**     | JWT authentication, BCrypt hashing, Spring Security filter chain       |
| **Integrations** | Stripe Payments (subscriptions, webhooks, customers)                   |
| **DevOps**       | Docker, Docker Compose, Multi-stage builds, Environment management     |
| **Deployment**   | Render (backend + frontend), Neon PostgreSQL                           |

---

## 🧪 Try It Out

**[Live Demo →](https://fanimal-fui5.onrender.com)**

**Test Payment (Stripe Test Mode):**

- Card: `4242 4242 4242 4242`
- Expiry: Any future date
- CVC: Any 3 digits

---

## 📊 Architecture Overview

```
User Request → React Frontend (Vite)
                    ↓
            Axios HTTP Client
                    ↓
       Spring Boot REST API (8080)
                    ↓
    ┌───────────────┼───────────────┐
    ↓               ↓               ↓
JWT Filter    Service Layer    Stripe API
                    ↓
           JPA / Hibernate
                    ↓
        PostgreSQL (Neon)
                    ↓
           JSON Response
```

---

## 🐳 Docker Development

```bash
# Clone and setup
git clone https://github.com/ericmignardi/fanimal.git
cd fanimal

# Configure environment
cp .env.example .env
# Add your API keys for Stripe, JWT secret, database credentials

# Run with Docker Compose
docker compose up -d --build

# Access the application
# Frontend: http://localhost:5173
# Backend:  http://localhost:8080
```

### Available Commands

| Command                     | Description             |
| --------------------------- | ----------------------- |
| `docker compose up -d`      | Start all services      |
| `docker compose down`       | Stop all services       |
| `docker compose down -v`    | Stop and remove volumes |
| `docker compose logs -f`    | Follow container logs   |
| `docker compose up --build` | Rebuild and start       |

---

## 💰 Subscription Tiers

| Tier         | Price  | Description                        |
| ------------ | ------ | ---------------------------------- |
| **Basic**    | $9.99  | Basic monthly support              |
| **Standard** | $14.99 | Enhanced monthly support           |
| **Premium**  | $19.99 | Premium monthly support with perks |

---

## 🚀 Local Development (Without Docker)

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

---

## 📄 License

MIT License — See [LICENSE](LICENSE) for details.

---

<div align="center">

**Built by Eric Mignardi**

[Live Demo](https://fanimal-fui5.onrender.com) • [GitHub](https://github.com/ericmignardi/fanimal) • [LinkedIn](https://linkedin.com/in/ericmignardi)

</div>
