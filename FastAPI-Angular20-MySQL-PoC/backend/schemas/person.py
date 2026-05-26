"""
Pydantic schemas for Person (User) request/response serialization.
"""

from __future__ import annotations
from typing import Optional
from pydantic import BaseModel, EmailStr, Field


# ─── Request bodies ──────────────────────────────────────────────────────────

class NewPersonRequest(BaseModel):
    """Body for POST /api/users — create a new user."""
    name: str = Field(..., min_length=3, max_length=50)
    email: EmailStr = Field(..., max_length=50)


class UpdatePersonRequest(BaseModel):
    """Body for PUT /api/users/{uuid} — partial update."""
    name: Optional[str] = Field(None, min_length=3, max_length=50)
    email: Optional[EmailStr] = Field(None, max_length=50)
    # password update is intentionally omitted (mirrors Java TODO)


# ─── Response body ───────────────────────────────────────────────────────────

class PersonResponse(BaseModel):
    """Serialized Person returned by the API (id is excluded).

    `validation_alias` lets Pydantic read the ORM's `full_name` attribute
    but serialize the field as `name` in the JSON response.
    """
    uuid: str
    name: str = Field(validation_alias="full_name")
    email: str

    model_config = {"from_attributes": True, "populate_by_name": True}
