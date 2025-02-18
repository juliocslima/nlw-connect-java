package br.com.nlw.events.service;

import br.com.nlw.events.model.Event;
import br.com.nlw.events.repository.EventRepository;
import br.com.nlw.events.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event addNewEvent(Event event) {
        // Generate pretty name
        event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event findByPrettyName(String prettyName) {
        return eventRepository.findByPrettyName(prettyName)
                .orElseThrow(() -> new ObjectNotFoundException("Evento não encontrado"));
    }
}
