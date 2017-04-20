package social.repository.graph;

import social.entity.graph.node.Event;
import social.entity.graph.node.Person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.Set;

public interface EventRepository extends GraphRepository<Event> {

    @Query("")
    Set<Event> getEventsForUser(Person person);

}
