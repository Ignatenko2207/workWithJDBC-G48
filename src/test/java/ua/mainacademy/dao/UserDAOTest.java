package ua.mainacademy.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.mainacademy.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private static List<User> users;

    @BeforeAll
    public static void setData() {
        users = new ArrayList<>();
    }

    @AfterAll
    public static void deleteData() {
        users.forEach(user -> UserDAO.delete(user.getId()));
    }

    @Test
    void create() {
        User testUser = User.builder()
                .login("testLogin")
                .password("testPass")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        User savedUser = UserDAO.save(testUser);
        users.add(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(testUser.getLogin(), savedUser.getLogin());
    }

    @Test
    void update() {
        User testUser = User.builder()
                .login("testLogin")
                .password("testPass")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        User savedUser = UserDAO.save(testUser);
        User readyForUpdate = User.builder()
                .id(savedUser.getId())
                .login("updatedLogin")
                .password("testPass")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        User updatedUser = UserDAO.update(readyForUpdate);
        users.add(updatedUser);
        Optional<User> foundUser = UserDAO.findById(updatedUser.getId());
        if (foundUser.isPresent()) {
            assertNotEquals(savedUser.getLogin(), foundUser.get().getLogin());
            assertEquals(savedUser.getId(), foundUser.get().getId());
        } else {
            fail("Updated user was not found");
        }
    }

    @Test
    void findById() {
        User testUser = User.builder()
                .login("testLogin")
                .password("testPass")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        User savedUser = UserDAO.save(testUser);
        users.add(savedUser);
        Optional<User> foundUser = UserDAO.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
    }

    @Test
    void delete() {
        User testUser = User.builder()
                .login("testLogin")
                .password("testPass")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        User savedUser = UserDAO.save(testUser);
        Optional<User> foundUser = UserDAO.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        UserDAO.delete(savedUser.getId());
        foundUser = UserDAO.findById(savedUser.getId());
        assertFalse(foundUser.isPresent());
    }
}