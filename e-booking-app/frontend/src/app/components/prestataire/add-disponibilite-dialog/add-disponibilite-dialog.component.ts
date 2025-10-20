import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PrestataireService } from '../../../services/prestataire.service';

@Component({
  selector: 'app-add-disponibilite-dialog',
  templateUrl: './add-disponibilite-dialog.component.html',
  styleUrls: ['./add-disponibilite-dialog.component.scss']
})
export class AddDisponibiliteDialogComponent implements OnInit {
  disponibiliteForm!: FormGroup;
  loading = false;
  isEditMode = false;

  jours = [
    { value: 'LUNDI', label: 'Lundi' },
    { value: 'MARDI', label: 'Mardi' },
    { value: 'MERCREDI', label: 'Mercredi' },
    { value: 'JEUDI', label: 'Jeudi' },
    { value: 'VENDREDI', label: 'Vendredi' },
    { value: 'SAMEDI', label: 'Samedi' },
    { value: 'DIMANCHE', label: 'Dimanche' }
  ];

  constructor(
    private fb: FormBuilder,
    private prestataireService: PrestataireService,
    private dialogRef: MatDialogRef<AddDisponibiliteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { prestataireId: number; disponibilite?: any }
  ) {}

  ngOnInit(): void {
    this.isEditMode = !!this.data.disponibilite;

    this.disponibiliteForm = this.fb.group({
      jourSemaine: [this.data.disponibilite?.jourSemaine || '', Validators.required],
      heureDebut: [this.data.disponibilite?.heureDebut || '', Validators.required],
      heureFin: [this.data.disponibilite?.heureFin || '', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.disponibiliteForm.invalid) {
      return;
    }

    this.loading = true;
    const disponibiliteData = {
      ...this.disponibiliteForm.value,
      prestataireId: this.data.prestataireId
    };

    if (this.isEditMode && this.data.disponibilite?.id) {
      // Update existing disponibilite
      this.prestataireService.updateDisponibilite(this.data.disponibilite.id, disponibiliteData).subscribe({
        next: (result: any) => {
          this.dialogRef.close(result);
        },
        error: (error: any) => {
          console.error('Error updating disponibilite:', error);
          this.loading = false;
        }
      });
    } else {
      // Create new disponibilite
      this.prestataireService.createDisponibilite(this.data.prestataireId, disponibiliteData).subscribe({
        next: (result: any) => {
          this.dialogRef.close(result);
        },
        error: (error: any) => {
          console.error('Error creating disponibilite:', error);
          this.loading = false;
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
