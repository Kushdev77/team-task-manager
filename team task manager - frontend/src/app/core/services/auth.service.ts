import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, SignupRequest, StoredUser } from '../models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly api = `${environment.apiUrl}/auth`;
  private readonly userSignal = signal<StoredUser | null>(this.loadUser());

  readonly user = this.userSignal.asReadonly();
  readonly isAuthenticated = computed(() => !!this.userSignal()?.token);

  constructor(private http: HttpClient) {}

  signup(request: SignupRequest) {
    return this.http.post<AuthResponse>(`${this.api}/signup`, this.normalizeRequest(request)).pipe(
      tap((res) => this.persistUser(res))
    );
  }

  login(request: LoginRequest) {
    return this.http.post<AuthResponse>(`${this.api}/login`, {
      email: request.email.trim().toLowerCase(),
      password: request.password
    }).pipe(
      tap((res) => this.persistUser(res))
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.userSignal.set(null);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  private normalizeRequest(request: SignupRequest): SignupRequest {
    return {
      name: request.name.trim(),
      email: request.email.trim().toLowerCase(),
      password: request.password
    };
  }

  private persistUser(res: AuthResponse): void {
    const stored: StoredUser = {
      id: res.id,
      name: res.name,
      email: res.email,
      token: res.token
    };
    localStorage.setItem('token', res.token);
    localStorage.setItem('user', JSON.stringify(stored));
    this.userSignal.set(stored);
  }

  private loadUser(): StoredUser | null {
    const raw = localStorage.getItem('user');
    if (!raw) return null;
    try {
      return JSON.parse(raw) as StoredUser;
    } catch {
      return null;
    }
  }
}
