package social.service.keyword;

import social.model.KeywordMetadata;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class KeywordCollector {

    protected abstract Collection<Pair<String, KeywordMetadata>> collectKeywords();

    public final Map<String, KeywordMetadata> collect() {
        return collectKeywords().stream()
                .filter(pair -> !StringUtils.isEmpty(pair.getKey()))
                .collect(Collectors.groupingBy(
                        Pair::getKey,
                        HashMap::new,
                        Collectors.mapping(
                                Pair::getValue,
                                Collectors.reducing(
                                        new KeywordMetadata(),
                                        (first, second) -> {
                                            Double newRank = first.getRank() + second.getRank();
                                            Set<Object> derivedFrom = new HashSet<>();
                                            derivedFrom.addAll(first.getSources());
                                            derivedFrom.addAll(second.getSources());
                                            return new KeywordMetadata(derivedFrom, newRank);
                                        }
                                )
                        ))
                );
    }

}
