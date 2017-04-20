package social.service.spotify;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

import social.util.MutableUserProfile;
import com.wrapper.spotify.models.User;

public class SpotifyApiAdapter implements ApiAdapter<Spotify> {

    @Override
    public boolean test(Spotify api) {
        return currentUser(api) != null;
    }

    @Override
    public void setConnectionValues(Spotify api, ConnectionValues values) {
        User user = currentUser(api);
        values.setDisplayName(user.getDisplayName());
        values.setProfileUrl(user.getUri());
        values.setProviderUserId(user.getId());
    }

    @Override
    public UserProfile fetchUserProfile(Spotify api) {
        User user = currentUser(api);
        MutableUserProfile userProfile = new MutableUserProfile();
        userProfile.setId(user.getId());
        userProfile.setUsername(user.getDisplayName());
        userProfile.setEmail(user.getEmail());
        return userProfile;
    }

    @Override
    public void updateStatus(Spotify api, String message) {

    }

    private User currentUser(Spotify api) {
        return api.findUser();
    }
}