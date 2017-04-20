package social.entity.graph.node;

import social.entity.graph.GraphNode;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.Relationship;
import social.entity.graph.RelationshipType;
import social.entity.graph.relationship.ArtistSimilarity;

import java.util.Set;

@Getter
@Setter
public class Artist extends GraphNode {

    private String name;

    @Relationship(type = RelationshipType.HAS_A)
    private Set<Tag> tags;

    @Relationship(type = RelationshipType.SIMILAR_TO, direction = Relationship.UNDIRECTED)
    private Set<ArtistSimilarity> similarArtists;

}
