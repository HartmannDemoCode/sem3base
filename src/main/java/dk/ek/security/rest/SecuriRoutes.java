package dk.ek.security.rest;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SecuriRoutes {
    ISecurityController securityController = new SecurityController();
    public EndpointGroup getSecurityRoute = () -> {
        path("/auth",()-> {
//          before(securityController::authenticate);
//          get("/", personEntityController.getAll(), Role.ANYONE);
//            get("/", personEntityController.getAll());
//            get("/resetdata", personEntityController.resetData());
//            get("/{id}", personEntityController.getById());

            post("/login", securityController.login());
//            put("/{id}", personEntityController.update());
//            delete("/{id}", personEntityController.delete());
        });
    };
}
