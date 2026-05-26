"""
Domain exceptions — Python equivalents of the Java exception classes.
"""


class BookNotFoundException(Exception):
    def __init__(self, message: str = "Book not found"):
        self.message = message
        super().__init__(message)


class BookAlreadyExistException(Exception):
    def __init__(self, message: str = "Book already exists"):
        self.message = message
        super().__init__(message)


class PersonNotFoundException(Exception):
    def __init__(self, message: str = "Person not found"):
        self.message = message
        super().__init__(message)


class PersonAlreadyExistException(Exception):
    def __init__(self, message: str = "Person already exists"):
        self.message = message
        super().__init__(message)
