package social.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfile {

    private String name;
    private boolean isFacebookConnected;
    private boolean isTwitterConnected;
    private boolean isSpotifyConnected;

}
