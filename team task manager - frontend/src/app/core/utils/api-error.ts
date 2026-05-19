import { HttpErrorResponse } from '@angular/common/http';

export function extractApiError(error: unknown, fallback: string): string {
  if (!(error instanceof HttpErrorResponse)) {
    return fallback;
  }

  if (error.status === 0) {
    return 'Cannot reach the server. Start the backend on port 8080, then run: ng serve';
  }

  const body = error.error;

  if (typeof body === 'string') {
    try {
      const parsed = JSON.parse(body) as { error?: string; fields?: Record<string, string> };
      if (parsed.error) return parsed.error;
      if (parsed.fields) return Object.values(parsed.fields).join(', ');
    } catch {
      return body || fallback;
    }
  }

  if (body && typeof body === 'object') {
    const record = body as { error?: string; fields?: Record<string, string> };
    if (record.error) return record.error;
    if (record.fields) return Object.values(record.fields).join(', ');
  }

  if (error.status === 401) return 'Invalid email or password';
  if (error.status === 403) return 'You do not have permission for this action';
  if (error.status === 409) return 'Email is already registered';

  return fallback;
}
