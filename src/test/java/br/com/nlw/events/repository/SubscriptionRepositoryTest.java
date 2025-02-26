package br.com.nlw.events.repository;

import br.com.nlw.events.dto.SubscriptionRankingItem;
import br.com.nlw.events.model.Event;
import br.com.nlw.events.model.Subscription;
import br.com.nlw.events.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class SubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    private Event testEvent;
    private User testUser;
    private User indicationUser;

    @BeforeEach
    void setup() {
        // Criar evento de teste
        testEvent = new Event();
        testEvent.setTitle("Evento Teste");
        testEvent.setPrettyName("evento-teste");
        testEvent.setLocation("São Paulo");
        testEvent.setPrice(100.0);
        testEvent = eventRepository.save(testEvent);

        // Criar usuário de teste
        testUser = new User();
        testUser.setName("João Silva");
        testUser.setEmail("joao@email.com");
        testUser = userRepository.save(testUser);

        // Criar usuário de indicação
        indicationUser = new User();
        indicationUser.setName("Maria Souza");
        indicationUser.setEmail("maria@email.com");
        indicationUser = userRepository.save(indicationUser);
    }

    @Test
    void itShouldFindByEventAndSubscriber() {
        Subscription subscription = new Subscription();
        subscription.setEvent(testEvent);
        subscription.setSubscriber(testUser);
        subscription.setIndication(indicationUser);
        subscriptionRepository.save(subscription);

        Subscription foundSubscription = subscriptionRepository.findByEventAndSubscriber(testEvent, testUser);
        assertNotNull(foundSubscription);
        assertEquals(testEvent.getEventId(), foundSubscription.getEvent().getEventId());
        assertEquals(testUser.getId(), foundSubscription.getSubscriber().getId());
    }

    @Test
    void itShouldGetRankingByUserOrdered() {
        Subscription subscription1 = new Subscription();
        subscription1.setEvent(testEvent);
        subscription1.setSubscriber(testUser);
        subscription1.setIndication(indicationUser);
        subscriptionRepository.save(subscription1);

        List<SubscriptionRankingItem> ranking = subscriptionRepository.getSubscriptionRankingItems(testEvent.getEventId());
        assertFalse(ranking.isEmpty());
        assertEquals(indicationUser.getId(), ranking.get(0).userId());
    }
}