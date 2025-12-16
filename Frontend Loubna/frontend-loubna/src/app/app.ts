import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
// CORRECTION : Le fichier s'appelle 'navbar.ts', donc on importe depuis './components/navbar/navbar'
import { NavbarComponent } from './components/navbar/navbar';

@Component({
  selector: 'app-root',
  standalone: true,
  // C'est ici qu'on dit à l'application : "J'utilise la NavbarComponent"
  imports: [CommonModule, RouterOutlet, NavbarComponent],
  // CORRECTION : Tes fichiers s'appellent app.html et app.css (voir image 2)
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent {
  title = 'frontend-loubna';
}