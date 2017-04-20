package social.service.ipaddr;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class IPAddressInfo {

    private String ip;
    private String countryCode;
    private String countryName;
    private String regionCode;
    private String regionName;
    private String city;
    private String zipCode;
    private String timeZone;
    private Double latitude;
    private Double longitude;
    private Long metroCode;

}
