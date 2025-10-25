import { Request, Response } from "express";
import { console } from "node:inspector";
import { UserDAO, UserDAOImpl } from "../db/daos/user-dao";
import { UserDTO } from "../domain/user";

export class UserService {
  userDAO: UserDAO = new UserDAOImpl();

  create = async (req: Request, res: Response) => {
    if (!req.body.title) {
      res.status(400).send({
        message: "Content can not be empty!",
      });
      return;
    }

    try {
      const user: UserDTO = req.body;
      const savedUser = await this.userDAO.save(user);

      res.status(201).send(savedUser);
    } catch (err) {
      res.status(500).send({
        message: "Some error occurred while retrieving users." + err,
      });
    }
  };

  findAll = async (req: Request, res: Response) => {
    const title = typeof req.query.title === "string" ? req.query.title : "";
    const reqQuery = req.query;
    console.log("REQ QUERY: ", reqQuery);

    try {
      const users = await this.userDAO.retrieveAll({ title: title });

      res.status(200).send(users);
    } catch (err) {
      res.status(500).send({
        message: "Some error occurred while retrieving users." + err,
      });
    }
  };

  findOne = async (req: Request, res: Response) => {
    const id: number = parseInt(req.params.id);

    try {
      const user = await this.userDAO.retrieveById(id);

      if (user) res.status(200).send(user);
      else
        res.status(404).send({
          message: `Cannot find User with id=${id}.`,
        });
    } catch (err) {
      res.status(500).send({
        message: `Error retrieving User with id=${id}.`,
      });
    }
  };

  update = async (req: Request, res: Response) => {
    let user: UserDTO = req.body;
    user.id = parseInt(req.params.id);

    try {
      const num = await this.userDAO.update(user);

      if (num == 1) {
        res.send({
          message: "User was updated successfully.",
        });
      } else {
        res.send({
          message: `Cannot update User with id=${user.id}. Maybe User was not found or req.body is empty!`,
        });
      }
    } catch (err) {
      res.status(500).send({
        message: `Error updating User with id=${user.id}.`,
      });
    }
  };

  delete = async (req: Request, res: Response) => {
    const id: number = parseInt(req.params.id);

    try {
      const num = await this.userDAO.delete(id);

      if (num == 1) {
        res.send({
          message: "User was deleted successfully!",
        });
      } else {
        res.send({
          message: `Cannot delete User with id=${id}. Maybe User was not found!`,
        });
      }
    } catch (err) {
      res.status(500).send({
        message: `Could not delete User with id==${id}.`,
      });
    }
  };

  deleteAll = async (req: Request, res: Response) => {
    try {
      const num = await this.userDAO.deleteAll();

      res.send({ message: `${num} Users were deleted successfully!` });
    } catch (err) {
      res.status(500).send({
        message: "Some error occurred while removing all users.",
      });
    }
  };

  findAllPublished = async (req: Request, res: Response) => {
    try {
      const users = await this.userDAO.retrieveAll({
        published: true,
      });

      res.status(200).send(users);
    } catch (err: any) {
      res.status(500).send({
        message: ["Some error occurred while retrieving users.", err.message],
      });
    }
  };
}
