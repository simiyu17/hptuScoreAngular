import { Component, OnInit, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { User } from '../../models/User';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { GlobalService } from '../../services/global.service';
import { EditUserComponent } from './edit-user/edit-user.component';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatFormFieldModule,
    MatTableModule,
    MatTooltipModule,
    MatIconModule,
    MatPaginatorModule,
    MatButtonModule, 
    MatSortModule, 
    MatInputModule
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss'
})
export class UsersComponent implements OnInit{

  displayedColumns: string[] = ['firstName', 'lastName', 'username', 'designation', 'cellPhone', 'action'];
  dataSource!: MatTableDataSource<User>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  users?: User[];
  constructor(public dialog: MatDialog, private userService: UserService, private router: Router, private gs: GlobalService) { }

  openEditUserDialog(user?: User) {
    const dialogRef = this.dialog.open(EditUserComponent, {data: user});
    dialogRef.afterClosed().subscribe({
      next: (res) => {
          this.getAvailableUsers();
      }
    });
  }

  getAvailableUsers() {
    this.userService.getAvailableUsers()
      .subscribe({
        next: (response) => {
          this.users = response;
          this.dataSource = new MatTableDataSource(this.users);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        },
        error: (error) => { }
      });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  ngOnInit(): void {
    this.getAvailableUsers();
  }
}
