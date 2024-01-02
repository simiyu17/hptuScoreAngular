import { Routes } from '@angular/router';
import { UsersComponent } from './components/users/users.component';

export const routes: Routes = [
    {
        path : 'dashboard',
        children : [
            {
                path: '',
                loadComponent: () => import('./components/dashboard/dashboard.component').then(c => c.DashboardComponent)
            },
            {
                path: ':meta-id/:pillar-name',
                loadComponent: () => import('./components/dashboard/pillar-detailed/pillar-detailed.component').then(c => c.PillarDetailedComponent)
            }
        ]
    },
    {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
    {
        path : 'assessment-setup',
        children : [
            {
                path: 'pillars',
                loadComponent: () => import('./components/assessment-setup/assessment-setup.component').then(c => c.AssessmentSetupComponent)
            },
            {
                path: 'pillars/:id/categories',
                loadComponent: () => import('./components/assessment-setup/pillar-categories/pillar-categories.component').then(c => c.PillarCategoriesComponent)
            }
        ]
    },
    {
        path : 'county-assessments',
        children : [
            {
                path: '',
                loadComponent: () => import('./components/county-assessments/county-assessments.component').then(c => c.CountyAssessmentsComponent)
            },
            {
                path: 'new',
                loadComponent: () => import('./components/county-assessments/create-assessment/create-assessment.component').then(c => c.CreateAssessmentComponent)
            }
        ]
    }, 
    {path: 'users', component: UsersComponent},
];
