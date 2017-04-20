package social.service.keyword;

import social.model.KeywordMetadata;
import org.apache.commons.math3.util.Pair;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompositeKeywordCollector extends KeywordCollector {

    private final KeywordCollector[] keywordCollectors;

    public CompositeKeywordCollector(KeywordCollector... keywordCollectors) {
        this.keywordCollectors = keywordCollectors;
    }

    @Override
    protected Collection<Pair<String, KeywordMetadata>> collectKeywords() {
        return Stream.of(keywordCollectors)
                .flatMap(keywordCollector -> keywordCollector.collectKeywords().stream())
                .collect(Collectors.toList());
    }

}
