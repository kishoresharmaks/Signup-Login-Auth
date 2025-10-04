const api = {
  signup: async (name, email, password) => {
    const res = await fetch('/api/auth/signup', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ name, email, password })
    });
    return handleResponse(res);
  },
  login: async (email, password) => {
    const res = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ email, password })
    });
    return handleResponse(res);
  },
  me: async () => {
    const res = await fetch('/api/auth/me', {
      credentials: 'include'
    });
    return handleResponse(res);
  },
  logout: async () => {
    const res = await fetch('/api/auth/logout', {
      method: 'POST',
      credentials: 'include'
    });
    return handleResponse(res);
  }
};

async function handleResponse(res) {
  const text = await res.text();
  let data;
  try { data = text ? JSON.parse(text) : null; } catch { data = text; }
  if (!res.ok) {
    const message = typeof data === 'string' ? data : (data && data.message) || res.statusText;
    throw new Error(message);
  }
  return data;
}

function setupSignup() {
  const form = document.getElementById('signup-form');
  const status = document.getElementById('signup-status');
  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    status.textContent = 'Signing up...';
    const name = document.getElementById('signup-name').value.trim();
    const email = document.getElementById('signup-email').value.trim();
    const password = document.getElementById('signup-password').value;
    try {
      const res = await api.signup(name, email, password);
      status.textContent = `Created: ${res.name} (${res.email})`;
      form.reset();
    } catch (err) {
      status.textContent = `Error: ${err.message}`;
    }
  });
}

function setupLogin() {
  const form = document.getElementById('login-form');
  const status = document.getElementById('login-status');
  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    status.textContent = 'Logging in...';
    const email = document.getElementById('login-email').value.trim();
    const password = document.getElementById('login-password').value;
    try {
      const res = await api.login(email, password);
      status.textContent = `Hello, ${res.name}`;
    } catch (err) {
      status.textContent = `Error: ${err.message}`;
    }
  });
}

function setupSessionActions() {
  const meBtn = document.getElementById('me-btn');
  const logoutBtn = document.getElementById('logout-btn');
  const output = document.getElementById('me-output');

  meBtn.addEventListener('click', async () => {
    output.textContent = 'Loading...';
    try {
      const me = await api.me();
      output.textContent = JSON.stringify(me, null, 2);
    } catch (err) {
      output.textContent = `Error: ${err.message}`;
    }
  });

  logoutBtn.addEventListener('click', async () => {
    output.textContent = 'Logging out...';
    try {
      await api.logout();
      output.textContent = 'Logged out';
    } catch (err) {
      output.textContent = `Error: ${err.message}`;
    }
  });
}

document.addEventListener('DOMContentLoaded', () => {
  if (document.getElementById('signup-form')) {
    setupSignup();
  }
  if (document.getElementById('login-form')) {
    setupLogin();
  }
  if (document.getElementById('me-btn') || document.getElementById('me-output')) {
    setupSessionActions();
  }
});

// Redirect helpers
async function redirectAfter(ms, url) {
  return new Promise(resolve => setTimeout(() => { window.location.href = url; resolve(); }, ms));
}

// Extend login/signup flows with redirects if present on page
(function extendFlows() {
  const loginForm = document.getElementById('login-form');
  if (loginForm) {
    const original = loginForm.onsubmit;
  }
})();


