package dk.ek.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.ek.exceptions.ValidationException;
import dk.ek.persistence.HibernateConfig;
import dk.ek.utils.Utils;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class SecurityController {
    ISecurityDAO securityDAO = new SecurityDAO(HibernateConfig.getEntityManagerFactory());
    ObjectMapper objectMapper = new Utils().getObjectMapper();
    public Handler login(){
        return (Context ctx) -> {
            User user = ctx.bodyAsClass(User.class);
            try {
                User verified = securityDAO.getVerifiedUser(user.getUsername(), user.getPassword());
                ObjectNode on = objectMapper
                        .createObjectNode()
                        .put("msg","Succesfull login for user: "+verified.getUsername());
                ctx.json(on).status(200);
            } catch(ValidationException ex){
                ObjectNode on = objectMapper.createObjectNode().put("msg","login failed. Wrong username or password");
                ctx.json(on).status(401);
            }
        };
    }
}
