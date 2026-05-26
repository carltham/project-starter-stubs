"""
Users router — equivalent of APIUserController.java (properly wired to PersonService).

Endpoints:
  POST   /api/users          create a user
  GET    /api/users          list all users
  GET    /api/users/{uuid}   get one user
  PUT    /api/users/{uuid}   update a user
  DELETE /api/users/{uuid}   delete one user
  DELETE /api/users          delete all users
"""

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from backend.database import get_db
from backend.schemas.person import PersonResponse, NewPersonRequest, UpdatePersonRequest
from backend.services import person_service
from backend.exceptions import PersonAlreadyExistException, PersonNotFoundException

router = APIRouter(prefix="/api/users", tags=["users"])


@router.post("", response_model=PersonResponse, status_code=status.HTTP_201_CREATED)
def create_user(request: NewPersonRequest, db: Session = Depends(get_db)):
    try:
        return person_service.create_person(db, request)
    except PersonAlreadyExistException as e:
        raise HTTPException(status_code=status.HTTP_409_CONFLICT, detail=e.message)


@router.get("", response_model=list[PersonResponse])
def get_all_users(db: Session = Depends(get_db)):
    return person_service.get_all_persons(db)


@router.get("/{person_uuid}", response_model=PersonResponse)
def get_user(person_uuid: str, db: Session = Depends(get_db)):
    try:
        person = person_service.get_person_by_uuid(db, person_uuid)
    except ValueError as e:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail=str(e))
    if person is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
    return person


@router.put("/{person_uuid}", response_model=PersonResponse)
def update_user(person_uuid: str, request: UpdatePersonRequest, db: Session = Depends(get_db)):
    try:
        return person_service.update_person(db, person_uuid, request)
    except ValueError as e:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail=str(e))
    except PersonNotFoundException as e:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=e.message)
    except PersonAlreadyExistException as e:
        raise HTTPException(status_code=status.HTTP_409_CONFLICT, detail=e.message)


@router.delete("/{person_uuid}", status_code=status.HTTP_204_NO_CONTENT)
def delete_user(person_uuid: str, db: Session = Depends(get_db)):
    try:
        person_service.delete_person_by_uuid(db, person_uuid)
    except ValueError as e:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail=str(e))
    except PersonNotFoundException as e:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=e.message)


@router.delete("", status_code=status.HTTP_204_NO_CONTENT)
def delete_all_users(db: Session = Depends(get_db)):
    person_service.delete_all_persons(db)
