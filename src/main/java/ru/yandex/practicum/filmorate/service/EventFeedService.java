package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.EventFeedDao;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventFeedService {
    private final EventFeedDao eventFeedDao;

    public List<Event> getAllEventFeed(Long id){
        return eventFeedDao.getAllEventFeed(id);
    }
}
