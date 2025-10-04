## Login & Signup (Session-based, no JWT)

This project implements minimal signup and login using HttpSession (server-side sessions). Passwords are hashed with BCrypt; no JWT is used.

### Stack
- Spring Boot 3
- Spring Web, Spring Data JPA
- MySQL (configurable)
- BCrypt via `spring-security-crypto`

### Key Files

**Backend:**
- `src/main/java/com/nexus/authentication/model/User.java` – JPA entity (`password` is hidden from JSON)
- `src/main/java/com/nexus/authentication/repository/UserRepository.java` – JPA repo with `findByEmail`, `existsByEmail`
- `src/main/java/com/nexus/authentication/service/UserService.java` – registration (hash) + authentication (verify)
- `src/main/java/com/nexus/authentication/controller/AuthController.java` – session-based `/signup`, `/login`, `/me`, `/logout`
- `src/main/java/com/nexus/authentication/controller/UserController.java` – basic CRUD endpoints

**Frontend:**
- `src/main/resources/static/index.html` – Signup form (default landing page)
- `src/main/resources/static/login.html` – Login form page
- `src/main/resources/static/profile.html` – User profile and logout page
- `src/main/resources/static/styles.css` – Modern dark theme styling

### Prerequisites
- JDK 21+
- Maven 3.9+
- MySQL (or update datasource to your DB)

### Configuration
Update `src/main/resources/application.properties` for your database, for example:

```
spring.datasource.url=jdbc:mysql://localhost:3306/login_demo
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Run the app:

```
mvn spring-boot:run
```

The server runs on `http://localhost:8081` by default.

### Frontend Pages

The application includes a modern web interface with 3 dedicated pages:

**1. Signup Page (`http://localhost:8081/` - Default landing page)**
- Clean signup form with name, email, and password fields
- Validates input and handles errors gracefully  
- Redirects to login page after successful registration
- Link to login page for existing users

**2. Login Page (`http://localhost:8081/login.html`)**
- Email and password login form
- Establishes session cookies for authenticated users
- Redirects to profile page after successful login
- Link to signup page for new users

**3. Profile Page (`http://localhost:8081/profile.html`)**
- Displays current user information via `/api/auth/me`
- "Load Profile" button to fetch user data
- Sign out functionality with session cleanup
- Protected route - requires authentication

**UI Features:**
- Modern dark theme with professional styling
- Responsive design that works on mobile and desktop
- Consistent navigation bar across all pages
- Smooth animations and hover effects
- Color-coded success/error messages
- Automatic form redirects and page transitions

**Navigation Flow:**
```
Signup → Login → Profile → Logout → Login
```

### How Authentication Works (no JWT)
1. User signs up or logs in.
2. On successful login, the server stores `AUTH_USER_ID` in the HttpSession.
3. The browser/client keeps the session cookie and sends it on subsequent requests.
4. `/api/auth/me` reads the session to identify the current user.
5. `/api/auth/logout` invalidates the session.

Password handling:
- On registration, the password is hashed with BCrypt.
- On login, the raw password is verified against the stored hash.

### API Endpoints

Base path: `http://localhost:8081`

- POST `/api/auth/signup`
  - Body: `{ "name": "Kishore", "email": "kishore@ks.com", "password": "secret" }`
  - 201 Created on success. Stores hashed password; does not log user in automatically.

- POST `/api/auth/login`
  - Body: `{ "email": "kishore@ks.com", "password": "secret" }`
  - 200 OK on success with user info; creates a server session (cookie-based).

- GET `/api/auth/me`
  - Returns current user if session is valid; else 401.

- POST `/api/auth/logout`
  - Invalidates the current session; returns 200.

User CRUD (optional, admin/dev use):
- GET `/api/user` – list users
- POST `/api/user/create` – create user (note: this bypasses hashing if you pass raw password; prefer `/api/auth/signup`)
- PUT `/api/user/update/{id}` – update user
- DELETE `/api/user/remove/{id}` – delete user

### Testing

**Option 1: Using the Web Interface (Recommended)**
1. Start the application: `mvn spring-boot:run`
2. Open browser to `http://localhost:8081`
3. Create an account using the signup form
4. Sign in with your credentials  
5. View your profile information
6. Sign out when done

**Option 2: Using curl (API testing)**

1) Signup
```
curl -i -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com","password":"secret"}' \
  http://localhost:8080/api/auth/signup
```

2) Login (save cookie)
```
curl -i -c cookies.txt -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"secret"}' \
  http://localhost:8080/api/auth/login
```

3) Me (send cookie)
```
curl -i -b cookies.txt http://localhost:8080/api/auth/me
```

4) Logout
```
curl -i -b cookies.txt -X POST http://localhost:8080/api/auth/logout
```

### Frontend Development

**Built-in Frontend**
The application includes a complete web interface built with vanilla HTML, CSS, and JavaScript. All pages are served from `/static/` and work seamlessly with the backend API.

**Frontend Technologies**
- **HTML5** - Semantic markup with proper forms and structure
- **CSS3** - Modern styling with CSS variables, flexbox, and smooth transitions
- **JavaScript (ES6+)** - Fetch API for HTTP requests with async/await
- **Responsive Design** - Mobile-first approach with flexible layouts

**Session Management**
- All frontend requests include `credentials: 'include'` to maintain session cookies
- Automatic session handling - no manual cookie management required
- Session persistence across browser tabs and page refreshes

**Frontend Features**
- **Form Validation** - HTML5 validation + custom error handling
- **Loading States** - Visual feedback during API calls
- **Error Handling** - User-friendly error messages with color coding
- **Auto-redirects** - Smooth navigation flow after successful actions
- **Responsive Navigation** - Consistent header across all pages

**Customization**
The frontend can be easily customized:
- Modify `styles.css` for different themes, colors, or layouts
- Update HTML templates for different form fields or layouts
- Enhance JavaScript functionality in individual page scripts
- Add new pages by creating additional HTML files in `/static/`

**External Frontend Integration**
If building a separate frontend (React, Vue, Angular), note:
- Enable CORS in Spring Boot configuration
- Always include `credentials: 'include'` or `withCredentials: true`
- Session cookies work automatically with same-origin requests
- API endpoints return plain text responses (not JSON objects)

### Security Notes
- BCrypt hashing is used; never store raw passwords.

### Common Issues
- 401 on `/me`: The session cookie wasn’t sent. Ensure the client preserves and sends cookies.
- Email conflict on signup: The email already exists in the database.
- Using `/api/user/create` creates a user without hashing. Prefer `/api/auth/signup`.


