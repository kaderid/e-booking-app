import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;
  hidePassword = true;
  errorMessage = '';
  returnUrl = '/';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

    // Check if user was redirected due to blocked account
    if (this.route.snapshot.queryParams['blocked'] === 'true') {
      this.errorMessage = 'Votre compte a été bloqué par l\'administrateur. Veuillez contacter le support client pour plus d\'informations.';
    }
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    // Map 'password' to 'motDePasse' for backend
    const loginData = {
      email: this.loginForm.value.email,
      motDePasse: this.loginForm.value.password
    };

    this.authService.login(loginData).subscribe({
      next: () => {
        const user = this.authService.currentUserValue;
        if (user?.role === 'ADMIN') {
          this.router.navigate(['/admin/dashboard']);
        } else if (user?.role === 'PROVIDER') {
          this.router.navigate(['/prestataire/dashboard']);
        } else {
          this.router.navigate(['/client/dashboard']);
        }
      },
      error: (error) => {
        this.loading = false;

        // Check if error response has specific error type
        if (error.error && error.error.error === 'ACCOUNT_BLOCKED') {
          this.errorMessage = error.error.message || 'Votre compte a été bloqué par l\'administrateur. Veuillez contacter le support client pour plus d\'informations.';
        } else if (error.error && error.error.message) {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = 'Email ou mot de passe incorrect';
        }

        console.error('Login error:', error);
      }
    });
  }
}
