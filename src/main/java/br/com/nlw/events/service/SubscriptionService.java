package br.com.nlw.events.service;

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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               UserRepository userRepository,
                               EventRepository eventRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public SubscriptionResponse createSubscription(String event, User user, Integer userId) {
        // Verifica se evento informado existe
        Event evt = eventRepository.findByPrettyName(event)
                .orElseThrow(() -> new EventNotFoundException("Evento " + event + " não encontrado"));

        // Se existe usuario na base, recupera registro, senão, cadastro
        // novo usuário
        Optional<User> userRec = userRepository.findByEmail(user.getEmail());

        if (userRec.isEmpty()) {
            user = userRepository.save(user);
        } else {
            user = userRec.get();
        }

        // Verifica se usuário já está inscrito neste evento
        Subscription check = subscriptionRepository.findByEventAndSubscriber(evt, user);

        if (check != null) {
            throw new SubscriptionConflictException("Usuário já inscrito neste evento");
        }

        User indicator = null;

        if (userId != null) {
            indicator = userRepository.findById(userId)
                    .orElseThrow(() -> new IndicatorNotFoundException("Usuário indicador não encontrado"));
        }

        Subscription subscription = new Subscription();

        subscription.setEvent(evt);
        subscription.setSubscriber(user);
        subscription.setIndication(indicator);

        subscriptionRepository.save(subscription);

        return new SubscriptionResponse(subscription.getSubscriptionNumber(), "http://codecraft.com/subscription/"+subscription.getEvent().getPrettyName()+"/"+subscription.getSubscriber().getId());

    }
}
