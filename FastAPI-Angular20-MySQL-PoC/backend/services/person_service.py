"""
PersonService — Python equivalent of PersonServiceImpl.java.

Handles all business logic for Person (User) CRUD operations.
"""

import uuid as uuid_lib
import re
import bcrypt
from typing import List, Optional
from sqlalchemy.orm import Session

from backend.models.person import Person
from backend.schemas.person import NewPersonRequest, UpdatePersonRequest
from backend.exceptions import PersonAlreadyExistException, PersonNotFoundException

UUID_REGEX = re.compile(
    r"^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$"
)

_DEFAULT_PASSWORD = bcrypt.hashpw(b"123456", bcrypt.gensalt()).decode()


def _validate_uuid(key: str) -> None:
    if not key or not UUID_REGEX.match(key):
        raise ValueError(f"'{key}' is not a valid UUID")


def create_person(db: Session, request: NewPersonRequest) -> Person:
    """Create a new Person. Raises PersonAlreadyExistException if email is taken."""
    if db.query(Person).filter(Person.email == request.email).first():
        raise PersonAlreadyExistException(
            f"Person with email {request.email} already exists"
        )

    person = Person(
        uuid=str(uuid_lib.uuid4()),
        full_name=request.name,
        email=request.email,
        password=_DEFAULT_PASSWORD,
    )
    db.add(person)
    db.commit()
    db.refresh(person)
    return person


def get_all_persons(db: Session) -> List[Person]:
    return db.query(Person).all()


def get_person_by_uuid(db: Session, person_uuid: str) -> Optional[Person]:
    _validate_uuid(person_uuid)
    return db.query(Person).filter(Person.uuid == person_uuid).first()


def update_person(db: Session, person_uuid: str, request: UpdatePersonRequest) -> Person:
    """Partial update. Raises PersonNotFoundException / PersonAlreadyExistException."""
    _validate_uuid(person_uuid)
    person = db.query(Person).filter(Person.uuid == person_uuid).first()
    if person is None:
        raise PersonNotFoundException(f"Person with uuid {person_uuid} not found")

    if request.name:
        person.full_name = request.name

    if request.email:
        conflict = db.query(Person).filter(Person.email == request.email).first()
        if conflict and conflict.uuid != person_uuid:
            raise PersonAlreadyExistException(
                f"Person with email {request.email} already exists"
            )
        person.email = request.email

    db.commit()
    db.refresh(person)
    return person


def delete_person_by_uuid(db: Session, person_uuid: str) -> None:
    """Delete a Person by UUID. Raises PersonNotFoundException if not found."""
    _validate_uuid(person_uuid)
    person = db.query(Person).filter(Person.uuid == person_uuid).first()
    if person is None:
        raise PersonNotFoundException(f"Person with uuid {person_uuid} not found")
    db.delete(person)
    db.commit()


def delete_all_persons(db: Session) -> None:
    db.query(Person).delete()
    db.commit()
