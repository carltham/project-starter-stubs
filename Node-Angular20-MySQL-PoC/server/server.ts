import cors, { CorsOptions } from "cors";
import express from "express";
import { dirname } from "node:path";
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
 * Serve static files from /browser
 */
const appFolder = `${__dirname}/../ui/browser`; // "build/ui/browser/index.html";
console.log("dirname(appFolder) = ", dirname(appFolder));
console.log("appFolder = ", appFolder);
//application.use(staticFiles);
application.use(express.static(appFolder));

//application.use("/", staticFiles);
application.use("/home", new HomeController().matchHttpToFunction);
application.use("/api", new RootController().matchHttpToFunction);
application.use("/api/books", new BookController().matchHttpToFunction);

// --- Example REST API ---
application.use("/api/users", new UserController().matchHttpToFunction);
