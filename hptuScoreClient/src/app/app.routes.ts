import { Routes } from '@angular/router';
import { UsersComponent } from './components/users/users.component';
import { AuthGuard } from './util/AuthGuard';
import { LoginComponent } from './components/login/login.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';

export const routes: Routes = [
    {
        path : 'dashboard',
        canActivateChild: [AuthGuard],
        children : [
            {
                path: '',
                loadComponent: () => import('./components/dashboard/dashboard.component').then(c => c.DashboardComponent)
            },
            {
                path: 'pillar-summary',
                loadComponent: () => import('./components/dashboard/pillar-detailed/pillar-detailed.component').then(c => c.PillarDetailedComponent)
            }
        ]
    },
    {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
    {
        path : 'assessment-setup',
        canActivateChild: [AuthGuard],
        children : [
            {
                path: 'pillars',
                loadComponent: () => import('./components/assessment-setup/assessment-setup.component').then(c => c.AssessmentSetupComponent)
            },
            {
                path: 'pillars/:id/categories',
                loadComponent: () => import('./components/assessment-setup/pillar-categories/pillar-categories.component').then(c => c.PillarCategoriesComponent)
            },
            {
                path: 'functionalities',
                loadComponent: () => import('./components/assessment-setup/question-summary-list/question-summary-list.component').then(c => c.QuestionSummaryListComponent)
            },
            {
                path: 'functionalities/:id/add-summary-question',
                loadComponent: () => import('./components/assessment-setup/add-question-summary/add-question-summary.component').then(c => c.AddQuestionSummaryComponent)
            }
        ]
    },
    {
        path : 'county-assessments',
        canActivateChild: [AuthGuard],
        children : [
            {
                path: '',
                loadComponent: () => import('./components/county-assessments/county-assessments.component').then(c => c.CountyAssessmentsComponent)
            },
            {
                path: 'new',
                loadComponent: () => import('./components/county-assessments/create-assessment/create-assessment.component').then(c => c.CreateAssessmentComponent)
            },
            {
                path: 'new-hptu',
                loadComponent: () => import('./components/hptu-assessment/assessment/assessment.component').then(c => c.AssessmentComponent)
            }
        ]
    }, 
    {path: 'users', component: UsersComponent, canActivate: [AuthGuard]},
    {path: 'login', component: LoginComponent},
    {path: 'change-password', component: ChangePasswordComponent, canActivate: [AuthGuard]},
];
