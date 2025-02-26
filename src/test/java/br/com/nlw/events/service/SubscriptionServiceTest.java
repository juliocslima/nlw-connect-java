package br.com.nlw.events.service;

import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.model.Event;
import br.com.nlw.events.model.Subscription;
import br.com.nlw.events.model.User;
import br.com.nlw.events.repository.EventRepository;
import br.com.nlw.events.repository.SubscriptionRepository;
import br.com.nlw.events.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    private SubscriptionService underTest;

    @BeforeEach
    void setup() {
        underTest = new SubscriptionService(subscriptionRepository, userRepository, eventRepository);
    }

    @Test
    void itShouldCreateSubscriptionWhenSubscriptionNotExistsAndHasNoIndication() {
        // Given
        // Criar evento de teste
        Event testEvent = new Event();
        testEvent.setEventId(1);
        testEvent.setTitle("Evento Teste");
        testEvent.setPrettyName("evento-teste");
        testEvent.setLocation("São Paulo");
        testEvent.setPrice(100.0);

        // Criar usuário de teste
        User testUser = new User();
        testUser.setId(1);
        testUser.setName("João Silva");
        testUser.setEmail("joao@email.com");

        Subscription subscription = new Subscription();
        subscription.setSubscriptionNumber(1);
        subscription.setEvent(testEvent);
        subscription.setSubscriber(testUser);

        given(eventRepository.findByPrettyName(testEvent.getPrettyName()))
                .willReturn(Optional.of(testEvent));

        given(userRepository.findByEmail(testUser.getEmail()))
                .willReturn(Optional.of(testUser));

        given(subscriptionRepository.findByEventAndSubscriber(testEvent, testUser))
                .willReturn(null);

        given(subscriptionRepository.save(any(Subscription.class))).willReturn(subscription);

        // When
        SubscriptionResponse createdSubscription = underTest.createSubscription(
                testEvent.getPrettyName(),
                testUser,
                null
        );

        // Then
        assertThat(createdSubscription).isNotNull();
        assertThat(testEvent.getEventId()).isEqualTo(1);
        assertThat(testEvent.getPrettyName()).isEqualTo(testEvent.getPrettyName());
    }

    @Test
    void itShouldCreateSubscriptionWhenSubscriptionNotExistsAndHasIndication() {
        // Given
        // Criar evento de teste
        Event testEvent = new Event();
        testEvent.setEventId(1);
        testEvent.setTitle("Evento Teste");
        testEvent.setPrettyName("evento-teste");
        testEvent.setLocation("São Paulo");
        testEvent.setPrice(100.0);

        // Criar usuário de teste
        User testUser = new User();
        testUser.setId(1);
        testUser.setName("João Silva");
        testUser.setEmail("joao@email.com");

        // Criar usuário de indicação
        User indicationUser = new User();
        indicationUser.setId(2);
        indicationUser.setName("Maria Souza");
        indicationUser.setEmail("maria@email.com");

        Subscription subscription = new Subscription();
        subscription.setSubscriptionNumber(1);
        subscription.setEvent(testEvent);
        subscription.setSubscriber(testUser);

        given(eventRepository.findByPrettyName(testEvent.getPrettyName()))
                .willReturn(Optional.of(testEvent));

        given(userRepository.findByEmail(testUser.getEmail()))
                .willReturn(Optional.of(testUser));

        given(subscriptionRepository.findByEventAndSubscriber(testEvent, testUser))
                .willReturn(null);

        given(userRepository.findById(indicationUser.getId()))
                .willReturn(Optional.of(indicationUser));

        given(subscriptionRepository.save(any(Subscription.class))).willReturn(subscription);

        // When
        SubscriptionResponse createdSubscription = underTest.createSubscription(
                testEvent.getPrettyName(),
                testUser,
                indicationUser.getId()
        );

        // Then
        assertThat(createdSubscription).isNotNull();
        assertThat(testEvent.getEventId()).isEqualTo(1);
        assertThat(testEvent.getPrettyName()).isEqualTo(testEvent.getPrettyName());
    }

    @Test
    void getCompleteRanking() {
    }

    @Test
    void getRankingByUser() {
    }
}