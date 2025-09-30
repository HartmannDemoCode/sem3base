package dk.ek.security;

import dk.ek.exceptions.ValidationException;
import dk.ek.persistence.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import javax.management.relation.Role;

public class SecurityDAO implements ISecurityDAO{
    EntityManagerFactory emf;

    public SecurityDAO(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public User getVerifiedUser(String username, String password) throws ValidationException {
        return null;
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
    public Role createRole(String role) {
        return null;
    }

    @Override
    public User addUserRole(String username, String role) {
        return null;
    }
    public static void main(String[] args) {
        ISecurityDAO dao = new SecurityDAO(HibernateConfig.getEntityManagerFactory());

        User user = dao.createUser("user1", "pass123");
        System.out.println(user.getUsername()+": "+user.getPassword());
    }
}
