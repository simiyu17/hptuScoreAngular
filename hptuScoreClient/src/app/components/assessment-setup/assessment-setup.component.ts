import { Component, ViewChild } from '@angular/core';
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { CreatePillarComponent } from './create-pillar/create-pillar.component';
import { EditPillarComponent } from './edit-pillar/edit-pillar.component';
import { ConfirmDialogComponent } from '../shared/confirm-dialog/confirm-dialog.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { AssessmentPillar } from '../../models/AssessmentPillar';
import { PillarsService } from '../../services/pillars.service';
import { GlobalService } from '../../services/global.service';
import { ConfirmDialogModel } from '../../models/ConfirmDialogModel';
import {MatTabsModule} from '@angular/material/tabs';

@Component({
  selector: 'app-assessment-setup',
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
    MatInputModule,
    MatTabsModule
  ],
  templateUrl: './assessment-setup.component.html',
  styleUrl: './assessment-setup.component.scss'
})
export class AssessmentSetupComponent {

  displayedColumns: string[] = ['pillarName', 'pillarCategoryCount', 'action'];
  dataSource!: MatTableDataSource<AssessmentPillar>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  pillars?: AssessmentPillar[];
  constructor(public dialog: MatDialog, private pillarService: PillarsService, private router: Router, private gs: GlobalService) { }

  openNewPillarDialog() {
    const dialogRef = this.dialog.open(CreatePillarComponent);

    dialogRef.afterClosed().subscribe({
      next: (res) => {
          this.getAvailablePillars();
      }
    });
  }

  openEditPillarDialog(pillar: AssessmentPillar) {
    const dialogRef = this.dialog.open(EditPillarComponent, {data: pillar});

    dialogRef.afterClosed().subscribe({
      next: (res) => {
          this.getAvailablePillars();
      }
    });
  }

  getAvailablePillars() {
    this.pillarService.getAllPillars()
      .subscribe({
        next: (response) => {
          this.pillars = response;
          this.dataSource = new MatTableDataSource(this.pillars);
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
    this.getAvailablePillars();
  }

  openPillarCategories(pillarId: any){
    this.router.navigateByUrl('/assessment-setup/pillars/'+pillarId+'/categories')
  }

  deletePillar(pillarId: number): void {
    const dialogData = new ConfirmDialogModel("Confirm", `Are you sure you want to delete this pillar?`);
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      maxWidth: "400px",
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(dialogResult => {
      if(dialogResult){
        this.pillarService.deletePillarById(pillarId)
        .subscribe({
          next: (response) => {
            this.gs.openSnackBar(response.message, "Dismiss");
            this.getAvailablePillars()
          },
          error: (error) => { }
        });
      }
    });
  }
}
