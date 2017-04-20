package social.entity.graph.node;

import social.entity.graph.GraphNode;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.Relationship;
import social.entity.graph.RelationshipType;
import social.entity.graph.relationship.TagSimilarity;

import java.util.Set;

@Getter
@Setter
public class Tag extends GraphNode {

    private String name;

    @Relationship(type = RelationshipType.SIMILAR_TO, direction = Relationship.UNDIRECTED)
    private Set<TagSimilarity> similarTags;

}
