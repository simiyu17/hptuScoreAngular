import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { EditCategoryComponent } from './edit-category/edit-category.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { PillarCategory } from '../../../models/PillarCategory';
import { PillarsService } from '../../../services/pillars.service';
import { NewPillarCategory } from '../../../models/NewPillarCategory';

@Component({
  selector: 'app-pillar-categories',
  standalone: true,
  imports: [
    MatToolbarModule, 
    MatFormFieldModule, 
    MatInputModule,
    MatTableModule, 
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatPaginatorModule
  ],
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
    this.pillarService.getAllCategoriesByPillarId(this.pillarId)
      .subscribe({
        next: (response) => {
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
