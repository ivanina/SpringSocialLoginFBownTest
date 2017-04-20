package social_light.controller;

import social_light.model.UserProfile;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("api/users")
public class UserResource {

    @Inject
    private ConnectionRepository connectionRepository;

    @GetMapping
    public UserProfile getCurrentUserProfile() {
        UserProfile userProfile = new UserProfile();
        Connection<Facebook> facebookConnection = connectionRepository.findPrimaryConnection(Facebook.class);
        Connection<Twitter> twitterConnection = connectionRepository.findPrimaryConnection(Twitter.class);

        if (twitterConnection != null) {
            userProfile.setTwitterConnected(true);
            userProfile.setName(twitterConnection.getDisplayName());
        }

        if (facebookConnection != null) {
            userProfile.setFacebookConnected(true);
            userProfile.setName(facebookConnection.getDisplayName());
        }
        return userProfile;
    }

}
