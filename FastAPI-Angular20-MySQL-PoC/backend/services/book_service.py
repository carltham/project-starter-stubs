"""
BookService — Python equivalent of BookServiceImpl.java.

Handles all business logic for Book CRUD operations.
"""

import uuid as uuid_lib
import re
import bcrypt
from typing import List, Optional
from sqlalchemy.orm import Session

from backend.models.book import Book, BookState
from backend.schemas.book import NewBookRequest, UpdateBookRequest
from backend.exceptions import BookAlreadyExistException, BookNotFoundException

UUID_REGEX = re.compile(
    r"^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$"
)

# A fixed hashed default password (mirrors Java's randomPassword = BCrypt.hashpw("123456", ...))
_DEFAULT_PASSWORD = bcrypt.hashpw(b"123456", bcrypt.gensalt()).decode()


def _validate_uuid(key: str) -> None:
    if not key or not UUID_REGEX.match(key):
        raise ValueError(f"'{key}' is not a valid UUID")


def create_book(db: Session, request: NewBookRequest) -> Book:
    """Create a new Book. Raises BookAlreadyExistException if email is taken."""
    if db.query(Book).filter(Book.email == request.email).first():
        raise BookAlreadyExistException(f"Book with email {request.email} already exists")

    book = Book(
        uuid=str(uuid_lib.uuid4()),
        full_name=request.name,
        email=request.email,
        password=_DEFAULT_PASSWORD,
    )
    db.add(book)
    db.commit()
    db.refresh(book)
    return book


def get_all_books(db: Session) -> List[Book]:
    return db.query(Book).all()


def get_all_published_books(db: Session) -> List[Book]:
    return db.query(Book).filter(Book.state == BookState.PUBLISHED).all()


def get_book_by_uuid(db: Session, book_uuid: str) -> Optional[Book]:
    _validate_uuid(book_uuid)
    return db.query(Book).filter(Book.uuid == book_uuid).first()


def update_book(db: Session, book_uuid: str, request: UpdateBookRequest) -> Book:
    """Partial update. Raises BookNotFoundException / BookAlreadyExistException."""
    _validate_uuid(book_uuid)
    book = db.query(Book).filter(Book.uuid == book_uuid).first()
    if book is None:
        raise BookNotFoundException(f"Book with uuid {book_uuid} not found")

    if request.name:
        book.full_name = request.name

    if request.email:
        conflict = db.query(Book).filter(Book.email == request.email).first()
        if conflict and conflict.uuid != book_uuid:
            raise BookAlreadyExistException(
                f"Book with email {request.email} already exists"
            )
        book.email = request.email

    db.commit()
    db.refresh(book)
    return book


def delete_book_by_uuid(db: Session, book_uuid: str) -> None:
    """Delete a Book by UUID. Raises BookNotFoundException if not found."""
    _validate_uuid(book_uuid)
    book = db.query(Book).filter(Book.uuid == book_uuid).first()
    if book is None:
        raise BookNotFoundException(f"Book with uuid {book_uuid} not found")
    db.delete(book)
    db.commit()


def delete_all_books(db: Session) -> None:
    db.query(Book).delete()
    db.commit()
