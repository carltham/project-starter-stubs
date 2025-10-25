import { ResultSetHeader } from "mysql2";
import { UserDTO, UserEntity } from "../../domain/user";
import { MysqlHandler } from "../mysql-handler";

export interface UserDAO {
  save(user: UserDTO): Promise<UserDTO>;
  retrieveAll(searchParams: {
    title?: string;
    published?: boolean;
  }): Promise<UserDTO[]>;
  retrieveById(userId: number): Promise<UserDTO | undefined>;
  update(user: UserDTO): Promise<number>;
  delete(userId: number): Promise<number>;
  deleteAll(): Promise<number>;
}

export class UserDAOImpl implements UserDAO {
  connection = new MysqlHandler().connection;
  save(user: UserDTO): Promise<UserDTO> {
    return new Promise((resolve, reject) => {
      this.connection.query<ResultSetHeader>(
        "INSERT INTO users (name, email) VALUES(?,?)",
        [user.name, user.email],
        (err: any, res: any) => {
          if (err) reject(err);
          else
            this.retrieveById(res.insertId)
              .then((user) => resolve(user!))
              .catch(reject);
        }
      );
    });
  }

  retrieveAll(searchParams: {
    title?: string;
    published?: boolean;
  }): Promise<UserDTO[]> {
    let query: string = "SELECT * FROM users";

    let condition: string = "";

    if (searchParams?.published) condition += "published = TRUE";

    if (searchParams?.title)
      condition += `LOWER(title) LIKE '%${searchParams.title}%'`;

    if (condition.length) query += " WHERE " + condition;
    console.log("QUERY: ", query);

    return new Promise<UserDTO[]>((resolve, reject) => {
      let users: UserDTO[];
      this.connection.query<UserEntity[]>(
        query,
        (err: any, res: UserEntity[]) => {
          users = this.convertAndAddTo(res);
          if (err) reject(err);
          else resolve(users);
        }
      );
    });
  }

  private convertAndAddTo(entities: UserEntity[]) {
    const users: UserDTO[] = [];
    entities.forEach((dbUser) => {
      const user: UserDTO = {
        id: dbUser.id,
        name: dbUser.full_name,
        email: dbUser.email,
      };
      users.push(user);
    });
    return users;
  }

  retrieveById(userId: number): Promise<UserDTO> {
    return new Promise((resolve, reject) => {
      let users: UserDTO[];

      this.connection.query<UserEntity[]>(
        "SELECT * FROM users WHERE id = ?",
        [userId],
        (err: any, res: any) => {
          users = this.convertAndAddTo(res);
          if (err) reject(err);
          else resolve(users?.[0]);
        }
      );
    });
  }

  update(user: UserDTO): Promise<number> {
    return new Promise((resolve, reject) => {
      this.connection.query<ResultSetHeader>(
        "UPDATE users SET full_name = ?, email = ? WHERE id = ?",
        [user.name, user.email, user.id],
        (err: any, res: any) => {
          if (err) reject(err);
          else resolve(res.affectedRows);
        }
      );
    });
  }

  delete(userId: number): Promise<number> {
    return new Promise((resolve, reject) => {
      this.connection.query<ResultSetHeader>(
        "DELETE FROM users WHERE id = ?",
        [userId],
        (err: any, res: any) => {
          if (err) reject(err);
          else resolve(res.affectedRows);
        }
      );
    });
  }

  deleteAll(): Promise<number> {
    return new Promise((resolve, reject) => {
      this.connection.query<ResultSetHeader>(
        "DELETE FROM users",
        (err: any, res: any) => {
          if (err) reject(err);
          else resolve(res.affectedRows);
        }
      );
    });
  }
}
