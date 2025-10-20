import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from '../../../services/auth.service';
import { ServiceService } from '../../../services/service.service';
import { PrestataireService } from '../../../services/prestataire.service';
import { RendezVousService } from '../../../services/rendezvous.service';
import { User } from '../../../models/user.model';
import { Service } from '../../../models/service.model';
import { Prestataire } from '../../../models/prestataire.model';
import { RendezVous } from '../../../models/rendezvous.model';
import { Router } from '@angular/router';
import { ReservationDialogComponent } from '../reservation-dialog/reservation-dialog.component';
import { DisponibilitesDialogComponent } from '../disponibilites-dialog/disponibilites-dialog.component';

@Component({
  selector: 'app-client-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class ClientDashboardComponent implements OnInit {
  currentUser: User | null = null;
  services: Service[] = [];
  prestataires: Prestataire[] = [];
  rendezVous: RendezVous[] = [];
  loading = false;
  selectedTab = 0;

  displayedColumns: string[] = ['service', 'prestataire', 'date', 'heure', 'statut'];

  constructor(
    private authService: AuthService,
    private serviceService: ServiceService,
    private prestataireService: PrestataireService,
    private rendezVousService: RendezVousService,
    private router: Router,
    public dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.currentUserValue;
    console.log('Current user in dashboard:', this.currentUser);
    console.log('Current user ID:', this.currentUser?.id);
    this.loadServices();
    this.loadPrestataires();
    this.loadRendezVous();
  }

  loadServices(): void {
    this.loading = true;
    this.serviceService.getAllServices().subscribe({
      next: (services) => {
        this.services = services;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading services:', error);
        this.loading = false;
      }
    });
  }

  loadPrestataires(): void {
    this.prestataireService.getAllPrestataires().subscribe({
      next: (prestataires) => {
        this.prestataires = prestataires;
      },
      error: (error) => {
        console.error('Error loading prestataires:', error);
      }
    });
  }

  loadRendezVous(): void {
    if (this.currentUser?.id) {
      this.rendezVousService.getRendezVousByClientId(this.currentUser.id).subscribe({
        next: (rendezVous) => {
          this.rendezVous = rendezVous;
        },
        error: (error) => {
          console.error('Error loading rendez-vous:', error);
        }
      });
    }
  }

  getPrestataireById(id: number): Prestataire | undefined {
    return this.prestataires.find(p => p.id === id);
  }

  getServicesByPrestataireId(prestataireId: number): Service[] {
    return this.services.filter(s => s.prestataireId === prestataireId);
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

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  openReservationDialog(service: Service): void {
    if (!this.currentUser?.id) {
      console.error('User ID not found');
      return;
    }

    const dialogRef = this.dialog.open(ReservationDialogComponent, {
      width: '600px',
      data: {
        service: service,
        clientId: this.currentUser.id
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadRendezVous();
      }
    });
  }

  openDisponibilitesDialog(prestataire: Prestataire): void {
    const prestataireServices = this.getServicesByPrestataireId(prestataire.id!);

    this.dialog.open(DisponibilitesDialogComponent, {
      width: '700px',
      data: {
        prestataire: prestataire,
        services: prestataireServices
      }
    });
  }
}
