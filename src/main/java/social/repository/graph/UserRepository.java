package social.repository.graph;

import social.entity.graph.node.Person;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface UserRepository extends GraphRepository<Person> {

}
