import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PopupService, PopupConfig } from '../../services/popup.service';
import { Subscription } from 'rxjs';

@Component({
    selector: 'app-popup',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './popup.component.html',
    styleUrls: ['./popup.component.css']
})
export class PopupComponent implements OnInit, OnDestroy {
    config: PopupConfig | null = null;
    private subscription: Subscription | null = null;

    constructor(private popupService: PopupService) { }

    ngOnInit(): void {
        this.subscription = this.popupService.popup$.subscribe(config => {
            this.config = config;
        });
    }

    ngOnDestroy(): void {
        this.subscription?.unsubscribe();
    }

    confirm(): void {
        this.popupService.close({ confirmed: true });
    }

    cancel(): void {
        this.popupService.close({ confirmed: false });
    }

    getIconClass(): string {
        switch (this.config?.type) {
            case 'confirm': return 'icon-confirm';
            case 'success': return 'icon-success';
            case 'error': return 'icon-error';
            default: return 'icon-alert';
        }
    }

    getIcon(): string {
        switch (this.config?.type) {
            case 'confirm': return '❓';
            case 'success': return '✓';
            case 'error': return '✕';
            default: return 'ℹ';
        }
    }
}
