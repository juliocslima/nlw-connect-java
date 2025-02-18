package br.com.nlw.events.controller;

import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.model.User;
import br.com.nlw.events.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping({"/subscription/{prettyName}", "/subscription/{prettyName}/{userId}"})
    public ResponseEntity<SubscriptionResponse> createSubscription(@PathVariable String prettyName,
                                                                   @RequestBody User subscriber,
                                                                   @PathVariable(required = false) Integer userId) {
        SubscriptionResponse result = subscriptionService.createSubscription(prettyName, subscriber, userId);
        return ResponseEntity.ok().body(result);
    }
}
