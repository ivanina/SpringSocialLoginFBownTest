package social.service;

import social.service.keyword.DummyKeywordCollector;
import social.service.keyword.KeywordCollector;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;

import javax.inject.Inject;

public abstract class SocialKeywordService<T> extends AbstractKeywordService {

    @Inject
    private ConnectionRepository connectionRepository;

    @Override
    public final KeywordCollector keywordCollector() {
        Connection<T> connection = connectionRepository.findPrimaryConnection(apiType());
        if (connection == null) {
            return new DummyKeywordCollector();
        }
        return keywordCollector(connection.getApi());
    }

    protected abstract Class<T> apiType();

    protected abstract KeywordCollector keywordCollector(T api);

}
