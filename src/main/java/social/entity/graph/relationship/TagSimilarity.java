package social.entity.graph.relationship;

import social.entity.graph.WeightedGraphRelationship;
import social.entity.graph.node.Tag;
import org.neo4j.ogm.annotation.RelationshipEntity;
import social.entity.graph.RelationshipType;

@RelationshipEntity(type = RelationshipType.SIMILAR_TO)
public class TagSimilarity extends WeightedGraphRelationship<Integer, Tag, Tag> {

}
