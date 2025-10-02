# Security steps
## Password hashing
- Create User entity: username, password
  - Create constructor that hashes password
  - Create method: verifyPassword(String password)
  - Create Role @ManyToMany with User
  - User::addRole, User::removeRole
  - SecurityDAO implements ISecurityDAO (from toolbox->security->API Security)
## Login
- SecurityController implements ISecurityController (void login(Context ctx); void register(Context ctx); void authenticate(Context ctx); void authorize(Context ctx);
  - login() uses securityDAO.getVerifiedUser (Created in class tuesday) and [createToken](https://gist.github.com/Thomas-Hartmann/9e976318d148bb4699ddc73739d7429a)
  - register() creates a new user with the securityDAO and returns token
  - [authenticate](https://gist.github.com/Thomas-Hartmann/87f5c32075b3514b591d970cf6f76c49)
  - [authorize](https://gist.github.com/Thomas-Hartmann/a08c0e6ef3e543c27d003cc26e560d1c)
  - [Security routes](https://gist.github.com/Thomas-Hartmann/9dd18a585f2f103ed064cfe4b9c058aa)
  - 