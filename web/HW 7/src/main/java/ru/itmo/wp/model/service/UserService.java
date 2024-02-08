package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserService {
    private static final String PASSWORD_SALT = "1174f9d7bc21e00e9a5fd0a783a44c9a9f73413c";

    private final UserRepository userRepository = new UserRepositoryImpl();

    public void validateRegistration(User user, String password, String passwordConfirmation) throws ValidationException {
        validateLogin(user);
        validateEmail(user);
        validatePassword(password, passwordConfirmation);
    }


    private void validateLogin(User user) throws ValidationException {
        if (Strings.isNullOrEmpty(user.getLogin())) {
            throw new ValidationException("Login is required");
        }
        if (!user.getLogin().matches("[a-z]+")) {
            throw new ValidationException("Login can contain only lowercase Latin letters");
        }
        if (user.getLogin().length() > 8) {
            throw new ValidationException("Login can't be longer than 8 letters");
        }
        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new ValidationException("Login is already in use");
        }
    }

    private void validateEmail(User user) throws ValidationException {
        if (Strings.isNullOrEmpty(user.getEmail())) {
            throw new ValidationException("Email is required");
        }
        if (user.getEmail().length() > 50) {
            throw new ValidationException("Email can't be longer than 50 letters");
        }
        if (user.getEmail().chars().filter(x -> x == '@').count() != 1) {
            throw new ValidationException("Invalid email");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ValidationException("Email is already in use");
        }
    }

    private void validatePassword(String password, String passwordConfirmation) throws ValidationException {
        if (Strings.isNullOrEmpty(password)) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < 4) {
            throw new ValidationException("Password can't be shorter than 4 characters");
        }
        if (password.length() > 64) {
            throw new ValidationException("Password can't be longer than 64 characters");
        }

        if (Strings.isNullOrEmpty(passwordConfirmation)) {
            throw new ValidationException("Password confirmation is required");
        }

        if (!passwordConfirmation.equals(password)) {
            throw new ValidationException("Password and password confirmation must be equal");
        }
    }

    private void validateIsAdmin(User user) throws ValidationException {

        if (user == null || userRepository.find(user.getId()) == null || !userRepository.find(user.getId()).isAdminStatus()) {
            throw new ValidationException("You need admin status for this action");
        }
    }

    public void register(User user, String password) {
        userRepository.save(user, getPasswordSha(password));
    }

    private String getPasswordSha(String password) {
        return Hashing.sha256().hashBytes((PASSWORD_SALT + password).getBytes(StandardCharsets.UTF_8)).toString();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public long findCount() {
        return userRepository.findCount();
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void validateEnter(String login, String password) throws ValidationException {
        User user = userRepository.findByLoginOrEmailAndPasswordSha(login, getPasswordSha(password));
        if (user == null) {
            throw new ValidationException("Invalid login / email or password");
        }
    }

    public User findByLoginOrEmailAndPassword(String loginOrEmail, String password) {
        return userRepository.findByLoginOrEmailAndPasswordSha(loginOrEmail, getPasswordSha(password));
    }

    public Object find(long userId) {
        return userRepository.find(userId);
    }

    public User validateAndFindByLoginAndPassword(String login, String password) throws ValidationException {
        validateEnter(login, password);
        return findByLoginOrEmailAndPassword(login, password);
    }

    public void changeAdminStatus(User user, long operatedUserId, boolean operatedUserStatus) throws ValidationException{
        validateIsAdmin(user);
        userRepository.changeAdminStatus(operatedUserId, !operatedUserStatus);
    }
}
