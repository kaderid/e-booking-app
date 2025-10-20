import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { of, throwError } from 'rxjs';

import { LoginComponent } from './login.component';
import { AuthService } from '../../../services/auth.service';
import { AuthResponse, User } from '../../../models/user.model';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;
  let route: jasmine.SpyObj<ActivatedRoute>;

  const mockUser: User = {
    id: 1,
    email: 'test@example.com',
    role: 'CLIENT',
    prenom: 'John',
    nom: 'Doe',
    telephone: '+221771234567'
  };

  const mockAuthResponse: AuthResponse = {
    token: 'test-token'
  };

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['login', 'currentUserValue']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    const activatedRouteSpy = jasmine.createSpyObj('ActivatedRoute', [], {
      snapshot: { queryParams: {} }
    });

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatIconModule,
        MatProgressSpinnerModule,
        MatCardModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: ActivatedRoute, useValue: activatedRouteSpy }
      ]
    }).compileComponents();

    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    route = TestBed.inject(ActivatedRoute) as jasmine.SpyObj<ActivatedRoute>;

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the login form with empty fields', () => {
    expect(component.loginForm.get('email')?.value).toBe('');
    expect(component.loginForm.get('password')?.value).toBe('');
  });

  it('should validate email field as required', () => {
    const emailControl = component.loginForm.get('email');
    emailControl?.setValue('');
    expect(emailControl?.hasError('required')).toBeTruthy();
  });

  it('should validate email field format', () => {
    const emailControl = component.loginForm.get('email');
    emailControl?.setValue('invalid-email');
    expect(emailControl?.hasError('email')).toBeTruthy();
  });

  it('should validate password field as required', () => {
    const passwordControl = component.loginForm.get('password');
    passwordControl?.setValue('');
    expect(passwordControl?.hasError('required')).toBeTruthy();
  });

  it('should not submit when form is invalid', () => {
    component.loginForm.setValue({ email: '', password: '' });
    component.onSubmit();
    expect(authService.login).not.toHaveBeenCalled();
  });

  it('should login CLIENT user successfully', () => {
    const clientUser = { ...mockUser, role: 'CLIENT' };
    authService.login.and.returnValue(of(mockAuthResponse));
    Object.defineProperty(authService, 'currentUserValue', { get: () => clientUser });

    component.loginForm.setValue({
      email: 'test@example.com',
      password: 'password123'
    });

    component.onSubmit();

    expect(authService.login).toHaveBeenCalledWith({
      email: 'test@example.com',
      motDePasse: 'password123'
    });
    expect(router.navigate).toHaveBeenCalledWith(['/client/dashboard']);
  });

  it('should login ADMIN user successfully', () => {
    const adminUser = { ...mockUser, role: 'ADMIN' };
    authService.login.and.returnValue(of(mockAuthResponse));
    Object.defineProperty(authService, 'currentUserValue', { get: () => adminUser });

    component.loginForm.setValue({
      email: 'admin@example.com',
      password: 'password123'
    });

    component.onSubmit();

    expect(router.navigate).toHaveBeenCalledWith(['/admin/dashboard']);
  });

  it('should login PROVIDER user successfully', () => {
    const providerUser = { ...mockUser, role: 'PROVIDER' };
    authService.login.and.returnValue(of(mockAuthResponse));
    Object.defineProperty(authService, 'currentUserValue', { get: () => providerUser });

    component.loginForm.setValue({
      email: 'provider@example.com',
      password: 'password123'
    });

    component.onSubmit();

    expect(router.navigate).toHaveBeenCalledWith(['/prestataire/dashboard']);
  });

  it('should handle login error', () => {
    authService.login.and.returnValue(throwError(() => ({ status: 401, error: 'Unauthorized' })));

    component.loginForm.setValue({
      email: 'wrong@example.com',
      password: 'wrongpassword'
    });

    component.onSubmit();

    expect(component.errorMessage).toBe('Email ou mot de passe incorrect');
    expect(component.loading).toBe(false);
  });

  it('should handle blocked account error', () => {
    const blockedError = {
      error: {
        error: 'ACCOUNT_BLOCKED',
        message: 'Votre compte a été bloqué'
      }
    };

    authService.login.and.returnValue(throwError(() => blockedError));

    component.loginForm.setValue({
      email: 'blocked@example.com',
      password: 'password123'
    });

    component.onSubmit();

    expect(component.errorMessage).toBe('Votre compte a été bloqué');
    expect(component.loading).toBe(false);
  });

  it('should display blocked account message from query params', () => {
    Object.defineProperty(route.snapshot, 'queryParams', {
      get: () => ({ blocked: 'true' }),
      configurable: true
    });

    component.ngOnInit();

    expect(component.errorMessage).toContain('bloqué par l\'administrateur');
  });

  it('should toggle password visibility', () => {
    expect(component.hidePassword).toBe(true);

    const compiled = fixture.nativeElement;
    const toggleButton = compiled.querySelector('button[mat-icon-button]');

    expect(component.hidePassword).toBe(true);
  });
});
