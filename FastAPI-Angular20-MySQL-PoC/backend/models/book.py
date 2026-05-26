"""
SQLAlchemy ORM model for Book.

Mirrors the Java Book entity.
Fields: id (PK, hidden), uuid, full_name, email, password, state.
"""

import enum
from sqlalchemy import Column, Integer, String, Enum
from backend.database import Base


class BookState(str, enum.Enum):
    PUBLISHED = "PUBLISHED"


class Book(Base):
    __tablename__ = "books"

    id = Column(Integer, primary_key=True, autoincrement=True)
    uuid = Column(String(36), unique=True, nullable=False, index=True)
    full_name = Column(String(255), nullable=False)
    email = Column(String(50), unique=True, nullable=False)
    password = Column(String(255), nullable=False)
    state = Column(Enum(BookState), nullable=True)

    def __repr__(self) -> str:
        return f"Book(uuid={self.uuid!r}, name={self.full_name!r}, email={self.email!r})"
