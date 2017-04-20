package social.repository.graph;

import social.entity.graph.node.Tag;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface GenreRepository extends GraphRepository<Tag> {

    Tag findByName(String name);
}
