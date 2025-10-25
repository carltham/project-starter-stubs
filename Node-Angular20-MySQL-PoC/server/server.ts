import cors, { CorsOptions } from "cors";
import express from "express";
import { APIBookController } from "./controllers/api-book-controller";
import { APIHomeController } from "./controllers/api-home-controller";
import { APIRootController } from "./controllers/api-root-controller";
import { APIUserController } from "./controllers/api-user-controller";

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

/**
 * --- The rest is the REST API ---
 */
application.use("/home", new APIHomeController().matchHttpToFunction);
application.use("/api", new APIRootController().matchHttpToFunction);
application.use("/api/books", new APIBookController().matchHttpToFunction);

application.use("/api/users", new APIUserController().matchHttpToFunction);
