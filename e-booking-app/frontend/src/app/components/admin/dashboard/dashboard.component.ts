import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { AdminService } from '../../../services/admin.service';
import { User } from '../../../models/user.model';
import { Statistiques, StatistiquePrestataire } from '../../../models/rendezvous.model';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  currentUser: User | null = null;
  users: User[] = [];
  statistiques: Statistiques = {
    nombreTotalRendezVous: 0,
    nombreRendezVousEnAttente: 0,
    nombreRendezVousConfirmes: 0,
    nombreRendezVousAnnules: 0,
    nombreTotalClients: 0,
    nombreTotalPrestataires: 0,
    nombreTotalServices: 0
  };
  statistiquesPrestataires: StatistiquePrestataire[] = [];
  loading = false;
  selectedTab = 0;

  displayedColumns: string[] = ['id', 'prenom', 'nom', 'email', 'role', 'statut', 'actions'];
  displayedStatsColumns: string[] = ['prenom', 'nom', 'email', 'service', 'totalRdv', 'confirmes', 'enAttente', 'annules', 'clients'];

  constructor(
    private authService: AuthService,
    private adminService: AdminService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.currentUserValue;
    this.loadStatistiques();
    this.loadUsers();
    this.loadStatistiquesPrestataires();
  }

  loadStatistiques(): void {
    this.loading = true;
    this.adminService.getStatistiques().subscribe({
      next: (stats) => {
        this.statistiques = stats;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading statistiques:', error);
        this.loading = false;
      }
    });
  }

  loadUsers(): void {
    this.adminService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users;
      },
      error: (error) => {
        console.error('Error loading users:', error);
      }
    });
  }

  loadStatistiquesPrestataires(): void {
    this.adminService.getStatistiquesPrestataires().subscribe({
      next: (stats) => {
        this.statistiquesPrestataires = stats;
      },
      error: (error) => {
        console.error('Error loading statistiques prestataires:', error);
      }
    });
  }

  activerCompte(userId: number): void {
    this.adminService.activerCompte(userId).subscribe({
      next: () => {
        this.loadUsers();
      },
      error: (error) => {
        console.error('Error activating user:', error);
      }
    });
  }

  bloquerCompte(userId: number): void {
    this.adminService.bloquerCompte(userId).subscribe({
      next: () => {
        this.loadUsers();
      },
      error: (error) => {
        console.error('Error blocking user:', error);
      }
    });
  }

  getRoleClass(role: string): string {
    switch (role) {
      case 'ADMIN':
        return 'role-admin';
      case 'PRO':
        return 'role-pro';
      case 'CLIENT':
        return 'role-client';
      default:
        return '';
    }
  }

  getStatutClass(statut: string): string {
    return statut === 'ACTIF' ? 'statut-actif' : 'statut-bloque';
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
