package social.service.keyword;

import social.model.KeywordMetadata;
import lombok.Builder;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
public class KeywordCollectorImpl<Entity> extends KeywordCollector {

    private Stream<Entity> src;
    private Function<Entity, Stream<Pair<String, Double>>> extractKeywords;
    private Function<Entity, Object> mapSource;

    @Override
    protected Collection<Pair<String, KeywordMetadata>> collectKeywords() {
        return src.flatMap(entity -> extractKeywords.apply(entity)
                .map(keywordRankPair -> {
                    Object mappedEntity = mapSource.apply(entity);
                    Set<Object> sourceList = Collections.singleton(mappedEntity);
                    KeywordMetadata metadata = new KeywordMetadata(sourceList, keywordRankPair.getValue());
                    return Pair.create(keywordRankPair.getKey(), metadata);
                })
        ).collect(Collectors.toList());
    }

}
