package com.yaroslavyankov.frontend.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.yaroslavyankov.frontend.dto.auth.JwtRequest;
import com.yaroslavyankov.frontend.dto.auth.JwtResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Route("login")
@PageTitle("Login | Investflow")
public class LoginView extends VerticalLayout {

    private final RestTemplate restTemplate;

    public LoginView(RestTemplate restTemplate) {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);


        this.restTemplate = restTemplate;
        H1 appName = new H1("Investflow");
        H2 loginHeader = new H2("Login");
        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        Button loginButton = new Button("Login", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            JwtRequest loginRequest = new JwtRequest(username, password);

            try {
                JwtResponse response
                        = restTemplate.postForObject("http://localhost:9099/api/v1/auth/login", loginRequest, JwtResponse.class);
                if (response != null) {
                    VaadinSession.getCurrent().setAttribute("userId", response.getId());
                    VaadinSession.getCurrent().setAttribute("username", response.getUsername());
                    VaadinSession.getCurrent().setAttribute("accessToken", response.getAccessToken());
                    VaadinSession.getCurrent().setAttribute("refreshToken", response.getRefreshToken());
                }

                UI.getCurrent().navigate("/");
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                    Notification.show("Некорреектный username или пароль", 3000, Notification.Position.MIDDLE);
                }
            }

        });

        add(appName, loginHeader, usernameField, passwordField, loginButton);
    }
}
