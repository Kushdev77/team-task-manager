export interface AuthResponse {
  id: number;
  name: string;
  email: string;
  token: string;
}

export interface SignupRequest {
  name: string;
  email: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface StoredUser {
  id: number;
  name: string;
  email: string;
  token: string;
}
