package social.repository.graph;

import social.entity.graph.node.Artist;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface AttractionRepository extends GraphRepository<Artist> {

    Artist findByName(String name);

}
