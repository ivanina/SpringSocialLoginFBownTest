package social.entity.graph;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeightedGraphRelationship<N extends Number, S extends GraphNode, E extends GraphNode>
        extends GraphRelationship<S, E> {

    private N weight;

}
