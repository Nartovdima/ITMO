package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;

import java.util.List;

public interface TalkRepository {
    void save(Talk talk);
    Talk find(long id);
    List<Talk> findMessages(User user);
}
