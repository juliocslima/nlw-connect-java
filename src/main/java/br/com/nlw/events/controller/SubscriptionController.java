package br.com.nlw.events.controller;

import br.com.nlw.events.dto.SubscriptionRankingItem;
import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.model.User;
import br.com.nlw.events.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Tag(name = "Subscriptions", description = "API NLW Connect - Subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Operation(summary = "Subscribe to event", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event registration created successfully"),
            @ApiResponse(responseCode = "404", description = "Event pretty name or user ID not found"),
            @ApiResponse(responseCode = "422", description = "Invalid parameters request"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Service failure"),
    })
    @PostMapping({"/subscription/{prettyName}", "/subscription/{prettyName}/{userId}"})
    public ResponseEntity<SubscriptionResponse> createSubscription(@PathVariable String prettyName,
                                                                   @RequestBody User subscriber,
                                                                   @PathVariable(required = false) Integer userId) {
        SubscriptionResponse result = subscriptionService.createSubscription(prettyName, subscriber, userId);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "Get ranking", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search successful"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "422", description = "Invalid parameters request"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Service failure"),
    })
    @GetMapping("/subscription/{prettyName}/ranking")
    public ResponseEntity<List<SubscriptionRankingItem>> getSubscriptionRanking(@PathVariable String prettyName) {
        List<SubscriptionRankingItem> ranking = subscriptionService.getCompleteRanking(prettyName);

        return ResponseEntity.ok().body(ranking.subList(0, 3));
    }

    @Operation(summary = "Get subscriber ranking position", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search successful"),
            @ApiResponse(responseCode = "404", description = "Event or user ID not found"),
            @ApiResponse(responseCode = "422", description = "Invalid parameters request"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Service failure"),
    })
    @GetMapping("/subscription/{prettyName}/ranking/{userId}")
    public ResponseEntity<?> generateRankingByEventAndUser(@PathVariable String prettyName, @PathVariable Integer userId){
        return ResponseEntity.ok(subscriptionService.getRankingByUser(prettyName, userId));
    }
}
