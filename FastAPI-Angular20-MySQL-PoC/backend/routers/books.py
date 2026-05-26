"""
Books router — equivalent of APIBookController.java.

Endpoints:
  POST   /api/books              create a book
  GET    /api/books              list all books
  GET    /api/books/published    list published books only
  GET    /api/books/{uuid}       get one book
  PUT    /api/books/{uuid}       update a book
  DELETE /api/books/{uuid}       delete one book
  DELETE /api/books              delete all books
"""

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from backend.database import get_db
from backend.schemas.book import BookResponse, NewBookRequest, UpdateBookRequest
from backend.services import book_service
from backend.exceptions import BookAlreadyExistException, BookNotFoundException

router = APIRouter(prefix="/api/books", tags=["books"])


@router.post("", response_model=BookResponse, status_code=status.HTTP_201_CREATED)
def create_book(request: NewBookRequest, db: Session = Depends(get_db)):
    try:
        return book_service.create_book(db, request)
    except BookAlreadyExistException as e:
        raise HTTPException(status_code=status.HTTP_409_CONFLICT, detail=e.message)


@router.get("", response_model=list[BookResponse])
def get_all_books(db: Session = Depends(get_db)):
    return book_service.get_all_books(db)


@router.get("/published", response_model=list[BookResponse])
def get_published_books(db: Session = Depends(get_db)):
    return book_service.get_all_published_books(db)


@router.get("/{book_uuid}", response_model=BookResponse)
def get_book(book_uuid: str, db: Session = Depends(get_db)):
    try:
        book = book_service.get_book_by_uuid(db, book_uuid)
    except ValueError as e:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail=str(e))
    if book is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Book not found")
    return book


@router.put("/{book_uuid}", response_model=BookResponse)
def update_book(book_uuid: str, request: UpdateBookRequest, db: Session = Depends(get_db)):
    try:
        return book_service.update_book(db, book_uuid, request)
    except ValueError as e:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail=str(e))
    except BookNotFoundException as e:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=e.message)
    except BookAlreadyExistException as e:
        raise HTTPException(status_code=status.HTTP_409_CONFLICT, detail=e.message)


@router.delete("/{book_uuid}", status_code=status.HTTP_204_NO_CONTENT)
def delete_book(book_uuid: str, db: Session = Depends(get_db)):
    try:
        book_service.delete_book_by_uuid(db, book_uuid)
    except ValueError as e:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail=str(e))
    except BookNotFoundException as e:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=e.message)


@router.delete("", status_code=status.HTTP_204_NO_CONTENT)
def delete_all_books(db: Session = Depends(get_db)):
    book_service.delete_all_books(db)
