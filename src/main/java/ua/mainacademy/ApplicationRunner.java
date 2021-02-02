package ua.mainacademy;

import ua.mainacademy.model.User;
import ua.mainacademy.service.UserService;

import java.util.logging.Logger;

public class ApplicationRunner {
    private static final Logger LOG = Logger.getLogger(ApplicationRunner.class.getName());

    public static void main(String[] args) {
        String login = "alex2207";
        String password = "159159";

        User user = UserService.create(User.builder()
                .login(login)
                .password(password)
                .firstName("Alex")
                .lastName("Ignatenko")
                .build()
        );

        LOG.info(user.toString());
    }
}
