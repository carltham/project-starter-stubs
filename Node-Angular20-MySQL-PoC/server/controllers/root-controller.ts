import { Request, Response, Router } from "express";

export class RootController {
  private static BASE: string = "/";
  matchHttpToFunction = Router();

  constructor() {
    this.intializeApi();
  }

  intializeApi() {
    this.matchHttpToFunction.get(RootController.BASE, this.greet);
  }

  greet(req: Request, res: Response) {
    return res.json({ message: "Welcome to bezkoder application." });
  }
}

//export default new ApiEntryPoint().routeMatcher;
