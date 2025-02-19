package br.com.nlw.events.controller;

import br.com.nlw.events.dto.SubscriptionRankingItem;
import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.model.User;
import br.com.nlw.events.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/subscription/{prettyName}/ranking")
    public ResponseEntity<List<SubscriptionRankingItem>> getSubscriptionRanking(@PathVariable String prettyName) {
        List<SubscriptionRankingItem> ranking = subscriptionService.getCompleteRanking(prettyName);

        return ResponseEntity.ok().body(ranking.subList(0, 3));
    }

    @GetMapping("/subscription/{prettyName}/ranking/{userId}")
    public ResponseEntity<?> generateRaningByEventAndUser(@PathVariable String prettyName, @PathVariable Integer userId){
        return ResponseEntity.ok(subscriptionService.getRankingByUser(prettyName, userId));
    }
}
