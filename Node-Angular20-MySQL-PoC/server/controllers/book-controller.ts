import { Router } from "express";
import { BookService } from "../services/book-service";

export class BookController {
  matchHttpToFunction = Router();
  bookService = new BookService();

  constructor() {
    this.intializeRoutes();
  }

  intializeRoutes() {
    // Create a new Book
    this.matchHttpToFunction.post("/", this.bookService.create);

    // Retrieve all Books
    this.matchHttpToFunction.get("/", this.bookService.findAll);

    // Retrieve all published Books
    this.matchHttpToFunction.get(
      "/published",
      this.bookService.findAllPublished
    );

    // Retrieve a single Book with id
    this.matchHttpToFunction.get("/:id", this.bookService.findOne);

    // Update a Book with id
    this.matchHttpToFunction.put("/:id", this.bookService.update);

    // Delete a Book with id
    this.matchHttpToFunction.delete("/:id", this.bookService.delete);

    // Delete all Books
    this.matchHttpToFunction.delete("/", this.bookService.deleteAll);
  }
}
