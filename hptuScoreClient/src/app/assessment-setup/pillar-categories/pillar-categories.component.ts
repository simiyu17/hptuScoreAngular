import { Component, ViewChild } from '@angular/core';
import { SharedModule } from '../../modules/shared/shared.module';
import { MaterialModule } from '../../modules/material/material.module';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { PillarsService } from '../../services/pillars.service';
import { ActivatedRoute } from '@angular/router';
import { PillarCategory } from '../../models/PillarCategory';
import { AddCategoryComponent } from './add-category/add-category.component';
import { EditCategoryComponent } from './edit-category/edit-category.component';
import { EditCategoryDto } from '../../dto/EditCategoryDto';
import { NewPillarCategory } from '../../models/NewPillarCategory';

@Component({
  selector: 'app-pillar-categories',
  standalone: true,
  imports: [SharedModule, MaterialModule],
  templateUrl: './pillar-categories.component.html',
  styleUrl: './pillar-categories.component.scss'
})
export class PillarCategoriesComponent {

  displayedColumns: string[] = ['category', 'choiceOne', 'choiceOneScore', 'choiceTwo', 'choiceTwoScore', 'choiceThree', 'choiceThreeScore', 'choiceFour', 'choiceFourScore', 'action'];
  dataSource!: MatTableDataSource<PillarCategory>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  pillarId?: any;
  categories?: PillarCategory[];
  constructor(public dialog: MatDialog, private pillarService: PillarsService, private router: ActivatedRoute) { }

  openEditPillarCategoryDialog(category: PillarCategory | NewPillarCategory) {
    const dialogRef = this.dialog.open(EditCategoryComponent, {data: {category: category, pillarId: this.pillarId}});

    dialogRef.afterClosed().subscribe({
      next: (res) => {
          this.getAvailablePillarCategories();
      }
    });
  }

  getAvailablePillarCategories() {
    console.log(this.pillarId)
    this.pillarService.getAllCategoriesByPillarId(this.pillarId)
      .subscribe({
        next: (response) => {
          console.log(response);
          this.categories = response;
          this.dataSource = new MatTableDataSource(this.categories);
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
    this.pillarId = this.router.snapshot.paramMap.get('id');
    this.getAvailablePillarCategories();
  }
}
