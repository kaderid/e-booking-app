import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { User, LoginRequest, AuthResponse } from '../models/user.model';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let routerSpy: jasmine.SpyObj<Router>;

  const mockUser: User = {
    id: 1,
    prenom: 'John',
    nom: 'Doe',
    email: 'john@test.com',
    telephone: '+221771234567',
    role: 'CLIENT'
  };

  const mockAuthResponse: AuthResponse = {
    token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQHRlc3QuY29tIiwidXNlcklkIjoxLCJyb2xlIjoiQ0xJRU5UIn0.test'
  };

  beforeEach(() => {
    const routerSpyObj = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AuthService,
        { provide: Router, useValue: routerSpyObj }
      ]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    routerSpy = TestBed.inject(Router) as jasmine.SpyObj<Router>;

    // Clear localStorage before each test
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('register', () => {
    it('should register a user and store token', (done) => {
      service.register(mockUser).subscribe(response => {
        expect(response).toEqual(mockAuthResponse);
        expect(localStorage.getItem('token')).toBe(mockAuthResponse.token);
        expect(service.currentUserValue).toEqual(mockUser);
        done();
      });

      const req = httpMock.expectOne('http://localhost:8080/api/auth/register');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockUser);
      req.flush(mockAuthResponse);
    });

    it('should handle registration error', (done) => {
      const errorMessage = 'Registration failed';

      service.register(mockUser).subscribe(
        () => fail('should have failed'),
        (error) => {
          expect(error.status).toBe(400);
          done();
        }
      );

      const req = httpMock.expectOne('http://localhost:8080/api/auth/register');
      req.flush(errorMessage, { status: 400, statusText: 'Bad Request' });
    });
  });

  describe('login', () => {
    it('should login a user and store token', (done) => {
      const credentials: LoginRequest = {
        email: 'john@test.com',
        motDePasse: 'password123'
      };

      service.login(credentials).subscribe(response => {
        expect(response).toEqual(mockAuthResponse);
        expect(localStorage.getItem('token')).toBe(mockAuthResponse.token);
        expect(service.currentUserValue).toBeTruthy();
        expect(service.currentUserValue?.email).toBe('john@test.com');
        done();
      });

      const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(credentials);
      req.flush(mockAuthResponse);
    });

    it('should handle login error', (done) => {
      const credentials: LoginRequest = {
        email: 'wrong@test.com',
        motDePasse: 'wrongpass'
      };

      service.login(credentials).subscribe(
        () => fail('should have failed'),
        (error) => {
          expect(error.status).toBe(401);
          done();
        }
      );

      const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
      req.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });
    });
  });

  describe('logout', () => {
    it('should clear storage and navigate to login', () => {
      // Set up some data
      localStorage.setItem('token', 'test-token');
      localStorage.setItem('currentUser', JSON.stringify(mockUser));

      service.logout();

      expect(localStorage.getItem('token')).toBeNull();
      expect(localStorage.getItem('currentUser')).toBeNull();
      expect(service.currentUserValue).toBeNull();
      expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
    });
  });

  describe('getToken', () => {
    it('should return token from localStorage', () => {
      const token = 'test-token';
      localStorage.setItem('token', token);

      expect(service.getToken()).toBe(token);
    });

    it('should return null if no token exists', () => {
      expect(service.getToken()).toBeNull();
    });
  });

  describe('isAuthenticated', () => {
    it('should return true if token exists', () => {
      localStorage.setItem('token', 'test-token');
      expect(service.isAuthenticated()).toBe(true);
    });

    it('should return false if no token exists', () => {
      expect(service.isAuthenticated()).toBe(false);
    });
  });

  describe('hasRole', () => {
    it('should return true if user has the specified role', () => {
      // Simulate login by setting the token and user
      localStorage.setItem('token', mockAuthResponse.token);

      const credentials: LoginRequest = {
        email: 'john@test.com',
        motDePasse: 'password123'
      };

      service.login(credentials).subscribe(() => {
        expect(service.hasRole('CLIENT')).toBe(true);
      });

      const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
      req.flush(mockAuthResponse);
    });

    it('should return false if user does not have the specified role', () => {
      localStorage.setItem('token', mockAuthResponse.token);

      const credentials: LoginRequest = {
        email: 'john@test.com',
        motDePasse: 'password123'
      };

      service.login(credentials).subscribe(() => {
        expect(service.hasRole('ADMIN')).toBe(false);
      });

      const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
      req.flush(mockAuthResponse);
    });

    it('should return false if no user is logged in', () => {
      expect(service.hasRole('CLIENT')).toBe(false);
    });
  });

  describe('currentUserValue', () => {
    it('should return the current user after login', (done) => {
      const credentials: LoginRequest = {
        email: 'john@test.com',
        motDePasse: 'password123'
      };

      service.login(credentials).subscribe(() => {
        expect(service.currentUserValue).toBeTruthy();
        expect(service.currentUserValue?.email).toBe('john@test.com');
        done();
      });

      const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
      req.flush(mockAuthResponse);
    });

    it('should return null if no user is logged in', () => {
      expect(service.currentUserValue).toBeNull();
    });
  });
});
