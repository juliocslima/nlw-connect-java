package br.com.nlw.events.repository;

import br.com.nlw.events.dto.SubscriptionRankingItem;
import br.com.nlw.events.model.Event;
import br.com.nlw.events.model.Subscription;
import br.com.nlw.events.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Subscription findByEventAndSubscriber(Event event, User user);

    @Query(value = "select count(sub.subscription_number) as quantidade, sub.indication_user_id, usr.user_name " +
            "from tbl_subscription sub inner join " +
            "tbl_user usr on (sub.indication_user_id = usr.user_id) " +
            "where sub.indication_user_id is not null and event_id = :eventId " +
            "group by sub.indication_user_id " +
            "order by quantidade desc", nativeQuery = true)
    List<SubscriptionRankingItem> getSubscriptionRankingItems(@Param("eventId") Integer eventId);
}
