package br.com.nlw.events.repository;

import br.com.nlw.events.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Transactional(readOnly = true)
    Optional<Event> findByPrettyName(String prettyName);
}
