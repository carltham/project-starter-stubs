import { Request, Response, Router } from "express";

export class HomeController {
  private static BASE: string = "/";
  matchHttpToFunction = Router();

  constructor() {
    this.intializeApi();
  }

  intializeApi() {
    this.matchHttpToFunction.get(HomeController.BASE, this.greet);
  }

  greet(req: Request, res: Response) {
    return res.json({ message: "Welcome home!" });
  }
}

//export default new ApiEntryPoint().routeMatcher;
