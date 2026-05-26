# FastAPI-Angular20-MySQL PoC

A drop-in Python replacement for the Spring Boot backend of the `Spring-Angular20-MySQL-PoC` project.

| Layer | Technology |
|-------|-----------|
| Frontend | Angular 20 (unchanged from original) |
| Backend | **Python 3.12 + FastAPI + SQLAlchemy** |
| Database | SQLite (dev default) → MySQL (set `DATABASE_URL`) |

---

## Project structure

```
FastAPI-Angular20-MySQL-PoC/
├── Angular-Front/          # Angular 20 frontend (same as original)
│   └── proxy.conf.json     # Proxies /api/* → FastAPI on :8080
└── backend/
    ├── main.py             # FastAPI app + table creation
    ├── database.py         # SQLAlchemy engine + session dependency
    ├── exceptions.py       # Domain exceptions
    ├── models/
    │   ├── book.py         # Book ORM model (≈ Book.java)
    │   └── person.py       # Person ORM model (≈ Person.java)
    ├── schemas/
    │   ├── book.py         # Pydantic request/response schemas for books
    │   └── person.py       # Pydantic request/response schemas for users
    ├── services/
    │   ├── book_service.py    # ≈ BookServiceImpl.java
    │   └── person_service.py  # ≈ PersonServiceImpl.java
    └── routers/
        ├── books.py        # ≈ APIBookController.java  → /api/books
        └── users.py        # ≈ APIUserController.java  → /api/users
```

---

## Quick start

### 1 — Backend (FastAPI)

```bash
cd FastAPI-Angular20-MySQL-PoC

# Create a virtual environment
python3 -m venv .venv
source .venv/bin/activate

# Install dependencies
pip install -r backend/requirements.txt

# Run with hot-reload
uvicorn backend.main:app --reload --port 8080
```

Swagger UI is available at **<http://localhost:8080/docs>**

### 2 — Frontend (Angular dev server with proxy)

```bash
cd Angular-Front
npm install
ng serve          # serves on :4200, proxies /api → :8080
```

Open **<http://localhost:4200>**.

---

## API endpoints

### Books (`/api/books`)

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/books` | Create a book |
| `GET` | `/api/books` | List all books |
| `GET` | `/api/books/published` | List published books |
| `GET` | `/api/books/{uuid}` | Get one book |
| `PUT` | `/api/books/{uuid}` | Update a book |
| `DELETE` | `/api/books/{uuid}` | Delete one book |
| `DELETE` | `/api/books` | Delete all books |

### Users (`/api/users`)

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/users` | Create a user |
| `GET` | `/api/users` | List all users |
| `GET` | `/api/users/{uuid}` | Get one user |
| `PUT` | `/api/users/{uuid}` | Update a user |
| `DELETE` | `/api/users/{uuid}` | Delete one user |
| `DELETE` | `/api/users` | Delete all users |

---

## Switching to MySQL

```bash
# Install the MySQL driver
pip install pymysql

# Set the connection URL before starting uvicorn
export DATABASE_URL="mysql+pymysql://user:password@localhost:3306/poc_db"
uvicorn backend.main:app --reload --port 8080
```

---

## Production build

```bash
# Build Angular into Angular-Front/dist/
cd Angular-Front && ng build --configuration production

# FastAPI will automatically serve the Angular SPA from /
uvicorn backend.main:app --port 8080
```
