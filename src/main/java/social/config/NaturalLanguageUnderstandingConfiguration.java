package social.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ibm.watson.developer_cloud.http.HttpHeaders;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;

@Configuration
public class NaturalLanguageUnderstandingConfiguration {
    
    @Value("${natural.language.understanding.username}")
    private String username;
    
    @Value("${natural.language.understanding.password}")
    private String password;
    
    @Value("${natural.language.understanding.endpoint}")
    private String endPoint;
    
    @Bean
    public NaturalLanguageUnderstanding naturalLanguageUnderstanding() {
        NaturalLanguageUnderstanding nlu = new NaturalLanguageUnderstanding(
                NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27);
        nlu.setUsernameAndPassword(username, password);
        nlu.setDefaultHeaders(getDefaultHeaders());
        nlu.setEndPoint(endPoint);
        return nlu;
    }

    @Bean
    public Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.X_WATSON_LEARNING_OPT_OUT, Boolean.TRUE.toString());
        headers.put(HttpHeaders.X_WATSON_TEST, Boolean.TRUE.toString());
        return headers;
    }

}
