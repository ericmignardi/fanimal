# 🐾 fanimal - Full Stack Interview Study Guide

> **A comprehensive reference document for reviewing your portfolio project in preparation for Full Stack Software Developer interviews.**

---

## 📋 Table of Contents

1. [Project Overview](#-project-overview)
2. [Architecture & System Design](#-architecture--system-design)
3. [Tech Stack Deep Dive](#-tech-stack-deep-dive)
4. [Database Design](#-database-design)
5. [API Design & Backend Patterns](#-api-design--backend-patterns)
6. [Authentication & Authorization](#-authentication--authorization)
7. [Third-Party Integrations](#-third-party-integrations)
8. [Frontend Patterns](#-frontend-patterns)
9. [DevOps & Deployment](#-devops--deployment)
10. [Practice Interview Questions](#-practice-interview-questions)
11. [Code Snippets Reference](#-code-snippets-reference)

---

## 🎯 Project Overview

**fanimal** is a full-stack subscription platform that connects animal lovers with local shelters. Users can browse shelters, select subscription tiers, and provide recurring monthly support via Stripe payments.

### Key Features

- 🔐 **JWT Authentication** - Secure login/register with Spring Security
- 💳 **Stripe Subscriptions** - Tiered pricing (Basic $9.99, Standard $14.99, Premium $19.99)
- 🏠 **Shelter Management** - Browse and support animal shelters
- 📱 **Responsive UI** - React + Tailwind CSS with neo-brutalism design
- 🐳 **Dockerized** - Full Docker Compose setup for local development

### Live Demo

- **Frontend**: [https://fanimal-fui5.onrender.com](https://fanimal-fui5.onrender.com)
- **Backend**: [https://fanimal.onrender.com](https://fanimal.onrender.com)
- **Repo**: [https://github.com/ericmignardi/fanimal](https://github.com/ericmignardi/fanimal)

---

## 🏗 Architecture & System Design

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENT                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  React + TypeScript + Vite                                │  │
│  │  • Context API for state                                  │  │
│  │  • React Router for navigation                            │  │
│  │  • Axios for HTTP                                         │  │
│  └────────────────────────┬─────────────────────────────────┘  │
└───────────────────────────┼─────────────────────────────────────┘
                            │ REST API (JSON)
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                       SPRING BOOT                               │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Controllers → Services → Repositories                   │  │
│  │  • JWT Authentication Filter                              │  │
│  │  • Spring Security                                        │  │
│  │  • Spring Data JPA                                        │  │
│  └────────────────────────┬─────────────────────────────────┘  │
└───────────────────────────┼─────────────────────────────────────┘
                            │ JPA/Hibernate
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                      POSTGRESQL (Neon)                          │
│  • Users, Roles, Shelters, Subscriptions                        │
└─────────────────────────────────────────────────────────────────┘
```

### Key Architectural Decisions

| Decision       | Choice                | Why                                               |
| -------------- | --------------------- | ------------------------------------------------- |
| **Backend**    | Spring Boot 3         | Enterprise-grade, excellent security, JPA support |
| **Frontend**   | React + Vite          | Fast development, TypeScript, hot reload          |
| **Database**   | PostgreSQL (Neon)     | Relational data, ACID, serverless scaling         |
| **Auth**       | JWT + Spring Security | Stateless, scalable, industry standard            |
| **Payments**   | Stripe                | Robust subscription API, webhook support          |
| **Deployment** | Docker + Render       | Container isolation, easy CI/CD                   |

### Interview Talking Points

> _"I chose Spring Boot for its mature ecosystem and built-in security features. For a payment-handling application, the enterprise-grade transaction support and Spring Security's filter chain were important factors."_

> _"The frontend uses React Context for state management - appropriate for this app's complexity without introducing Redux overhead. Each domain (auth, shelters, subscriptions) has its own context."_

---

## 🛠 Tech Stack Deep Dive

### Backend Stack

| Technology          | Purpose               | Key Files                               |
| ------------------- | --------------------- | --------------------------------------- |
| **Spring Boot 3**   | Framework             | `FanimalApplication.java`               |
| **Java 21**         | Language              | All `.java` files                       |
| **Spring Security** | Authentication        | `SecurityConfiguration.java`            |
| **Spring Data JPA** | ORM                   | `*Repository.java`                      |
| **JWT (jjwt)**      | Token generation      | `JwtUtils.java`, `AuthTokenFilter.java` |
| **Stripe Java**     | Payments              | `SubscriptionService.java`              |
| **Lombok**          | Boilerplate reduction | `@Data`, `@Builder` annotations         |
| **Maven**           | Build tool            | `pom.xml`                               |

### Frontend Stack

| Technology          | Purpose         | Key Files                    |
| ------------------- | --------------- | ---------------------------- |
| **React 19**        | UI Framework    | `App.tsx`, components        |
| **TypeScript**      | Type safety     | All `.tsx` files             |
| **Vite**            | Build tool      | `vite.config.ts`             |
| **Tailwind CSS**    | Styling         | `index.css`, utility classes |
| **React Router**    | Routing         | `App.tsx`                    |
| **React Hook Form** | Form management | Subscription forms           |
| **Zod**             | Validation      | Schema definitions           |
| **Axios**           | HTTP client     | `api.ts`                     |

---

## 💾 Database Design

### Entity Relationship Diagram

```
┌─────────────────┐         ┌─────────────────┐
│      USER       │         │      ROLE       │
├─────────────────┤         ├─────────────────┤
│ id (PK)         │         │ id (PK)         │
│ name            │◄───────►│ name (UK)       │
│ email (UK)      │  M:M    │                 │
│ username (UK)   │         └─────────────────┘
│ password        │
│ stripeCustomerId│
└────────┬────────┘
         │
         │ 1:N (owner)            1:N (subscriber)
         ▼                        ▼
┌─────────────────┐         ┌─────────────────┐
│     SHELTER     │◄────────│  SUBSCRIPTION   │
├─────────────────┤  1:N    ├─────────────────┤
│ id (PK)         │         │ id (PK)         │
│ name            │         │ userId (FK)     │
│ description     │         │ shelterId (FK)  │
│ address         │         │ tier (ENUM)     │
│ ownerId (FK)    │         │ status (ENUM)   │
│ stripeBasicId   │         │ startDate       │
│ stripeStdId     │         │ endDate         │
│ stripePremId    │         │ stripeSubId     │
└─────────────────┘         └─────────────────┘
```

### Key Database Concepts

**1. Enum Types**

```java
public enum Tier {
    BASIC(new BigDecimal("9.99")),
    STANDARD(new BigDecimal("14.99")),
    PREMIUM(new BigDecimal("19.99"));
}

public enum SubscriptionStatus {
    ACTIVE, INCOMPLETE, PAST_DUE, CANCELED, UNPAID
}
```

**2. JPA Relationships**

```java
// User entity
@ManyToMany(fetch = FetchType.EAGER)
@JoinTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
)
private Set<Role> roles = new HashSet<>();

// Subscription entity
@ManyToOne
@JoinColumn(name = "user_id")
private User user;

@ManyToOne
@JoinColumn(name = "shelter_id")
private Shelter shelter;
```

---

## 🔌 API Design & Backend Patterns

### REST Endpoints

| Endpoint                       | Method | Auth | Description           |
| ------------------------------ | ------ | ---- | --------------------- |
| `/api/auth/register`           | POST   | ❌   | Register new user     |
| `/api/auth/login`              | POST   | ❌   | Login, returns JWT    |
| `/api/auth/verify`             | GET    | ✅   | Verify token validity |
| `/api/shelters`                | GET    | ❌   | List all shelters     |
| `/api/shelters/{id}`           | GET    | ❌   | Get shelter details   |
| `/api/subscriptions`           | GET    | ✅   | User's subscriptions  |
| `/api/subscriptions/subscribe` | POST   | ✅   | Create subscription   |
| `/api/subscriptions/{id}`      | DELETE | ✅   | Cancel subscription   |

### Layered Architecture

```
Controller Layer (HTTP handling)
    ↓
Service Layer (Business logic)
    ↓
Repository Layer (Data access)
    ↓
Entity Layer (Domain models)
```

### DTO Pattern

```java
// Request DTO - what client sends
public record RegisterRequest(
    @NotBlank String name,
    @Email String email,
    @NotBlank String username,
    @Size(min = 8) String password
) {}

// Response DTO - what we return
public record UserResponse(Long id, String name, String email, String username) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(user.getId(), user.getName(),
                                user.getEmail(), user.getUsername());
    }
}
```

---

## 🔐 Authentication & Authorization

### JWT Flow

```
1. User POSTs credentials to /api/auth/login
2. AuthService validates against database
3. On success, JwtUtils generates signed token
4. Token returned to client, stored in localStorage
5. Client includes token in Authorization header
6. AuthTokenFilter validates token on each request
7. SecurityContextHolder populated with user details
```

### Security Configuration

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
        .authorizeHttpRequests(req -> {
            req.requestMatchers("/api/auth/**").permitAll();
            req.requestMatchers("/api/shelters/**").permitAll();
            req.anyRequest().authenticated();
        })
        .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
}
```

### Password Hashing

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// Usage in AuthService
String encodedPassword = passwordEncoder.encode(request.password());
user.setPassword(encodedPassword);
```

---

## 🔗 Third-Party Integrations

### Stripe Integration

**Customer Creation**

```java
public Customer getOrCreateCustomer(UserDetails userDetails) throws StripeException {
    User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));

    if (user.getStripeCustomerId() != null) {
        return Customer.retrieve(user.getStripeCustomerId());
    }

    CustomerCreateParams params = CustomerCreateParams.builder()
            .setEmail(user.getEmail())
            .setName(user.getName())
            .build();

    Customer customer = Customer.create(params);
    user.setStripeCustomerId(customer.getId());
    userRepository.save(user);
    return customer;
}
```

**Subscription Creation**

```java
SubscriptionCreateParams params = SubscriptionCreateParams.builder()
    .setCustomer(customer.getId())
    .addItem(SubscriptionCreateParams.Item.builder()
            .setPrice(priceId)
            .build())
    .setPaymentBehavior(PaymentBehavior.DEFAULT_INCOMPLETE)
    .addExpand("latest_invoice.payment_intent")
    .build();

Subscription subscription = Subscription.create(params);
```

---

## ⚛️ Frontend Patterns

### Context API Structure

```typescript
// AuthContext.tsx
export interface AuthContextType {
  user: UserType | null;
  token: string | null;
  login: (username: string, password: string) => Promise<void>;
  register: (data: RegisterData) => Promise<void>;
  logout: () => void;
  verify: () => Promise<void>;
  isLoading: boolean;
}

export function AuthProvider({ children }: Props) {
  const [user, setUser] = useState<UserType | null>(null);
  const [token, setToken] = useState<string | null>(() =>
    localStorage.getItem("token")
  );

  // Verify on mount if token exists
  useEffect(() => {
    if (token) verify();
  }, []);

  const login = async (username: string, password: string) => {
    const response = await axiosInstance.post("/auth/login", {
      username,
      password,
    });
    setUser(response.data.user);
    setToken(response.data.token);
    localStorage.setItem("token", response.data.token);
  };

  return (
    <AuthContext.Provider
      value={{ user, token, login, logout, verify, isLoading }}
    >
      {children}
    </AuthContext.Provider>
  );
}
```

### Axios Interceptors

```typescript
// Request interceptor - attach JWT
axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor - handle 401
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      window.location.href = "/";
    }
    return Promise.reject(error);
  }
);
```

---

## 🐳 DevOps & Deployment

### Docker Compose Architecture

```yaml
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: ${POSTGRES_DB}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}

  backend:
    build: ./backend
    depends_on: [db]
    environment:
      SPRING_DATASOURCE_URL: ${SPRING_DATASOURCE_URL}
      JWT_SECRET: ${JWT_SECRET}
      STRIPE_API_KEY: ${STRIPE_API_KEY}

  frontend:
    build:
      context: ./frontend
      args:
        VITE_BACKEND_URL: ${VITE_BACKEND_URL}
    depends_on: [backend]
```

### Multi-Stage Dockerfile (Backend)

```dockerfile
# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar fanimal.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "fanimal.jar"]
```

### Environment Management

| Environment | DB              | Frontend                  | Backend              |
| ----------- | --------------- | ------------------------- | -------------------- |
| **Local**   | Docker Postgres | localhost:5173            | localhost:8080       |
| **Render**  | Neon PostgreSQL | fanimal-fui5.onrender.com | fanimal.onrender.com |

---

## ❓ Practice Interview Questions

### System Design

**Q: Walk me through the authentication flow.**

> _"When a user logs in, they POST credentials to /api/auth/login. The AuthService looks up the user by username and uses BCryptPasswordEncoder to verify the password. On success, JwtUtils generates a signed JWT containing the username and expiration. The client stores this token in localStorage and includes it in the Authorization header for subsequent requests. A custom AuthTokenFilter intercepts every request, validates the token, and populates Spring Security's context."_

**Q: How do you handle subscription payments?**

> _"We integrate with Stripe's Subscription API. The flow: user selects a tier, frontend creates a PaymentMethod via Stripe.js, sends it to our backend. We get-or-create a Stripe Customer (storing the ID in our User entity), attach the payment method, then create a Subscription with the appropriate price ID from the Shelter entity. We save our local Subscription record with the stripeSubscriptionId for future reference."_

### Database

**Q: Explain your database relationships.**

> _"Users have many-to-many with Roles via a join table. Shelters belong to an owner User (many-to-one). Subscriptions link Users to Shelters - a user can have multiple subscriptions to different shelters, each with its own tier and status."_

**Q: Why store Stripe IDs rather than querying Stripe by email?**

> _"Storing stripeCustomerId and stripeSubscriptionId provides a direct link to Stripe resources. This is faster than lookup-by-email, handles edge cases like users changing emails, and is essential for webhook handlers that receive Stripe IDs and need to find the corresponding local record."_

### Backend

**Q: Why Spring Boot over Node.js?**

> _"Spring Boot provides enterprise features out of the box: dependency injection, transaction management, JPA for type-safe database access, and battle-tested security with Spring Security. For a payment platform, the maturity and security guarantees were important."_

**Q: Explain the DTO pattern.**

> _"DTOs separate our API contract from internal entities. Request DTOs validate incoming data with Jakarta validation annotations. Response DTOs control exactly what data leaves the API, preventing accidental exposure of sensitive fields like password hashes. The static fromEntity() factory method keeps conversion logic with the DTO."_

### Frontend

**Q: Why Context API over Redux?**

> _"For this application's complexity, Context is sufficient. We have three domains (auth, shelters, subscriptions), each with its own context. Redux would add boilerplate without proportional benefit. If the app grew to need normalized cache, complex async actions, or time-travel debugging, Redux would become worthwhile."_

**Q: How do you handle authentication state on page refresh?**

> _"The AuthProvider initializes token from localStorage. On mount, if a token exists, we call verify() which hits /api/auth/verify to validate and fetch fresh user data. This handles both persisting sessions and detecting expired tokens."_

### DevOps

**Q: How does your Docker setup work?**

> _"We use multi-stage builds. Backend: Maven builds the JAR in one stage, then we copy just the artifact to a slim JDK runtime image. Frontend: Node builds the static bundle, then we copy to nginx. Docker Compose orchestrates all three services with proper depends_on ordering and environment variable injection via the .env file."_

---

## 📝 Quick Reference Card

### Tech Stack Summary

| Layer    | Technology                                   |
| -------- | -------------------------------------------- |
| Frontend | React 19, TypeScript, Vite, Tailwind CSS     |
| Backend  | Spring Boot 3, Java 21, Spring Security, JPA |
| Database | PostgreSQL (Neon)                            |
| Auth     | JWT + BCrypt                                 |
| Payments | Stripe Subscriptions                         |
| DevOps   | Docker, Docker Compose, Render               |

### Key Design Patterns

- **DTO Pattern** → Separate API contract from entities
- **Repository Pattern** → Spring Data JPA interfaces
- **Context Pattern** → React global state management
- **Filter Pattern** → JWT authentication filter chain
- **Builder Pattern** → Lombok @Builder for entities

### Elevator Pitch (30 seconds)

> _"fanimal is a full-stack subscription platform I built connecting animal lovers with local shelters. The backend uses Spring Boot 3 with JWT authentication and Stripe for payments. The frontend is React with TypeScript using Context API for state. PostgreSQL stores the data. It's fully Dockerized and deployed on Render. Key features include tiered subscriptions, secure payment handling, and responsive UI with Tailwind."_

---

## 📁 Key Files Reference

| File                         | Purpose                  |
| ---------------------------- | ------------------------ |
| `SecurityConfiguration.java` | JWT filter chain, CORS   |
| `AuthTokenFilter.java`       | Token validation         |
| `JwtUtils.java`              | Token generation/parsing |
| `AuthService.java`           | Login/register logic     |
| `SubscriptionService.java`   | Stripe integration       |
| `AuthContext.tsx`            | React auth state         |
| `api.ts`                     | Axios configuration      |
| `docker-compose.yml`         | Container orchestration  |
| `Dockerfile` (backend)       | Multi-stage Java build   |
| `Dockerfile.prod` (frontend) | Multi-stage nginx build  |

---

_Good luck with your interviews! 🚀_
