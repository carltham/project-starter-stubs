import cors, { CorsOptions } from "cors";
import express from "express";
import { BookController } from "./controllers/book-controller";
import { HomeController } from "./controllers/home-controller";
import { RootController } from "./controllers/root-controller";
import { UserController } from "./controllers/user-controller";

const PORT: number = process.env.PORT ? parseInt(process.env.PORT, 10) : 8080;

const application = express();
application
  .listen(PORT, "localhost", function () {
    console.log(`Server is running on port ${PORT}.`);
  })
  .on("error", (err: any) => {
    if (err.code === "EADDRINUSE") {
      console.log("Error: address already in use");
    } else {
      console.log(err);
    }
  });

const corsOptions: CorsOptions = {
  origin: "http://localhost:8081",
};
application.use(cors(corsOptions));
application.use(express.json());
application.use(express.urlencoded({ extended: true }));

/**
 * Serve angular and static files
 */
const appFolder = `${__dirname}/../ui/browser`;
application.use(express.static(appFolder));
application.use("/home", new HomeController().matchHttpToFunction);

/**
 * --- The rest is the REST API ---
 */
application.use("/api", new RootController().matchHttpToFunction);
application.use("/api/books", new BookController().matchHttpToFunction);

application.use("/api/users", new UserController().matchHttpToFunction);
