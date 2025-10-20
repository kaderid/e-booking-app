import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { Statistiques, StatistiquePrestataire } from '../models/rendezvous.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/users`);
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/${id}`);
  }

  activerCompte(userId: number): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/users/${userId}/activer`, {});
  }

  bloquerCompte(userId: number): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/users/${userId}/bloquer`, {});
  }

  getStatistiques(): Observable<Statistiques> {
    return this.http.get<Statistiques>(`${this.apiUrl}/statistiques`);
  }

  getStatistiquesPrestataires(): Observable<StatistiquePrestataire[]> {
    return this.http.get<StatistiquePrestataire[]>(`${this.apiUrl}/statistiques/prestataires`);
  }
}
