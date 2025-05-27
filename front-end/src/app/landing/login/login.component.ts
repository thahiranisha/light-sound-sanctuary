import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule // Add HttpClientModule for HttpClient
  ]
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  isLogin: boolean = true;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private http: HttpClient
  ) {
    this.loginForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(6)]]
    }, { validator: this.passwordMatchValidator });
  }

  ngOnInit() {
    this.setMode('login');
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { mismatch: true };
  }

  setMode(mode: 'login' | 'signup') {
    this.isLogin = mode === 'login';
    this.errorMessage = null;
    if (this.isLogin) {
      this.loginForm.get('name')?.clearValidators();
      this.loginForm.get('confirmPassword')?.clearValidators();
    } else {
      this.loginForm.get('name')?.setValidators([Validators.required, Validators.minLength(3)]);
      this.loginForm.get('confirmPassword')?.setValidators([Validators.required, Validators.minLength(6)]);
    }
    this.loginForm.get('name')?.updateValueAndValidity();
    this.loginForm.get('confirmPassword')?.updateValueAndValidity();
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const { email, password, name } = this.loginForm.value;
      const endpoint = this.isLogin ? '/api/auth/login' : '/api/auth/signup';
      const body = this.isLogin ? { email, password } : { email, password, username: name };
      this.http.post(`${environment.apiBaseUrl}${endpoint}`, body).subscribe({
        next: (response: any) => {
          localStorage.setItem('token', response.token);
          this.router.navigate(['/']);
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Authentication failed';
        }
      });
    }
  }

  signupWithGoogle() {
    window.location.href = `${environment.baseUrl}/oauth2/authorization/google`;
  }

  signupWithFacebook() {
    window.location.href = `${environment.apiBaseUrl}/oauth2/authorization/facebook`;
  }
}
