import { Request, Response, Router } from "express";

export class APIRootController {
  private static BASE: string = "/";
  matchHttpToFunction = Router();

  constructor() {
    this.intializeApi();
  }

  intializeApi() {
    this.matchHttpToFunction.get(APIRootController.BASE, this.greet);
  }

  greet(req: Request, res: Response) {
    return res.json({ message: "Welcome to bezkoder application." });
  }
}

//export default new ApiEntryPoint().routeMatcher;
