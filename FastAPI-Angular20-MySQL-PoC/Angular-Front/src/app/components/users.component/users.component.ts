import { CommonModule } from '@angular/common';
import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { UserDTO } from '../../domain/user';
import { UserAPIService } from '../../services/user-api.service';

@Component({
  selector: 'app-users.component',
  imports: [CommonModule],
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss',
})
export class UsersComponent implements OnChanges, OnInit {
  users: UserDTO[] = [];
  loading = true;

  constructor(private userAPIService: UserAPIService) {}
  ngOnInit(): void {
    this.reload();
  }
  ngOnChanges(changes: SimpleChanges): void {
    this.reload();
  }
  reload() {
    this.userAPIService.getAll().subscribe({
      next: (list: any) => {
        this.users = list;
      },
      error: (err: any) => console.error(err),
      complete: () => (this.loading = false),
    });
  }
}
