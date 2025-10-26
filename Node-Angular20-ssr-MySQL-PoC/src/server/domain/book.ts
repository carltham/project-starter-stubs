//import { RowDataPacket } from "mysql2"

import { RowDataPacket } from 'mysql2';

export default interface BookEntity extends RowDataPacket {
  id?: number;
  title?: string;
  description?: string;
  published?: string;
}
