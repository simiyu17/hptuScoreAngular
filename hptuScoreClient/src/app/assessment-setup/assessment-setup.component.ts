import { Component, ViewChild } from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import { AssessmentPillar } from '../models/AssessmentPillar';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { PillarsService } from '../services/pillars.service';
import { CreatePillarComponent } from './create-pillar/create-pillar.component';
import { SharedModule } from '../modules/shared/shared.module';
import { MaterialModule } from '../modules/material/material.module';
import { EditPillarComponent } from './edit-pillar/edit-pillar.component';
import { ConfirmDialogModel } from '../models/ConfirmDialogModel';
import { ConfirmDialogComponent } from '../shared/confirm-dialog/confirm-dialog.component';
import { GlobalService } from '../services/global.service';

@Component({
  selector: 'app-assessment-setup',
  standalone: true,
  imports: [ SharedModule, MaterialModule],
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
          console.log(response);
          this.pillars = response;
          this.dataSource = new MatTableDataSource(this.pillars);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        },
        error: (error) => { }
      });
  }

  ngAfterViewInit() {

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
