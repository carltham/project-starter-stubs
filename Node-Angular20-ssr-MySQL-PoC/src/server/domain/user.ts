//import { RowDataPacket } from "mysql2"

import { RowDataPacket } from 'mysql2';

export interface UserEntity extends RowDataPacket {
  id?: number;
  full_name: string;
  email: string;
}
