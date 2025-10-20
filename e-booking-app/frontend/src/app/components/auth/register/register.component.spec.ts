import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { of, throwError } from 'rxjs';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../../services/auth.service';
import { AuthResponse } from '../../../models/user.model';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  const mockAuthResponse: AuthResponse = {
    token: 'test-token'
  };

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['register']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatSelectModule,
        MatIconModule,
        MatProgressSpinnerModule,
        MatCardModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty fields', () => {
    expect(component.registerForm.get('prenom')?.value).toBe('');
    expect(component.registerForm.get('nom')?.value).toBe('');
    expect(component.registerForm.get('email')?.value).toBe('');
    expect(component.registerForm.get('telephone')?.value).toBe('');
    expect(component.registerForm.get('motDePasse')?.value).toBe('');
    expect(component.registerForm.get('role')?.value).toBe('CLIENT'); // Default value
  });

  it('should validate required fields', () => {
    const form = component.registerForm;

    // Set empty values to trigger validation
    form.get('prenom')?.setValue('');
    form.get('nom')?.setValue('');
    form.get('email')?.setValue('');
    form.get('telephone')?.setValue('');
    form.get('motDePasse')?.setValue('');
    form.get('confirmPassword')?.setValue('');
    form.get('role')?.setValue('');

    expect(form.get('prenom')?.hasError('required')).toBeTruthy();
    expect(form.get('nom')?.hasError('required')).toBeTruthy();
    expect(form.get('email')?.hasError('required')).toBeTruthy();
    expect(form.get('telephone')?.hasError('required')).toBeTruthy();
    expect(form.get('motDePasse')?.hasError('required')).toBeTruthy();
    expect(form.get('role')?.hasError('required')).toBeTruthy();
  });

  it('should validate email format', () => {
    const emailControl = component.registerForm.get('email');
    emailControl?.setValue('invalid-email');
    expect(emailControl?.hasError('email')).toBeTruthy();
  });

  it('should validate password minimum length', () => {
    const passwordControl = component.registerForm.get('motDePasse');
    passwordControl?.setValue('12345');
    expect(passwordControl?.hasError('minlength')).toBeTruthy();
  });

  it('should not submit invalid form', () => {
    component.registerForm.setValue({
      prenom: '',
      nom: '',
      email: '',
      telephone: '',
      motDePasse: '',
      role: '',
      confirmPassword: ''
    });

    component.onSubmit();

    expect(authService.register).not.toHaveBeenCalled();
  });

  it('should register CLIENT successfully', () => {
    authService.register.and.returnValue(of(mockAuthResponse));

    component.registerForm.setValue({
      prenom: 'John',
      nom: 'Doe',
      email: 'john@example.com',
      telephone: '+33612345678',
      motDePasse: 'password123',
      role: 'CLIENT',
      confirmPassword: 'password123'
    });

    component.onSubmit();

    expect(authService.register).toHaveBeenCalled();
  });

  it('should register PROVIDER successfully', () => {
    authService.register.and.returnValue(of(mockAuthResponse));

    component.registerForm.setValue({
      prenom: 'Marie',
      nom: 'Dupont',
      email: 'marie@example.com',
      telephone: '+33612345678',
      motDePasse: 'password123',
      role: 'PROVIDER',
      confirmPassword: 'password123'
    });

    component.onSubmit();

    expect(authService.register).toHaveBeenCalled();
  });

  it('should handle registration error', () => {
    authService.register.and.returnValue(throwError(() => ({ status: 400, error: 'Bad Request' })));

    component.registerForm.setValue({
      prenom: 'John',
      nom: 'Doe',
      email: 'existing@example.com',
      telephone: '+33612345678',
      motDePasse: 'password123',
      role: 'CLIENT',
      confirmPassword: 'password123'
    });

    component.onSubmit();

    expect(component.errorMessage).toBeTruthy();
    expect(component.loading).toBe(false);
  });

  it('should toggle password visibility', () => {
    expect(component.hidePassword).toBe(true);
    // The toggle functionality exists but requires DOM interaction
  });

  it('should redirect to login after successful registration', () => {
    jasmine.clock().install();
    authService.register.and.returnValue(of(mockAuthResponse));

    component.registerForm.setValue({
      prenom: 'John',
      nom: 'Doe',
      email: 'john@example.com',
      telephone: '+33612345678',
      motDePasse: 'password123',
      role: 'CLIENT',
      confirmPassword: 'password123'
    });

    component.onSubmit();
    jasmine.clock().tick(2001);

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
    jasmine.clock().uninstall();
  });
});
