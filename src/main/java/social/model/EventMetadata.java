package social.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.util.Pair;

import java.util.Collections;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventMetadata {

    private Set<Pair<String, KeywordMetadata>> keywords = Collections.emptySet();
    private Double rank = 0.0;

}
