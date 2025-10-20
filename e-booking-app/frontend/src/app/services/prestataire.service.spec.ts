import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PrestataireService } from './prestataire.service';
import { Prestataire } from '../models/prestataire.model';

describe('PrestataireService', () => {
  let service: PrestataireService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8080/api/prestataires';

  const mockPrestataire: Prestataire = {
    id: 1,
    prenom: 'John',
    nom: 'Doe',
    specialite: 'Coiffure',
    adresse: '123 Rue de Test',
    description: 'Professionnel de la coiffure',
    serviceNom: 'Coiffure'
  };

  const mockDisponibilite = {
    id: 1,
    prestataireId: 1,
    jourSemaine: 'Lundi',
    heureDebut: '09:00',
    heureFin: '17:00'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PrestataireService]
    });

    service = TestBed.inject(PrestataireService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all prestataires', (done) => {
    const mockPrestataires: Prestataire[] = [mockPrestataire];

    service.getAllPrestataires().subscribe(prestataires => {
      expect(prestataires).toEqual(mockPrestataires);
      done();
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockPrestataires);
  });

  it('should get prestataire by id', (done) => {
    const id = 1;

    service.getPrestataireById(id).subscribe(result => {
      expect(result).toEqual(mockPrestataire);
      done();
    });

    const req = httpMock.expectOne(`${apiUrl}/${id}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPrestataire);
  });

  it('should create prestataire', (done) => {
    const newPrestataire = { ...mockPrestataire, id: undefined };

    service.createPrestataire(newPrestataire).subscribe(result => {
      expect(result).toEqual(mockPrestataire);
      done();
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    req.flush(mockPrestataire);
  });

  it('should update prestataire', (done) => {
    const id = 1;
    const updatedPrestataire = { ...mockPrestataire, specialite: 'Coiffure et Esthétique' };

    service.updatePrestataire(id, updatedPrestataire).subscribe(result => {
      expect(result.specialite).toBe('Coiffure et Esthétique');
      done();
    });

    const req = httpMock.expectOne(`${apiUrl}/${id}`);
    expect(req.request.method).toBe('PUT');
    req.flush(updatedPrestataire);
  });

  it('should delete prestataire', (done) => {
    const id = 1;

    service.deletePrestataire(id).subscribe(() => {
      expect(true).toBe(true);
      done();
    });

    const req = httpMock.expectOne(`${apiUrl}/${id}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should get disponibilites by prestataire id', (done) => {
    const prestataireId = 1;
    const mockDisponibilites = [mockDisponibilite];

    service.getDisponibilitesByPrestataireId(prestataireId).subscribe(disponibilites => {
      expect(disponibilites).toEqual(mockDisponibilites);
      done();
    });

    const req = httpMock.expectOne(`http://localhost:8080/api/disponibilites/${prestataireId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockDisponibilites);
  });

  it('should create disponibilite', (done) => {
    const prestataireId = 1;

    service.createDisponibilite(prestataireId, mockDisponibilite).subscribe(result => {
      expect(result).toEqual(mockDisponibilite);
      done();
    });

    const req = httpMock.expectOne('http://localhost:8080/api/disponibilites');
    expect(req.request.method).toBe('POST');
    req.flush(mockDisponibilite);
  });

  it('should update disponibilite', (done) => {
    const id = 1;
    const updated = { ...mockDisponibilite, heureDebut: '08:00' };

    service.updateDisponibilite(id, updated).subscribe(result => {
      expect(result.heureDebut).toBe('08:00');
      done();
    });

    const req = httpMock.expectOne(`http://localhost:8080/api/disponibilites/${id}`);
    expect(req.request.method).toBe('PUT');
    req.flush(updated);
  });

  it('should delete disponibilite', (done) => {
    const id = 1;

    service.deleteDisponibilite(id).subscribe(() => {
      expect(true).toBe(true);
      done();
    });

    const req = httpMock.expectOne(`http://localhost:8080/api/disponibilites/${id}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
