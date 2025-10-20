import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  currentUser: User | null = null;
  isAuthenticated = false;

  features = [
    {
      icon: 'event',
      title: 'Réservation facile',
      description: 'Prenez rendez-vous en quelques clics avec vos prestataires préférés'
    },
    {
      icon: 'schedule',
      title: 'Disponibilités en temps réel',
      description: 'Consultez les créneaux disponibles et choisissez celui qui vous convient'
    },
    {
      icon: 'notifications',
      title: 'Rappels automatiques',
      description: 'Recevez des notifications pour ne jamais manquer un rendez-vous'
    },
    {
      icon: 'verified',
      title: 'Prestataires vérifiés',
      description: 'Travaillez avec des professionnels qualifiés et vérifiés'
    }
  ];

  services = [
    {
      icon: 'content_cut',
      name: 'Coiffure',
      description: 'Coupe, coloration, coiffage',
      image: 'assets/images/coiffure.jpg'
    },
    {
      icon: 'spa',
      name: 'Esthétique',
      description: 'Soins du visage, manucure, pédicure',
      image: 'assets/images/esthetique.jpg'
    },
    {
      icon: 'fitness_center',
      name: 'Sport & Fitness',
      description: 'Coaching personnel, cours collectifs',
      image: 'assets/images/sport.jpg'
    },
    {
      icon: 'healing',
      name: 'Santé & Bien-être',
      description: 'Massage, physiothérapie, ostéopathie',
      image: 'assets/images/wellness.jpg'
    }
  ];

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isAuthenticated = this.authService.isAuthenticated();
    this.currentUser = this.authService.currentUserValue;
  }

  navigateToLogin(): void {
    this.router.navigate(['/login']);
  }

  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }

  navigateToDashboard(): void {
    if (this.currentUser) {
      switch (this.currentUser.role) {
        case 'ADMIN':
          this.router.navigate(['/admin/dashboard']);
          break;
        case 'PROVIDER':
          this.router.navigate(['/prestataire/dashboard']);
          break;
        case 'CLIENT':
          this.router.navigate(['/client/dashboard']);
          break;
        default:
          this.router.navigate(['/login']);
      }
    }
  }

  logout(): void {
    this.authService.logout();
  }
}
