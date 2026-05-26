"""
Database configuration and session management.

Default: SQLite (dev-friendly, no setup required).
Switch to MySQL by setting DATABASE_URL env var, e.g.:
  DATABASE_URL=mysql+pymysql://user:password@localhost:3306/mydb
"""

import os
from sqlalchemy import create_engine
from sqlalchemy.orm import DeclarativeBase, sessionmaker

DATABASE_URL = os.getenv(
    "DATABASE_URL",
    "sqlite:///./fastapi_poc.db",
)

# SQLite needs connect_args for thread safety; MySQL does not.
connect_args = {"check_same_thread": False} if DATABASE_URL.startswith("sqlite") else {}

engine = create_engine(DATABASE_URL, connect_args=connect_args)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)


class Base(DeclarativeBase):
    pass


def get_db():
    """FastAPI dependency that yields a DB session and closes it afterwards."""
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
