# Small Business Management SaaS

A multi-tenant SaaS web application for small and medium businesses to manage daily operations — inventory, sales, purchases, customers, suppliers, employees, expenses, payments, invoices, and analytics.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React.js + Vite + JavaScript + Bootstrap |
| Backend | Java 21 + Spring Boot 3.2.5 + Spring Security + JWT |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL 8.0 (H2 for development) |
| Build | Maven (backend), npm/Vite (frontend) |

## Project Structure

```
Small Business Management/
├── backend/                    # Spring Boot REST API
│   ├── src/main/java/com/sbm/
│   │   ├── config/            # Security, CORS, DataSeeder
│   │   ├── controller/        # REST controllers (15)
│   │   ├── dto/               # Request/Response DTOs (16)
│   │   ├── entity/            # JPA entities (15)
│   │   ├── exception/         # Custom exceptions + global handler
│   │   ├── repository/        # Spring Data JPA repositories (13)
│   │   ├── security/          # JWT, UserDetails, filters
│   │   └── service/           # Business logic (10)
│   ├── src/main/resources/
│   │   ├── application.properties         # MySQL (production)
│   │   ├── application-dev.properties     # H2 (development)
│   │   └── db/migration/                  # Flyway SQL migrations
│   ├── system.properties      # Java version for Render
│   ├── .env.example
│   └── pom.xml
├── frontend/                   # React SPA
│   ├── src/
│   │   ├── components/        # Reusable components
│   │   ├── context/           # Auth context
│   │   ├── layouts/           # Dashboard layout with sidebar
│   │   ├── pages/             # 17 page components
│   │   └── services/          # Axios API client
│   ├── vercel.json            # Vercel SPA routing
│   ├── .env.example
│   └── package.json
├── .gitignore
└── README.md
```

## Prerequisites

- Java 21
- Maven 3.9+ (Maven wrapper included)
- Node.js 18+
- MySQL 8.0 (optional — H2 used for development)
- Git

## Quick Start (Development)

### Backend (with H2 — no MySQL needed)

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Backend starts at `http://localhost:8080` with in-memory H2 database.
H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:sbm_saas`)

### Backend (with MySQL)

```bash
cd backend

# Create database
mysql -u root -p -e "CREATE DATABASE sbm_saas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Set environment variables
export DB_URL=jdbc:mysql://localhost:3306/sbm_saas
export DB_USERNAME=root
export DB_PASSWORD=your_password
export JWT_SECRET=your-secret-key-at-least-256-bits

# Run (Flyway will create all tables automatically)
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend starts at `http://localhost:5173`

## Default Accounts

| Role | Email | Password |
|------|-------|----------|
| Platform Admin | admin@sbm.com | Admin@123 |

Register a new account to create a business with owner access.

## API Endpoints

| Module | Endpoints |
|--------|----------|
| Auth | `POST /api/auth/register`, `POST /api/auth/login` |
| Business | `GET/PUT /api/business` |
| Categories | `CRUD /api/categories` |
| Products | `CRUD /api/products` |
| Inventory | `GET /api/inventory`, `GET /api/inventory/alerts` |
| Customers | `CRUD /api/customers` |
| Suppliers | `CRUD /api/suppliers` |
| Purchases | `GET/POST /api/purchases` |
| Sales | `GET/POST /api/sales` |
| Invoices | `GET /api/invoices` |
| Payments | `GET/POST /api/payments` |
| Expenses | `GET/POST/DELETE /api/expenses` |
| Employees | `CRUD /api/employees` (Owner only) |
| Dashboard | `GET /api/dashboard` |
| Notifications | `GET /api/notifications` |

## Key Features

- **Multi-Tenancy**: Each business is isolated — Business A cannot see Business B's data
- **Inventory Tracking**: Purchases auto-increase stock, Sales auto-decrease stock
- **Stock Validation**: Sales blocked when insufficient stock
- **Auto Invoice/Payment**: Created automatically with each sale
- **Dashboard Analytics**: Real-time KPIs, monthly sales charts, expense breakdown
- **Role-Based Access**: Owner, Employee, Platform Admin

## Security

- BCrypt password hashing
- JWT stateless authentication (24hr expiry)
- Role-based authorization
- Input validation with Bean Validation
- CORS configurable via environment variables
- Passwords never exposed in API responses

## Deployment

### Frontend → Vercel

1. Push to GitHub
2. Connect repo to Vercel
3. Set root directory: `frontend`
4. Build command: `npm run build`
5. Output directory: `dist`
6. Add environment variable: `VITE_API_URL=https://your-backend.onrender.com/api`

### Backend → Render (Native Java)

1. Push to GitHub
2. Create new Web Service on Render
3. Set root directory: `backend`
4. Build command: `./mvnw clean package -DskipTests`
5. Start command: `java -jar target/sbm-backend-1.0.0.jar`
6. Add environment variables:
   - `DB_URL` — your MySQL connection URL
   - `DB_USERNAME` — database username
   - `DB_PASSWORD` — database password
   - `JWT_SECRET` — secure random string (64+ chars)
   - `CORS_ALLOWED_ORIGINS` — your Vercel frontend URL
   - `SPRING_PROFILES_ACTIVE` — leave empty for MySQL (default)

### Database → MySQL (Production)

Use a managed MySQL service (e.g., PlanetScale, Railway, Aiven, or Render MySQL add-on).
