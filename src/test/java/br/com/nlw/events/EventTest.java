package br.com.nlw.events;

import br.com.nlw.events.repository.EventRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EventTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    @Disabled
    public void createValidEvent() {

    }
}
