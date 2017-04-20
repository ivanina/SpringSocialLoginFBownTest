package social.entity.graph.node;

import social.entity.graph.GraphNode;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.Relationship;
import social.entity.graph.RelationshipType;

import java.util.Set;

@Getter
@Setter
public class Person extends GraphNode {

    private String name;

    @Relationship(type = RelationshipType.INTERESTED_IN)
    private Set<Tag> interestingTags;

    @Relationship(type = RelationshipType.INTERESTED_IN)
    private Set<Artist> interestingArtists;

}
