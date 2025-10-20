import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RendezVousService } from './rendezvous.service';
import { RendezVous } from '../models/rendezvous.model';

describe('RendezVousService', () => {
  let service: RendezVousService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8080/api/rendezvous';

  const mockRendezVous: RendezVous = {
    id: 1,
    clientId: 1,
    clientPrenom: 'John',
    clientNom: 'Doe',
    prestataireId: 2,
    prestatairePrenom: 'Marie',
    prestataireNom: 'Dupont',
    serviceId: 1,
    serviceNom: 'Coiffure',
    date: '2025-10-25',
    heure: '10:00',
    statut: 'EN_ATTENTE'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [RendezVousService]
    });

    service = TestBed.inject(RendezVousService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getAllRendezVous', () => {
    it('should return all rendez-vous', (done) => {
      const mockRendezVousList: RendezVous[] = [mockRendezVous];

      service.getAllRendezVous().subscribe(rdvs => {
        expect(rdvs).toEqual(mockRendezVousList);
        expect(rdvs.length).toBe(1);
        done();
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('GET');
      req.flush(mockRendezVousList);
    });

    it('should return empty array if no rendez-vous', (done) => {
      service.getAllRendezVous().subscribe(rdvs => {
        expect(rdvs).toEqual([]);
        expect(rdvs.length).toBe(0);
        done();
      });

      const req = httpMock.expectOne(apiUrl);
      req.flush([]);
    });
  });

  describe('getRendezVousByClientId', () => {
    it('should return rendez-vous for a specific client', (done) => {
      const clientId = 1;
      const mockClientRendezVous: RendezVous[] = [mockRendezVous];

      service.getRendezVousByClientId(clientId).subscribe(rdvs => {
        expect(rdvs).toEqual(mockClientRendezVous);
        expect(rdvs.length).toBe(1);
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/client/${clientId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockClientRendezVous);
    });

    it('should return empty array if client has no rendez-vous', (done) => {
      const clientId = 999;

      service.getRendezVousByClientId(clientId).subscribe(rdvs => {
        expect(rdvs).toEqual([]);
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/client/${clientId}`);
      req.flush([]);
    });
  });

  describe('getRendezVousByPrestataireId', () => {
    it('should return rendez-vous for a specific provider', (done) => {
      const prestataireId = 2;
      const mockProviderRendezVous: RendezVous[] = [mockRendezVous];

      service.getRendezVousByPrestataireId(prestataireId).subscribe(rdvs => {
        expect(rdvs).toEqual(mockProviderRendezVous);
        expect(rdvs.length).toBe(1);
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/prestataire/${prestataireId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockProviderRendezVous);
    });

    it('should return empty array if provider has no rendez-vous', (done) => {
      const prestataireId = 999;

      service.getRendezVousByPrestataireId(prestataireId).subscribe(rdvs => {
        expect(rdvs).toEqual([]);
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/prestataire/${prestataireId}`);
      req.flush([]);
    });
  });

  describe('createRendezVous', () => {
    it('should create a new rendez-vous', (done) => {
      const newRendezVous: RendezVous = {
        ...mockRendezVous,
        id: undefined
      };

      service.createRendezVous(newRendezVous).subscribe(rdv => {
        expect(rdv).toEqual(mockRendezVous);
        expect(rdv.id).toBe(1);
        done();
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(newRendezVous);
      req.flush(mockRendezVous);
    });

    it('should handle creation error', (done) => {
      const newRendezVous: RendezVous = { ...mockRendezVous, id: undefined };

      service.createRendezVous(newRendezVous).subscribe(
        () => fail('should have failed'),
        (error) => {
          expect(error.status).toBe(400);
          done();
        }
      );

      const req = httpMock.expectOne(apiUrl);
      req.flush('Bad Request', { status: 400, statusText: 'Bad Request' });
    });
  });

  describe('updateRendezVous', () => {
    it('should update an existing rendez-vous', (done) => {
      const id = 1;
      const updatedRendezVous: RendezVous = {
        ...mockRendezVous,
        statut: 'CONFIRME'
      };

      service.updateRendezVous(id, updatedRendezVous).subscribe(rdv => {
        expect(rdv.statut).toBe('CONFIRME');
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/${id}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updatedRendezVous);
      req.flush(updatedRendezVous);
    });

    it('should handle update error', (done) => {
      const id = 999;

      service.updateRendezVous(id, mockRendezVous).subscribe(
        () => fail('should have failed'),
        (error) => {
          expect(error.status).toBe(404);
          done();
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/${id}`);
      req.flush('Not Found', { status: 404, statusText: 'Not Found' });
    });
  });

  describe('deleteRendezVous', () => {
    it('should delete a rendez-vous', (done) => {
      const id = 1;

      service.deleteRendezVous(id).subscribe(() => {
        expect(true).toBe(true);
        done();
      });

      const req = httpMock.expectOne(`${apiUrl}/${id}`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });

    it('should handle delete error', (done) => {
      const id = 999;

      service.deleteRendezVous(id).subscribe(
        () => fail('should have failed'),
        (error) => {
          expect(error.status).toBe(404);
          done();
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/${id}`);
      req.flush('Not Found', { status: 404, statusText: 'Not Found' });
    });
  });
});
