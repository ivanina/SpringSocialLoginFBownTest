package social.service.keyword;

import social.model.KeywordMetadata;
import org.apache.commons.math3.util.Pair;

import java.util.Collection;
import java.util.Collections;

public class DummyKeywordCollector extends KeywordCollector {

    @Override
    protected Collection<Pair<String, KeywordMetadata>> collectKeywords() {
        return Collections.emptyList();
    }
}
