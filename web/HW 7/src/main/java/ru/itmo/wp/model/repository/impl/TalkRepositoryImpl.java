package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.TalkRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TalkRepositoryImpl extends AbstractRepositoryImpl implements TalkRepository {
    @Override
    public void save(Talk talk) {
        super.save(
                talk,
                saveQueryFabric("Talk", "sourceUserId", "targetUserId", "text", "creationTime"),
                talk.getSourceUserId(), talk.getTargetUserId(), talk.getText()
        );
    }

    @Override
    public Talk find(long id) {
        return super.find(id, "Talk", TalkRepositoryImpl::toTalk);
    }

    @Override
    public List<Talk> findMessages(User user) {
        List<Talk> messages = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Talk` WHERE `sourceUserId`=? OR `targetUserId`=? ORDER BY `creationTime` DESC")) {
                statement.setLong(1, user.getId());
                statement.setLong(2, user.getId());
                try (ResultSet resultSet = statement.executeQuery()) {
                    Talk talk;
                    while ((talk = toTalk(statement.getMetaData(), resultSet)) != null) {
                        messages.add(talk);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Event.", e);
        }

        return messages;
    }

    public static Talk toTalk(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Talk talk = new Talk();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    talk.setId(resultSet.getLong(i));
                    break;
                case "sourceUserId":
                    talk.setSourceUserId(resultSet.getLong(i));
                    break;
                case "targetUserId":
                    talk.setTargetUserId(resultSet.getLong(i));
                    break;
                case "text":
                    talk.setText(resultSet.getString(i));
                    break;
                case "creationTime":
                    talk.setCreationTime(resultSet.getTimestamp(i));
                    break;
                default:
                    // No operations.
            }
        }
        return talk;
    }
}
