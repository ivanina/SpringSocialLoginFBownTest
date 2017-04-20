package social.service;

import javax.inject.Inject;

public abstract class AbstractKeywordService implements KeywordService {

    @Inject
    protected NaturalLanguageUnderstandingService nluService;

}
