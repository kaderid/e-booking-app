import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ServiceService } from './service.service';
import { Service } from '../models/service.model';

describe('ServiceService', () => {
  let service: ServiceService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8080/api/services';

  const mockService: Service = {
    id: 1,
    nom: 'Coiffure',
    description: 'Services de coiffure professionnelle',
    prix: 50,
    duree: 60
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ServiceService]
    });

    service = TestBed.inject(ServiceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all services', (done) => {
    const mockServices: Service[] = [mockService];

    service.getAllServices().subscribe(services => {
      expect(services).toEqual(mockServices);
      done();
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockServices);
  });

  it('should get service by id', (done) => {
    const id = 1;

    service.getServiceById(id).subscribe(result => {
      expect(result).toEqual(mockService);
      done();
    });

    const req = httpMock.expectOne(`${apiUrl}/${id}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockService);
  });

  it('should create service', (done) => {
    const newService = { ...mockService, id: undefined };

    service.createService(newService).subscribe(result => {
      expect(result).toEqual(mockService);
      done();
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    req.flush(mockService);
  });

  it('should update service', (done) => {
    const id = 1;
    const updatedService = { ...mockService, prix: 60 };

    service.updateService(id, updatedService).subscribe(result => {
      expect(result.prix).toBe(60);
      done();
    });

    const req = httpMock.expectOne(`${apiUrl}/${id}`);
    expect(req.request.method).toBe('PUT');
    req.flush(updatedService);
  });

  it('should delete service', (done) => {
    const id = 1;

    service.deleteService(id).subscribe(() => {
      expect(true).toBe(true);
      done();
    });

    const req = httpMock.expectOne(`${apiUrl}/${id}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should get services by prestataire id', (done) => {
    const prestataireId = 1;
    const mockServices: Service[] = [mockService];

    service.getServicesByPrestataireId(prestataireId).subscribe(services => {
      expect(services).toEqual(mockServices);
      done();
    });

    const req = httpMock.expectOne(`${apiUrl}/prestataire/${prestataireId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockServices);
  });
});
