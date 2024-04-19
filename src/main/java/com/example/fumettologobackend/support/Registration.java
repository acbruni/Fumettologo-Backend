package com.example.fumettologobackend.support;

import com.example.fumettologobackend.support.exceptions.KeycloackRegistrationException;
import com.example.fumettologobackend.user.User;
import lombok.experimental.UtilityClass;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class Registration {
    public void keycloackRegistration(RegistrationRequest registrationRequest) throws KeycloackRegistrationException {
        try {
            String usernameAdmin = "anna.c.bruni.23@gmail.com";
            String passwordAdmin = "23072001Mao!";
            String clientName = "springboot-keycloak";
            String role = "user";
            String serverUrl = "http://localhost:8080";
            String realm = "fumettologo";

            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientName)
                    .username(usernameAdmin)
                    .password(passwordAdmin)
                    .build();
            System.out.println('1');

            User utente = registrationRequest.getUser();
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(utente.getEmail());
            user.setEmail(utente.getEmail());
            user.setFirstName(utente.getFirstName());
            user.setLastName(utente.getLastName());
            user.setAttributes(Collections.singletonMap("origin", List.of("demo")));
            System.out.println('2');

            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();
            System.out.println('3');

            Response response = usersResource.create(user);
            System.out.println(response);
            System.out.printf("Response: %s %s%n", response.getStatus(), response.getStatusInfo());
            System.out.println(response.getLocation());
            String userId = CreatedResponseUtil.getCreatedId(response);
            System.out.printf("User created with userId: %s%n", userId);
            System.out.println('4');

            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(registrationRequest.getPassword());
            UserResource userResource = usersResource.get(userId);
            userResource.resetPassword(passwordCred);
            System.out.println('5');

            RoleRepresentation testerRealmRole = realmResource.roles().get(role).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(testerRealmRole));
            System.out.println('6');

        } catch (Exception e) {
            throw new KeycloackRegistrationException();
        }
    }
}