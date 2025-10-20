import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../../services/auth.service';
import { ServiceService } from '../../../services/service.service';
import { PrestataireService } from '../../../services/prestataire.service';
import { RendezVousService } from '../../../services/rendezvous.service';
import { User } from '../../../models/user.model';
import { Service } from '../../../models/service.model';
import { Prestataire } from '../../../models/prestataire.model';
import { RendezVous } from '../../../models/rendezvous.model';
import { Disponibilite } from '../../../models/disponibilite.model';
import { AddServiceDialogComponent } from '../add-service-dialog/add-service-dialog.component';
import { AddDisponibiliteDialogComponent } from '../add-disponibilite-dialog/add-disponibilite-dialog.component';

@Component({
  selector: 'app-prestataire-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class PrestataireDashboardComponent implements OnInit {
  currentUser: User | null = null;
  prestataire: Prestataire | null = null;
  services: Service[] = [];
  rendezVous: RendezVous[] = [];
  disponibilites: Disponibilite[] = [];
  loading = false;
  selectedTab = 0;

  // Statistics
  stats = {
    totalRendezVous: 0,
    enAttente: 0,
    confirmes: 0,
    termines: 0
  };

  constructor(
    private authService: AuthService,
    private serviceService: ServiceService,
    private prestataireService: PrestataireService,
    private rendezVousService: RendezVousService,
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.currentUserValue;
    this.loadPrestataireInfo();
  }

  loadPrestataireInfo(): void {
    this.loading = true;
    // In a real app, we would get the prestataire ID from the user's relationship
    // For now, we'll load all prestataires and find the one matching the user
    this.prestataireService.getAllPrestataires().subscribe({
      next: (prestataires) => {
        // Find the prestataire associated with this user (would be based on userId in real app)
        this.prestataire = prestataires[0]; // Simplified for demo
        if (this.prestataire?.id) {
          this.loadServices();
          this.loadRendezVous();
          this.loadDisponibilites();
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading prestataire info:', error);
        this.loading = false;
      }
    });
  }

  loadServices(): void {
    if (this.prestataire?.id) {
      this.serviceService.getServicesByPrestataireId(this.prestataire.id).subscribe({
        next: (services) => {
          this.services = services;
        },
        error: (error) => {
          console.error('Error loading services:', error);
        }
      });
    }
  }

  loadRendezVous(): void {
    if (this.prestataire?.id) {
      this.rendezVousService.getRendezVousByPrestataireId(this.prestataire.id).subscribe({
        next: (rendezVous) => {
          this.rendezVous = rendezVous;
          this.calculateStats();
        },
        error: (error) => {
          console.error('Error loading rendez-vous:', error);
        }
      });
    }
  }

  loadDisponibilites(): void {
    if (this.prestataire?.id) {
      this.prestataireService.getDisponibilitesByPrestataireId(this.prestataire.id).subscribe({
        next: (disponibilites) => {
          this.disponibilites = disponibilites;
        },
        error: (error) => {
          console.error('Error loading disponibilites:', error);
        }
      });
    }
  }

  calculateStats(): void {
    this.stats.totalRendezVous = this.rendezVous.length;
    this.stats.enAttente = this.rendezVous.filter(r => r.statut === 'EN_ATTENTE').length;
    this.stats.confirmes = this.rendezVous.filter(r => r.statut === 'CONFIRME').length;
    this.stats.termines = this.rendezVous.filter(r => r.statut === 'TERMINE').length;
  }

  confirmerRendezVous(rdv: RendezVous): void {
    if (rdv.id) {
      const updatedRdv = { ...rdv, statut: 'CONFIRME' as const };
      this.rendezVousService.updateRendezVous(rdv.id, updatedRdv).subscribe({
        next: () => {
          this.loadRendezVous();
        },
        error: (error) => {
          console.error('Error confirming rendez-vous:', error);
        }
      });
    }
  }

  annulerRendezVous(rdv: RendezVous): void {
    if (rdv.id) {
      const updatedRdv = { ...rdv, statut: 'ANNULE' as const };
      this.rendezVousService.updateRendezVous(rdv.id, updatedRdv).subscribe({
        next: () => {
          this.loadRendezVous();
        },
        error: (error) => {
          console.error('Error cancelling rendez-vous:', error);
        }
      });
    }
  }

  getStatutClass(statut: string): string {
    switch (statut) {
      case 'CONFIRME':
        return 'statut-confirme';
      case 'EN_ATTENTE':
        return 'statut-attente';
      case 'ANNULE':
        return 'statut-annule';
      case 'TERMINE':
        return 'statut-termine';
      default:
        return '';
    }
  }

  getJourLabel(jour: string): string {
    const jours: { [key: string]: string } = {
      'LUNDI': 'Lundi',
      'MARDI': 'Mardi',
      'MERCREDI': 'Mercredi',
      'JEUDI': 'Jeudi',
      'VENDREDI': 'Vendredi',
      'SAMEDI': 'Samedi',
      'DIMANCHE': 'Dimanche'
    };
    return jours[jour] || jour;
  }

  openAddServiceDialog(): void {
    if (!this.prestataire?.id) {
      this.snackBar.open('Erreur: Prestataire non trouvé', 'OK', { duration: 3000 });
      return;
    }

    const dialogRef = this.dialog.open(AddServiceDialogComponent, {
      width: '600px',
      data: { prestataireId: this.prestataire.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.snackBar.open('Service ajouté avec succès!', 'OK', { duration: 3000 });
        this.loadServices();
      }
    });
  }

  editService(service: Service): void {
    if (!this.prestataire?.id) {
      this.snackBar.open('Erreur: Prestataire non trouvé', 'OK', { duration: 3000 });
      return;
    }

    const dialogRef = this.dialog.open(AddServiceDialogComponent, {
      width: '600px',
      data: {
        prestataireId: this.prestataire.id,
        service: service
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.snackBar.open('Service modifié avec succès!', 'OK', { duration: 3000 });
        this.loadServices();
      }
    });
  }

  deleteService(service: Service): void {
    if (!service.id) {
      return;
    }

    if (confirm(`Êtes-vous sûr de vouloir supprimer le service "${service.nom}" ?`)) {
      this.serviceService.deleteService(service.id).subscribe({
        next: () => {
          this.snackBar.open('Service supprimé avec succès!', 'OK', { duration: 3000 });
          this.loadServices();
        },
        error: (error) => {
          console.error('Error deleting service:', error);
          this.snackBar.open('Erreur lors de la suppression', 'OK', { duration: 3000 });
        }
      });
    }
  }

  openAddDisponibiliteDialog(): void {
    if (!this.prestataire?.id) {
      this.snackBar.open('Erreur: Prestataire non trouvé', 'OK', { duration: 3000 });
      return;
    }

    const dialogRef = this.dialog.open(AddDisponibiliteDialogComponent, {
      width: '600px',
      data: { prestataireId: this.prestataire.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.snackBar.open('Disponibilité ajoutée avec succès!', 'OK', { duration: 3000 });
        this.loadDisponibilites();
      }
    });
  }

  editDisponibilite(dispo: Disponibilite): void {
    if (!this.prestataire?.id) {
      this.snackBar.open('Erreur: Prestataire non trouvé', 'OK', { duration: 3000 });
      return;
    }

    const dialogRef = this.dialog.open(AddDisponibiliteDialogComponent, {
      width: '600px',
      data: {
        prestataireId: this.prestataire.id,
        disponibilite: dispo
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.snackBar.open('Disponibilité modifiée avec succès!', 'OK', { duration: 3000 });
        this.loadDisponibilites();
      }
    });
  }

  deleteDisponibilite(dispo: Disponibilite): void {
    if (!dispo.id) {
      return;
    }

    if (confirm(`Êtes-vous sûr de vouloir supprimer la disponibilité du ${this.getJourLabel(dispo.jourSemaine)} ?`)) {
      this.prestataireService.deleteDisponibilite(dispo.id).subscribe({
        next: () => {
          this.snackBar.open('Disponibilité supprimée avec succès!', 'OK', { duration: 3000 });
          this.loadDisponibilites();
        },
        error: (error) => {
          console.error('Error deleting disponibilite:', error);
          this.snackBar.open('Erreur lors de la suppression', 'OK', { duration: 3000 });
        }
      });
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
