package dk.ek.security.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.bugelhartmann.TokenSecurity;
import dk.bugelhartmann.UserDTO;
import dk.ek.exceptions.ApiException;
import dk.ek.persistence.HibernateConfig;
import dk.ek.security.ISecurityDAO;
import dk.ek.security.SecurityDAO;
import dk.ek.security.User;
import dk.ek.utils.Utils;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.Set;
import java.util.stream.Collectors;

public class SecurityController implements ISecurityController {
    ISecurityDAO securityDAO = new SecurityDAO(HibernateConfig.getEntityManagerFactory());
    ObjectMapper objectMapper = new Utils().getObjectMapper();
    TokenSecurity tokenSecurity = new TokenSecurity();

    @Override
    public Handler login() {
        return (Context ctx)->{
            User user = ctx.bodyAsClass(User.class);
            User checkedUser = securityDAO.getVerifiedUser(user.getUsername(), user.getPassword());
            Set<String> roles = checkedUser
                    .getRoles()
                    .stream()
                    .map(role->role.getRolename())
                    .collect(Collectors.toSet());
            System.out.println(roles);
            UserDTO forToken = new UserDTO(checkedUser.getUsername(), roles);
            String token = createToken(forToken);

            ObjectNode on = objectMapper
                    .createObjectNode()
                    .put("token", token)
                    .put("username", forToken.getUsername());
            ctx.json(on).status(200);
        };
    }

    @Override
    public Handler register() {
        return null;
    }

    @Override
    public Handler authenticate() {
        return null;
    }

    @Override
    public boolean authorize(UserDTO userDTO, Set<String> allowedRoles) {
        return false;
    }

    private String createToken(UserDTO user) {
        try {
            String ISSUER;
            String TOKEN_EXPIRE_TIME;
            String SECRET_KEY;

            if (System.getenv("DEPLOYED") != null) {
                ISSUER = System.getenv("ISSUER");
                TOKEN_EXPIRE_TIME = System.getenv("TOKEN_EXPIRE_TIME");
                SECRET_KEY = System.getenv("SECRET_KEY");
            } else {
                ISSUER = Utils.getPropertyValue("ISSUER", "config.properties");
                TOKEN_EXPIRE_TIME = Utils.getPropertyValue("TOKEN_EXPIRE_TIME", "config.properties");
                SECRET_KEY = Utils.getPropertyValue("SECRET_KEY", "config.properties");
            }
            return tokenSecurity.createToken(user, ISSUER, TOKEN_EXPIRE_TIME, SECRET_KEY);
        } catch (Exception e) {
//            logger.error("Could not create token", e);
            throw new ApiException(500, "Could not create token");
        }
    }

}
