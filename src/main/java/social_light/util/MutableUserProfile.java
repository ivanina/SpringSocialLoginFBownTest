package social_light.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.social.connect.UserProfile;

@Getter
@Setter
public class MutableUserProfile extends UserProfile {

    private String id;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String username;

    public MutableUserProfile() {
        super(null, null, null, null, null, null);
    }

}
