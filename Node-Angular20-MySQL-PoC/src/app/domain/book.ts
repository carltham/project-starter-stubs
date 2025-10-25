//import { RowDataPacket } from "mysql2"

import { RowDataPacket } from "mysql2";

export default interface Book extends RowDataPacket {
  id?: number;
  title?: string;
  description?: string;
  published?: boolean;
}
