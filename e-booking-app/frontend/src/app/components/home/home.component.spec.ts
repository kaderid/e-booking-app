import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { HomeComponent } from './home.component';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  const mockUser: User = {
    id: 1,
    email: 'test@example.com',
    role: 'CLIENT',
    prenom: 'John',
    nom: 'Doe',
    telephone: '+221771234567'
  };

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['isAuthenticated', 'logout'], {
      currentUserValue: mockUser
    });
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [HomeComponent],
      imports: [
        HttpClientTestingModule,
        MatToolbarModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        MatMenuModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with current user and authentication status', () => {
    authService.isAuthenticated.and.returnValue(true);
    fixture.detectChanges();

    expect(component.currentUser).toEqual(mockUser);
    expect(component.isAuthenticated).toBe(true);
  });

  it('should initialize features array', () => {
    fixture.detectChanges();
    expect(component.features).toBeDefined();
    expect(component.features.length).toBe(4);
    expect(component.features[0].title).toBe('Réservation facile');
  });

  it('should initialize services array', () => {
    fixture.detectChanges();
    expect(component.services).toBeDefined();
    expect(component.services.length).toBe(4);
    expect(component.services[0].name).toBe('Coiffure');
  });

  it('should navigate to login', () => {
    component.navigateToLogin();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should navigate to register', () => {
    component.navigateToRegister();
    expect(router.navigate).toHaveBeenCalledWith(['/register']);
  });

  it('should navigate to CLIENT dashboard', () => {
    component.currentUser = { ...mockUser, role: 'CLIENT' };
    component.navigateToDashboard();
    expect(router.navigate).toHaveBeenCalledWith(['/client/dashboard']);
  });

  it('should navigate to ADMIN dashboard', () => {
    component.currentUser = { ...mockUser, role: 'ADMIN' };
    component.navigateToDashboard();
    expect(router.navigate).toHaveBeenCalledWith(['/admin/dashboard']);
  });

  it('should navigate to PROVIDER dashboard', () => {
    component.currentUser = { ...mockUser, role: 'PROVIDER' };
    component.navigateToDashboard();
    expect(router.navigate).toHaveBeenCalledWith(['/prestataire/dashboard']);
  });

  it('should navigate to login if user has no role', () => {
    component.currentUser = { ...mockUser, role: 'UNKNOWN' as any };
    component.navigateToDashboard();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should logout user', () => {
    component.logout();
    expect(authService.logout).toHaveBeenCalled();
  });

  it('should render features in template', () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    const featureCards = compiled.querySelectorAll('.feature-card');
    expect(featureCards.length).toBeGreaterThan(0);
  });

  it('should render services in template', () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    const serviceCards = compiled.querySelectorAll('.service-card');
    expect(serviceCards.length).toBeGreaterThan(0);
  });
});
