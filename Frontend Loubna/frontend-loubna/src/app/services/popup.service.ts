import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

export interface PopupConfig {
    title: string;
    message: string;
    type: 'confirm' | 'alert' | 'success' | 'error';
    confirmText?: string;
    cancelText?: string;
}

export interface PopupResult {
    confirmed: boolean;
}

@Injectable({
    providedIn: 'root'
})
export class PopupService {
    private popupSubject = new Subject<PopupConfig | null>();
    private responseSubject = new Subject<PopupResult>();

    popup$ = this.popupSubject.asObservable();

    open(config: PopupConfig): Observable<PopupResult> {
        this.popupSubject.next(config);
        return new Observable(observer => {
            const subscription = this.responseSubject.subscribe(result => {
                observer.next(result);
                observer.complete();
                subscription.unsubscribe();
            });
        });
    }

    close(result: PopupResult): void {
        this.responseSubject.next(result);
        this.popupSubject.next(null);
    }

    // Méthodes utilitaires
    confirm(title: string, message: string): Observable<PopupResult> {
        return this.open({
            title,
            message,
            type: 'confirm',
            confirmText: 'Confirmer',
            cancelText: 'Annuler'
        });
    }

    alert(title: string, message: string): Observable<PopupResult> {
        return this.open({
            title,
            message,
            type: 'alert',
            confirmText: 'OK'
        });
    }

    success(title: string, message: string): Observable<PopupResult> {
        return this.open({
            title,
            message,
            type: 'success',
            confirmText: 'OK'
        });
    }

    error(title: string, message: string): Observable<PopupResult> {
        return this.open({
            title,
            message,
            type: 'error',
            confirmText: 'OK'
        });
    }
}
