/**
 * Injects API_URL into environment.prod.ts before production build (Vercel / CI).
 * Set API_URL in Vercel → e.g. https://your-app.onrender.com/api
 */
const fs = require('fs');
const path = require('path');

const raw = process.env.API_URL || process.env.NG_APP_API_URL || 'http://localhost:8080/api';
const apiUrl = raw.endsWith('/api') ? raw : `${raw.replace(/\/$/, '')}/api`;

const content = `export const environment = {
  production: true,
  apiUrl: '${apiUrl}'
};
`;

const target = path.join(__dirname, '..', 'src', 'environments', 'environment.prod.ts');
fs.writeFileSync(target, content, 'utf8');
console.log('[set-env] apiUrl =', apiUrl);
