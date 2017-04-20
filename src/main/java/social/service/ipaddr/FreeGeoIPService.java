package social.service.ipaddr;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;

import java.util.Collections;
import java.util.Map;

@Component
public class FreeGeoIPService implements IPAddressService {

    private final RestOperations restOperations;

    {
        RestTemplate restTemplate = new RestTemplate();
        DefaultUriTemplateHandler defaultUriTemplateHandler = new DefaultUriTemplateHandler();
        defaultUriTemplateHandler.setParsePath(true);
        defaultUriTemplateHandler.setBaseUrl("http://freegeoip.net/json");
        restTemplate.setUriTemplateHandler(defaultUriTemplateHandler);
        restOperations = restTemplate;
    }

    @Override
    public IPAddressInfo getInfo(String ipAddress) {
        Map<String, ?> params = Collections.singletonMap("address", ipAddress);
        return restOperations.getForObject("/{address}", IPAddressInfo.class, params);
    }

}
