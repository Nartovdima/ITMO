package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SqlNoDataSourceInspection")
public class UserRepositoryImpl extends AbstractRepositoryImpl implements UserRepository {
    private List<User> AbstractFinder(String SQLQuery, Object... SQLParams) {
        List<User> users = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQLQuery)) {
                for (int i = 0; i < SQLParams.length; i++) {
                    statement.setObject(i + 1, SQLParams[i]);
                }
                try (ResultSet resultSet = statement.executeQuery()) {
                    User user;
                    while ((user = toUser(statement.getMetaData(), resultSet)) != null) {
                        users.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find User.", e);
        }
        return users;
    }

    @Override
    public User find(long id) {
        return super.find(id, "User", UserRepositoryImpl::toUser);
    }

    @Override
    public User findByLogin(String login) {
        List <User> queryResult = AbstractFinder("SELECT * FROM User WHERE login=?", login);
       if (!queryResult.isEmpty()) {
            return queryResult.get(0);
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        List <User> queryResult =  AbstractFinder("SELECT * FROM User WHERE email=?", email);
        if (!queryResult.isEmpty()) {
            return queryResult.get(0);
        }
        return null;
    }

    @Override
    public User findByLoginOrEmailAndPasswordSha(String loginOrEmail, String passwordSha) {
        List <User> queryResult = AbstractFinder(
                "SELECT * FROM User WHERE (login=? OR email = ?) AND passwordSha=?",
                loginOrEmail,
                loginOrEmail,
                passwordSha
        );
        if (!queryResult.isEmpty()) {
            return queryResult.get(0);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return AbstractFinder("SELECT * FROM User ORDER BY id DESC");
    }

    public static User toUser(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        User user = new User();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    user.setId(resultSet.getLong(i));
                    break;
                case "login":
                    user.setLogin(resultSet.getString(i));
                    break;
                case "email":
                    user.setEmail(resultSet.getString(i));
                    break;
                case "creationTime":
                    user.setCreationTime(resultSet.getTimestamp(i));
                    break;
                case "adminStatus":
                    user.setAdminStatus(resultSet.getBoolean(i));
                    break;
                default:
                    // No operations.
            }
        }
        return user;
    }

    @Override
    public void save(User user, String passwordSha) {
        super.save(
                user,
                saveQueryFabric("User", "login", "email", "passwordSha", "creationTime"),
                user.getLogin(), user.getEmail(), passwordSha
        );
    }

    @Override
    public long findCount() {
        long usersNumber = 0;
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM `User`")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return 0;
                    }
                    usersNumber = resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Users.", e);
        }
        return usersNumber;
    }

    @Override
    public void changeAdminStatus(long id, boolean status) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE `User` SET adminStatus=? WHERE id=? ")) {
                statement.setBoolean(1, status);
                statement.setLong(2, id);
                statement.executeQuery();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Articles.", e);
        }
    }
}
