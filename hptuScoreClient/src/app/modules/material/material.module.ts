import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import {MatPaginatorModule} from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import {MatTableModule} from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import {MatStepperModule} from '@angular/material/stepper';



@NgModule({
  declarations: [],
  imports: [
    CommonModule, 
    MatPaginatorModule, 
    MatSortModule, 
    MatTableModule, 
    MatToolbarModule, 
    MatFormFieldModule, 
    MatIconModule, 
    MatInputModule,
    MatDialogModule,
    MatButtonModule,
    MatTooltipModule,
    MatStepperModule
  ],
  exports: [
    CommonModule, 
    MatPaginatorModule, 
    MatSortModule, 
    MatTableModule, 
    MatToolbarModule, 
    MatFormFieldModule, 
    MatIconModule, 
    MatInputModule,
    MatDialogModule,
    MatButtonModule,
    MatTooltipModule,
    MatStepperModule
  ]
})
export class MaterialModule { }
