package ua.mainacademy.service;

import ua.mainacademy.dao.UserDAO;
import ua.mainacademy.model.User;

public class UserService {

    public static User getUserByLoginAndPassword(String login, String password) {
        return UserDAO.getUserByLoginAndPassword(login, password);
    }

    public static User create(User user) {
        return UserDAO.save(user);
    }
}
