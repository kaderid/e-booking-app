import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Prestataire } from '../models/prestataire.model';

@Injectable({
  providedIn: 'root'
})
export class PrestataireService {
  private apiUrl = 'http://localhost:8080/api/prestataires';

  constructor(private http: HttpClient) {}

  getAllPrestataires(): Observable<Prestataire[]> {
    return this.http.get<Prestataire[]>(this.apiUrl);
  }

  getPrestataireById(id: number): Observable<Prestataire> {
    return this.http.get<Prestataire>(`${this.apiUrl}/${id}`);
  }

  createPrestataire(prestataire: Prestataire): Observable<Prestataire> {
    return this.http.post<Prestataire>(this.apiUrl, prestataire);
  }

  updatePrestataire(id: number, prestataire: Prestataire): Observable<Prestataire> {
    return this.http.put<Prestataire>(`${this.apiUrl}/${id}`, prestataire);
  }

  deletePrestataire(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getDisponibilitesByPrestataireId(prestataireId: number): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8080/api/disponibilites/${prestataireId}`);
  }

  createDisponibilite(prestataireId: number, disponibilite: any): Observable<any> {
    return this.http.post<any>('http://localhost:8080/api/disponibilites', disponibilite);
  }

  updateDisponibilite(id: number, disponibilite: any): Observable<any> {
    return this.http.put<any>(`http://localhost:8080/api/disponibilites/${id}`, disponibilite);
  }

  deleteDisponibilite(id: number): Observable<void> {
    return this.http.delete<void>(`http://localhost:8080/api/disponibilites/${id}`);
  }
}
