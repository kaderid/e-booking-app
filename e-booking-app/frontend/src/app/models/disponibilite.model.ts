export interface Disponibilite {
  id?: number;
  prestataireId: number;
  jourSemaine: 'Lundi' | 'Mardi' | 'Mercredi' | 'Jeudi' | 'Vendredi' | 'Samedi' | 'Dimanche';
  heureDebut: string;
  heureFin: string;
}
