import { Connection, createConnection } from "mysql2";
import { DbConfig } from "./mysql-config";
export class MysqlHandler {
  config: DbConfig = new DbConfig();
  connection: Connection = createConnection({
    host: this.config.HOST,
    user: this.config.USER,
    password: this.config.PASSWORD,
    database: this.config.DB,
  });
}
