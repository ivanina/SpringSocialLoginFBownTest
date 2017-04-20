package social.entity.graph;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.StartNode;

@Getter
@Setter
public class GraphRelationship<S extends GraphNode, E extends GraphNode> extends GraphEntity {

    @StartNode
    protected S start;
    @EndNode
    protected E end;

}
