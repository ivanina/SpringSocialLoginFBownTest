package social.entity.graph.relationship;

import social.entity.graph.WeightedGraphRelationship;
import social.entity.graph.node.Artist;
import org.neo4j.ogm.annotation.RelationshipEntity;
import social.entity.graph.RelationshipType;

@RelationshipEntity(type = RelationshipType.SIMILAR_TO)
public class ArtistSimilarity extends WeightedGraphRelationship<Integer, Artist, Artist> {

}
