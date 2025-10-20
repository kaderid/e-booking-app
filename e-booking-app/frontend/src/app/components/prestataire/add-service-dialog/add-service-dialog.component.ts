import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceService } from '../../../services/service.service';

@Component({
  selector: 'app-add-service-dialog',
  templateUrl: './add-service-dialog.component.html',
  styleUrls: ['./add-service-dialog.component.scss']
})
export class AddServiceDialogComponent implements OnInit {
  serviceForm!: FormGroup;
  loading = false;
  isEditMode = false;

  constructor(
    private fb: FormBuilder,
    private serviceService: ServiceService,
    private dialogRef: MatDialogRef<AddServiceDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { prestataireId: number; service?: any }
  ) {}

  ngOnInit(): void {
    this.isEditMode = !!this.data.service;

    this.serviceForm = this.fb.group({
      nom: [this.data.service?.nom || '', Validators.required],
      description: [this.data.service?.description || '', Validators.required],
      duree: [this.data.service?.duree || '', [Validators.required, Validators.min(15)]],
      prix: [this.data.service?.prix || '', [Validators.required, Validators.min(0)]]
    });
  }

  onSubmit(): void {
    if (this.serviceForm.invalid) {
      return;
    }

    this.loading = true;
    const serviceData = {
      ...this.serviceForm.value,
      prestataireId: this.data.prestataireId
    };

    if (this.isEditMode && this.data.service?.id) {
      // Update existing service
      this.serviceService.updateService(this.data.service.id, serviceData).subscribe({
        next: (result: any) => {
          this.dialogRef.close(result);
        },
        error: (error: any) => {
          console.error('Error updating service:', error);
          this.loading = false;
        }
      });
    } else {
      // Create new service
      this.serviceService.createService(serviceData).subscribe({
        next: (result: any) => {
          this.dialogRef.close(result);
        },
        error: (error: any) => {
          console.error('Error creating service:', error);
          this.loading = false;
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
