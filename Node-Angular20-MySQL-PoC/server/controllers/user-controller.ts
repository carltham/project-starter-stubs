import { Router } from "express";
import { UserService } from "../services/user-service";

export class UserController {
  matchHttpToFunction = Router();
  userService = new UserService();

  constructor() {
    this.intializeRoutes();
  }

  intializeRoutes() {
    // Create a new User
    this.matchHttpToFunction.post("/", this.userService.create);

    // Retrieve all Users
    this.matchHttpToFunction.get("/", this.userService.findAll);

    // Retrieve all published Users
    this.matchHttpToFunction.get(
      "/published",
      this.userService.findAllPublished
    );

    // Retrieve a single User with id
    this.matchHttpToFunction.get("/:id", this.userService.findOne);

    // Update a User with id
    this.matchHttpToFunction.put("/:id", this.userService.update);

    // Delete a User with id
    this.matchHttpToFunction.delete("/:id", this.userService.delete);

    // Delete all Users
    this.matchHttpToFunction.delete("/", this.userService.deleteAll);
  }
}
