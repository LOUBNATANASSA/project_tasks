import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TaskService } from '../../services/task';

@Component({
    selector: 'app-create-task',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterModule],
    templateUrl: './create-task.component.html',
    styleUrls: ['./create-task.component.css']
})
export class CreateTaskComponent implements OnInit {
    projectId: number | null = null;
    task: any = {
        title: '',
        description: '',
        dueDate: '',
        projectId: null
    };
    isLoading = false;
    errorMessage = '';

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private taskService: TaskService
    ) { }

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.projectId = +id;
            this.task.projectId = this.projectId;
        } else {
            this.router.navigate(['/projects']);
        }
    }

    onSubmit(): void {
        if (!this.task.title) {
            this.errorMessage = 'Title is required';
            return;
        }

        this.isLoading = true;
        this.taskService.createTask(this.task).subscribe({
            next: () => {
                this.isLoading = false;
                this.router.navigate(['/projects', this.projectId, 'tasks']);
            },
            error: (err) => {
                console.error('Error creating task', err);
                this.isLoading = false;
                this.errorMessage = 'Failed to create task. Please try again.';
            }
        });
    }

    goBack(): void {
        this.router.navigate(['/projects', this.projectId, 'tasks']);
    }
}
