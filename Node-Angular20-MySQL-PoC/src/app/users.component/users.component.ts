import { CommonModule } from "@angular/common";
import { Component, OnChanges, SimpleChanges } from "@angular/core";

import { UserDTO } from "../domain/user";
import { UserAPIService } from "../services/user-api.service";
@Component({
  selector: "app-users.component",
  imports: [CommonModule],
  templateUrl: "./users.component.html",
  styleUrl: "./users.component.scss",
})
export class UsersComponent implements OnChanges {
  users: UserDTO[] = [];
  loading = true;

  constructor(private userAPIService: UserAPIService) {}
  ngOnChanges(changes: SimpleChanges): void {
    this.userAPIService.getAll().subscribe({
      next: (list: any) => {
        this.users = list;
        console.log("UsersComponent: users = ", this.users);
      },
      error: (err: any) => console.error(err),
      complete: () => (this.loading = false),
    });
  }
}
