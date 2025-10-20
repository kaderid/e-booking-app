import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AdminService } from './admin.service';
import { User } from '../models/user.model';
import { Statistiques, StatistiquePrestataire } from '../models/rendezvous.model';

describe('AdminService', () => {
  let service: AdminService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8080/api/admin';

  const mockUser: User = {
    id: 1,
    prenom: 'John',
    nom: 'Doe',
    email: 'john@test.com',
    telephone: '+221771234567',
    role: 'CLIENT',
    statut: 'ACTIF'
  };

  const mockStatistiques: Statistiques = {
    nombreTotalRendezVous: 100,
    nombreRendezVousEnAttente: 20,
    nombreRendezVousConfirmes: 60,
    nombreRendezVousAnnules: 20,
    nombreTotalClients: 50,
    nombreTotalPrestataires: 10,
    nombreTotalServices: 5
  };

  const mockStatistiquesPrestataires: StatistiquePrestataire[] = [
    {
      prestataireId: 1,
      prestatairePrenom: 'Marie',
      prestataireNom: 'Dupont',
      prestataireEmail: 'marie@test.com',
      serviceName: 'Coiffure',
      nombreRendezVous: 30,
      nombreRendezVousConfirmes: 25,
      nombreRendezVousEnAttente: 3,
      nombreRendezVousAnnules: 2,
      nombreClientsUniques: 20
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AdminService]
    });

    service = TestBed.inject(AdminService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getAllUsers', () => {
    it('should return all users', (done) => {
      const mockUsers: User[] = [mockUser];

      service.getAllUsers().subscribe(users => {
        expect(users).toEqual(mockUsers);
        expect(users.length).toBe(1);
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/users`);
      expect(req.request.method).toBe('GET');
      req.flush(mockUsers);
    });

    it('should handle error when getting all users', (done) => {
      service.getAllUsers().subscribe(
        () => fail('should have failed'),
        (error) => {
          expect(error.status).toBe(500);
          done();
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/users`);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });
  });

  describe('getUserById', () => {
    it('should return a user by id', (done) => {
      const userId = 1;

      service.getUserById(userId).subscribe(user => {
        expect(user).toEqual(mockUser);
        expect(user.id).toBe(userId);
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/users/${userId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockUser);
    });

    it('should handle user not found error', (done) => {
      const userId = 999;

      service.getUserById(userId).subscribe(
        () => fail('should have failed'),
        (error) => {
          expect(error.status).toBe(404);
          done();
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/users/${userId}`);
      req.flush('Not found', { status: 404, statusText: 'Not Found' });
    });
  });

  describe('activerCompte', () => {
    it('should activate a user account', (done) => {
      const userId = 1;
      const activatedUser = { ...mockUser, statut: 'ACTIF' };

      service.activerCompte(userId).subscribe(user => {
        expect(user.statut).toBe('ACTIF');
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/users/${userId}/activer`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual({});
      req.flush(activatedUser);
    });
  });

  describe('bloquerCompte', () => {
    it('should block a user account', (done) => {
      const userId = 1;
      const blockedUser = { ...mockUser, statut: 'BLOQUE' };

      service.bloquerCompte(userId).subscribe(user => {
        expect(user.statut).toBe('BLOQUE');
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/users/${userId}/bloquer`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual({});
      req.flush(blockedUser);
    });
  });

  describe('getStatistiques', () => {
    it('should return global statistics', (done) => {
      service.getStatistiques().subscribe(stats => {
        expect(stats).toEqual(mockStatistiques);
        expect(stats.nombreTotalRendezVous).toBe(100);
        expect(stats.nombreTotalClients).toBe(50);
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/statistiques`);
      expect(req.request.method).toBe('GET');
      req.flush(mockStatistiques);
    });
  });

  describe('getStatistiquesPrestataires', () => {
    it('should return provider statistics', (done) => {
      service.getStatistiquesPrestataires().subscribe(stats => {
        expect(stats).toEqual(mockStatistiquesPrestataires);
        expect(stats.length).toBe(1);
        expect(stats[0].prestatairePrenom).toBe('Marie');
        expect(stats[0].nombreRendezVous).toBe(30);
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/statistiques/prestataires`);
      expect(req.request.method).toBe('GET');
      req.flush(mockStatistiquesPrestataires);
    });

    it('should return empty array if no providers', (done) => {
      service.getStatistiquesPrestataires().subscribe(stats => {
        expect(stats).toEqual([]);
        expect(stats.length).toBe(0);
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/statistiques/prestataires`);
      req.flush([]);
    });
  });
});
