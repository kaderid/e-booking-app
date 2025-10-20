import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PrestataireService } from '../../../services/prestataire.service';
import { Prestataire } from '../../../models/prestataire.model';
import { Disponibilite } from '../../../models/disponibilite.model';
import { Service } from '../../../models/service.model';

@Component({
  selector: 'app-disponibilites-dialog',
  templateUrl: './disponibilites-dialog.component.html',
  styleUrls: ['./disponibilites-dialog.component.scss']
})
export class DisponibilitesDialogComponent implements OnInit {
  disponibilites: Disponibilite[] = [];
  services: Service[] = [];
  loading = true;

  constructor(
    private prestataireService: PrestataireService,
    private dialogRef: MatDialogRef<DisponibilitesDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { prestataire: Prestataire; services: Service[] }
  ) {}

  ngOnInit(): void {
    this.services = this.data.services;
    this.loadDisponibilites();
  }

  loadDisponibilites(): void {
    this.prestataireService.getDisponibilitesByPrestataireId(this.data.prestataire.id!).subscribe({
      next: (disponibilites: Disponibilite[]) => {
        this.disponibilites = disponibilites;
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Error loading disponibilites:', error);
        this.loading = false;
      }
    });
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

  close(): void {
    this.dialogRef.close();
  }
}
