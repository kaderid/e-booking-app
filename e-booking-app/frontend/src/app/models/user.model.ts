export interface User {
  id?: number;
  prenom: string;
  nom: string;
  email: string;
  telephone: string;
  motDePasse?: string;
  role: 'CLIENT' | 'PROVIDER' | 'ADMIN';
  statut?: 'ACTIF' | 'BLOQUE';
}

export interface LoginRequest {
  email: string;
  motDePasse: string;
}

export interface AuthResponse {
  token: string;
}
