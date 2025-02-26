package br.com.nlw.events.service;

import br.com.nlw.events.dto.SubscriptionRankingByUser;
import br.com.nlw.events.dto.SubscriptionRankingItem;
import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.model.Event;
import br.com.nlw.events.model.Subscription;
import br.com.nlw.events.model.User;
import br.com.nlw.events.repository.EventRepository;
import br.com.nlw.events.repository.SubscriptionRepository;
import br.com.nlw.events.repository.UserRepository;
import br.com.nlw.events.service.exceptions.EventNotFoundException;
import br.com.nlw.events.service.exceptions.IndicatorNotFoundException;
import br.com.nlw.events.service.exceptions.SubscriptionConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
        assertThat(createdSubscription.subscriptionNumber()).isEqualTo(1);
        assertThat(createdSubscription.designation()).contains("http://codecraft.com/subscription/");

        verify(subscriptionRepository).save(any(Subscription.class));
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
        assertThat(createdSubscription.subscriptionNumber()).isEqualTo(1);
        assertThat(createdSubscription.designation()).contains("http://codecraft.com/subscription/");

        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    void itShouldThrowExceptionWhenIndicatorNotExists() {
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

        given(eventRepository.findByPrettyName(testEvent.getPrettyName()))
                .willReturn(Optional.of(testEvent));

        given(userRepository.findByEmail(testUser.getEmail()))
                .willReturn(Optional.of(testUser));

        given(subscriptionRepository.findByEventAndSubscriber(testEvent, testUser))
                .willReturn(null);

        given(userRepository.findById(indicationUser.getId()))
                .willReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> underTest.createSubscription(
                testEvent.getPrettyName(),
                testUser,
                indicationUser.getId()))
                .isInstanceOf(IndicatorNotFoundException.class)
                .hasMessageContaining("Usuário indicador não encontrado");

        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    void itShouldCreateSubscriptionWhenUserNotExists() {
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
                .willReturn(Optional.empty());

        given(userRepository.save(any(User.class)))
                .willReturn(testUser);

        given(subscriptionRepository.findByEventAndSubscriber(testEvent, testUser))
                .willReturn(null);

        given(subscriptionRepository.save(any(Subscription.class)))
                .willReturn(subscription);

        // When
        SubscriptionResponse createdSubscription = underTest.createSubscription(
                testEvent.getPrettyName(),
                testUser,
                null
        );

        // Then
        assertThat(createdSubscription).isNotNull();
        assertThat(createdSubscription.subscriptionNumber()).isEqualTo(1);
        assertThat(createdSubscription.designation())
                .contains("http://codecraft.com/subscription/"+subscription.getEvent().getPrettyName()+"/"+subscription.getSubscriber().getId());

        verify(userRepository).save(any(User.class));
        verify(subscriptionRepository).save(any(Subscription.class));

    }

    @Test
    void itShouldThrowExceptionWhenSubscriptionExists() {
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
                .willReturn(subscription);

        assertThatThrownBy(() -> underTest.createSubscription(testEvent.getPrettyName(), testUser, null))
                .isInstanceOf(SubscriptionConflictException.class)
                .hasMessageContaining("Usuário já inscrito neste evento");

        verify(userRepository, never()).save(any(User.class));
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    void itShouldReturnCompleteRankingWhenEventExists() {
        // Given
        // Criar evento de teste
        Event testEvent = new Event();
        testEvent.setEventId(1);
        testEvent.setTitle("Evento Teste");
        testEvent.setPrettyName("evento-teste");
        testEvent.setLocation("São Paulo");
        testEvent.setPrice(100.0);

        SubscriptionRankingItem item1 = new SubscriptionRankingItem(50L, 1, "João Silva");
        SubscriptionRankingItem item2 = new SubscriptionRankingItem(10L, 2, "José Fulano");

        given(eventRepository.findByPrettyName(testEvent.getPrettyName()))
                .willReturn(Optional.of(testEvent));

        given(subscriptionRepository.getSubscriptionRankingItems(testEvent.getEventId()))
                .willReturn(List.of(item1, item2));

        // When
        List<SubscriptionRankingItem> ranking = underTest.getCompleteRanking(
                testEvent.getPrettyName()
        );

        // Then
        assertThat(ranking).isNotNull();
        assertThat(ranking.size()).isEqualTo(2);
    }

    @Test
    void itShouldThrowExceptionWhenEventDoesNotExist() {
        // Given
        String eventPrettyName = "evento-sao-paulo-teste";

        given(eventRepository.findByPrettyName(anyString()))
                .willReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> underTest.getCompleteRanking(eventPrettyName))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessageContaining("Evento " + eventPrettyName + " não encontrado");

        verify(subscriptionRepository, never()).getSubscriptionRankingItems(anyInt());
    }

    @Test
    void itShouldReturnRankingByUserWhenUserExistsInEventRanking() {
        // Given

        int userId = 1;

        // Criar evento de teste
        Event testEvent = new Event();
        testEvent.setEventId(1);
        testEvent.setTitle("Evento Teste");
        testEvent.setPrettyName("evento-teste");
        testEvent.setLocation("São Paulo");
        testEvent.setPrice(100.0);

        SubscriptionRankingItem item1 = new SubscriptionRankingItem(50L, 1, "João Silva");
        SubscriptionRankingItem item2 = new SubscriptionRankingItem(10L, 2, "José Fulano");

        given(eventRepository.findByPrettyName(testEvent.getPrettyName()))
                .willReturn(Optional.of(testEvent));

        given(subscriptionRepository.getSubscriptionRankingItems(testEvent.getEventId()))
                .willReturn(List.of(item1, item2));

        // When
        SubscriptionRankingByUser rank = underTest.getRankingByUser(testEvent.getPrettyName(), userId);

        // Then
        assertThat(rank).isNotNull();
        assertThat(rank.rankingPosition()).isEqualTo(1);
    }

    @Test
    void itShouldReturnRankingByUserWhenUserNotExistsInEventRanking() {
        // Given

        int userId = 3;

        // Criar evento de teste
        Event testEvent = new Event();
        testEvent.setEventId(1);
        testEvent.setTitle("Evento Teste");
        testEvent.setPrettyName("evento-teste");
        testEvent.setLocation("São Paulo");
        testEvent.setPrice(100.0);

        SubscriptionRankingItem item1 = new SubscriptionRankingItem(50L, 1, "João Silva");
        SubscriptionRankingItem item2 = new SubscriptionRankingItem(10L, 2, "José Fulano");

        given(eventRepository.findByPrettyName(testEvent.getPrettyName()))
                .willReturn(Optional.of(testEvent));

        given(subscriptionRepository.getSubscriptionRankingItems(testEvent.getEventId()))
                .willReturn(List.of(item1, item2));

        // When
        assertThatThrownBy(() -> underTest.getRankingByUser(testEvent.getPrettyName(), userId))
                .isInstanceOf(IndicatorNotFoundException.class)
                .hasMessageContaining("Não há indicações para o inscrito: " + userId + " neste evento");
    }
}