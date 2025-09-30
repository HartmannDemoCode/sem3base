package dk.ek.security;

import dk.ek.exceptions.ValidationException;
import dk.ek.persistence.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;


public class SecurityDAO implements ISecurityDAO{
    EntityManagerFactory emf;

    public SecurityDAO(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public User getVerifiedUser(String username, String password) throws ValidationException {
        try(EntityManager em = emf.createEntityManager()){
            User foundUser = em.find(User.class, username);

            if(foundUser != null && foundUser.verifyPassword(password)){
                return foundUser;
            } else {
                throw new ValidationException("User or Password was incorrect");
            }
        }
    }

    @Override
    public User createUser(String username, String password) {
        try(EntityManager em = emf.createEntityManager()){
            User user = new User(username, password);
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        }
    }

    @Override
    public Role createRole(String roleName) {
        Role role = new Role(roleName);
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(role);
            em.getTransaction().commit();
            return role;
        }
    }

    @Override
    public User addUserRole(String username, String role) {

    }
    public static void main(String[] args) {
        ISecurityDAO dao = new SecurityDAO(HibernateConfig.getEntityManagerFactory());

//        User user = dao.createUser("user1", "pass123");
//        System.out.println(user.getUsername()+": "+user.getPassword());

        try {
            User validatedUser = dao.getVerifiedUser("user1", "pass123");
            System.out.println("User was validated: "+validatedUser.getUsername());
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
