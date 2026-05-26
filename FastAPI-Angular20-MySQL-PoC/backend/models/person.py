"""
SQLAlchemy ORM model for Person (User).

Mirrors the Java Person entity.
Fields: id (PK, hidden), uuid, full_name, email, password.
"""

from sqlalchemy import Column, Integer, String
from backend.database import Base


class Person(Base):
    __tablename__ = "persons"

    id = Column(Integer, primary_key=True, autoincrement=True)
    uuid = Column(String(36), unique=True, nullable=False, index=True)
    full_name = Column(String(255), nullable=False)
    email = Column(String(50), unique=True, nullable=False)
    password = Column(String(255), nullable=False)

    def __repr__(self) -> str:
        return f"Person(uuid={self.uuid!r}, name={self.full_name!r}, email={self.email!r})"
