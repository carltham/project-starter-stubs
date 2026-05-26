"""
FastAPI application entry point.

Equivalent to Spring Boot's ServingWebContentApplication.java + application.properties.

Startup:
    uvicorn backend.main:app --reload --port 8080

The Angular dev server proxies /api/* to this backend (see Angular-Front/proxy.conf.json).
For production the Angular build output can be served from the /static mount below.
"""

from pathlib import Path
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
from fastapi.responses import FileResponse

from backend.database import Base, engine
from backend.routers import books, users

# ─── Create tables ────────────────────────────────────────────────────────────
# Import models so SQLAlchemy knows about them before create_all().
import backend.models.book   # noqa: F401
import backend.models.person  # noqa: F401

Base.metadata.create_all(bind=engine)

# ─── App ──────────────────────────────────────────────────────────────────────
app = FastAPI(
    title="FastAPI-Angular-MySQL PoC",
    description="Python/FastAPI backend — drop-in replacement for the Spring Boot backend.",
    version="0.1.0",
)

# ─── CORS (allow Angular dev server on port 4200) ────────────────────────────
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4200"],
    allow_methods=["*"],
    allow_headers=["*"],
)

# ─── API routers ─────────────────────────────────────────────────────────────
app.include_router(books.router)
app.include_router(users.router)

# ─── Serve Angular build output (production) ─────────────────────────────────
_STATIC_DIR = Path(__file__).parent.parent / "Angular-Front" / "dist" / "Angular-Frontend" / "browser"

if _STATIC_DIR.exists():
    app.mount("/", StaticFiles(directory=str(_STATIC_DIR), html=True), name="static")
else:
    @app.get("/", include_in_schema=False)
    def root():
        return {
            "message": "FastAPI backend is running. "
                       "Build the Angular app and place output in Angular-Front/dist/ "
                       "to serve it here, or run 'ng serve' on port 4200."
        }
