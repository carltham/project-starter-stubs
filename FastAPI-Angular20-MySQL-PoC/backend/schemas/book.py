"""
Pydantic schemas for Book request/response serialization.
"""

from __future__ import annotations
from typing import Optional
from pydantic import BaseModel, EmailStr, Field
from backend.models.book import BookState


# ─── Request bodies ──────────────────────────────────────────────────────────

class NewBookRequest(BaseModel):
    """Body for POST /api/books — create a new book entry."""
    name: str = Field(..., min_length=3, max_length=50)
    email: EmailStr = Field(..., max_length=50)


class UpdateBookRequest(BaseModel):
    """Body for PUT /api/books/{uuid} — partial update."""
    name: Optional[str] = Field(None, min_length=3, max_length=50)
    email: Optional[EmailStr] = Field(None, max_length=50)
    # password update is intentionally omitted (mirrors Java TODO)


# ─── Response body ───────────────────────────────────────────────────────────

class BookResponse(BaseModel):
    """Serialized Book returned by the API (id is excluded).

    `validation_alias` lets Pydantic read the ORM's `full_name` attribute
    but serialize the field as `name` in the JSON response.
    """
    uuid: str
    name: str = Field(validation_alias="full_name")
    email: str
    state: Optional[BookState] = None

    model_config = {"from_attributes": True, "populate_by_name": True}
