import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PrestataireService } from '../../../services/prestataire.service';
import { RendezVousService } from '../../../services/rendezvous.service';
import { Prestataire } from '../../../models/prestataire.model';
import { Service } from '../../../models/service.model';
import { Disponibilite } from '../../../models/disponibilite.model';

@Component({
  selector: 'app-reservation-dialog',
  templateUrl: './reservation-dialog.component.html',
  styleUrls: ['./reservation-dialog.component.scss']
})
export class ReservationDialogComponent implements OnInit {
  reservationForm!: FormGroup;
  prestataires: Prestataire[] = [];
  disponibilites: Disponibilite[] = [];
  loading = false;
  minDate: Date = new Date();
  creneauInvalide = false;
  messageCreneauInvalide = '';

  constructor(
    private fb: FormBuilder,
    private prestataireService: PrestataireService,
    private rendezVousService: RendezVousService,
    private dialogRef: MatDialogRef<ReservationDialogComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: { service: Service; clientId: number }
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadPrestataires();
  }

  initForm(): void {
    this.reservationForm = this.fb.group({
      prestataireId: ['', Validators.required],
      date: ['', Validators.required],
      heure: ['', Validators.required]
    });

    // Watch for prestataire change to load disponibilites
    this.reservationForm.get('prestataireId')?.valueChanges.subscribe(prestataireId => {
      if (prestataireId) {
        this.loadDisponibilites(prestataireId);
        this.validateCreneau();
      } else {
        this.disponibilites = [];
        this.creneauInvalide = false;
        this.messageCreneauInvalide = '';
      }
    });

    // Watch for date change to validate creneau
    this.reservationForm.get('date')?.valueChanges.subscribe(() => {
      this.validateCreneau();
    });

    // Watch for heure change to validate creneau
    this.reservationForm.get('heure')?.valueChanges.subscribe(() => {
      this.validateCreneau();
    });
  }

  loadPrestataires(): void {
    // Load prestataires who offer this service
    this.prestataireService.getAllPrestataires().subscribe({
      next: (prestataires) => {
        // Filter prestataires who have this service
        this.prestataires = prestataires.filter(p => p.id === this.data.service.prestataireId);
      },
      error: (error) => {
        console.error('Error loading prestataires:', error);
        this.snackBar.open('Erreur lors du chargement des prestataires', 'OK', { duration: 3000 });
      }
    });
  }

  loadDisponibilites(prestataireId: number): void {
    this.prestataireService.getDisponibilitesByPrestataireId(prestataireId).subscribe({
      next: (disponibilites: Disponibilite[]) => {
        this.disponibilites = disponibilites;
        this.validateCreneau();
      },
      error: (error: any) => {
        console.error('Error loading disponibilites:', error);
        this.snackBar.open('Erreur lors du chargement des disponibilités', 'OK', { duration: 3000 });
      }
    });
  }

  validateCreneau(): void {
    const dateValue = this.reservationForm.get('date')?.value;
    const heureValue = this.reservationForm.get('heure')?.value;
    const prestataireId = this.reservationForm.get('prestataireId')?.value;

    // Réinitialiser l'état si les champs ne sont pas remplis
    if (!dateValue || !heureValue || !prestataireId || this.disponibilites.length === 0) {
      this.creneauInvalide = false;
      this.messageCreneauInvalide = '';
      return;
    }

    // Convertir la date en jour de la semaine
    const date = new Date(dateValue);
    const joursSemaine = ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'];
    const jourSemaine = joursSemaine[date.getDay()];

    // Trouver les disponibilités pour ce jour
    const disposDuJour = this.disponibilites.filter(d =>
      d.jourSemaine.toLowerCase() === jourSemaine.toLowerCase()
    );

    if (disposDuJour.length === 0) {
      this.creneauInvalide = true;
      this.messageCreneauInvalide = `Le prestataire n'est pas disponible le ${jourSemaine}`;
      return;
    }

    // Vérifier si l'heure est dans l'une des plages de disponibilité
    const heureChoisie = heureValue; // Format "HH:mm"

    const creneauValide = disposDuJour.some(dispo => {
      return heureChoisie >= dispo.heureDebut && heureChoisie < dispo.heureFin;
    });

    if (!creneauValide) {
      this.creneauInvalide = true;
      const plages = disposDuJour.map(d => `${d.heureDebut} - ${d.heureFin}`).join(', ');
      this.messageCreneauInvalide = `Ce créneau n'est pas disponible. Disponibilités le ${jourSemaine}: ${plages}`;
    } else {
      this.creneauInvalide = false;
      this.messageCreneauInvalide = '';
    }
  }

  onSubmit(): void {
    if (this.creneauInvalide) {
      this.snackBar.open(this.messageCreneauInvalide, 'OK', {
        duration: 5000,
        panelClass: ['error-snackbar']
      });
      return;
    }

    if (this.reservationForm.valid) {
      this.loading = true;
      const formValue = this.reservationForm.value;

      const rendezVousData = {
        clientId: this.data.clientId,
        prestataireId: formValue.prestataireId,
        serviceId: this.data.service.id,
        date: formValue.date,
        heure: formValue.heure,
        statut: 'EN_ATTENTE' as const
      };

      this.rendezVousService.createRendezVous(rendezVousData).subscribe({
        next: () => {
          this.loading = false;
          this.snackBar.open('Rendez-vous créé avec succès!', 'OK', {
            duration: 5000,
            panelClass: ['success-snackbar']
          });
          this.dialogRef.close(true);
        },
        error: (error) => {
          this.loading = false;
          console.error('Error creating rendez-vous:', error);

          // Extraire le message d'erreur du backend
          let errorMessage = 'Erreur lors de la création du rendez-vous';
          if (error.error && typeof error.error === 'string') {
            errorMessage = error.error;
          } else if (error.error && error.error.message) {
            errorMessage = error.error.message;
          } else if (error.message) {
            errorMessage = error.message;
          }

          this.snackBar.open(errorMessage, 'OK', {
            duration: 6000,
            panelClass: ['error-snackbar']
          });
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close(false);
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
}
