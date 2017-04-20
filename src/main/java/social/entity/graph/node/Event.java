package social.entity.graph.node;

import social.entity.graph.GraphNode;
import social.entity.graph.RelationshipType;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@Getter
@Setter
public class Event extends GraphNode {

    private String name;

    @Relationship(type = RelationshipType.HAS_A)
    private Set<Artist> artists;

    @Relationship(type = RelationshipType.HAS_A)
    private Set<Tag> tags;

}
