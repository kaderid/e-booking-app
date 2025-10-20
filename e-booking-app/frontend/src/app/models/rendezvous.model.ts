export interface RendezVous {
  id?: number;
  clientId?: number;
  clientPrenom?: string;
  clientNom?: string;
  prestataireId: number;
  prestatairePrenom?: string;
  prestataireNom?: string;
  serviceId?: number;
  serviceNom?: string;
  date: string;
  heure: string;
  statut?: 'EN_ATTENTE' | 'CONFIRME' | 'ANNULE' | 'TERMINE';
}

export interface Statistiques {
  nombreTotalRendezVous: number;
  nombreRendezVousEnAttente: number;
  nombreRendezVousConfirmes: number;
  nombreRendezVousAnnules: number;
  nombreTotalClients: number;
  nombreTotalPrestataires: number;
  nombreTotalServices: number;
}

export interface StatistiquePrestataire {
  prestataireId: number;
  prestatairePrenom: string;
  prestataireNom: string;
  prestataireEmail: string;
  serviceName: string;
  nombreRendezVous: number;
  nombreRendezVousConfirmes: number;
  nombreRendezVousEnAttente: number;
  nombreRendezVousAnnules: number;
  nombreClientsUniques: number;
}
