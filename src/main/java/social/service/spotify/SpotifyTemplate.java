package social.service.spotify;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.models.Artist;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.web.client.RestOperations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpotifyTemplate extends AbstractOAuth2ApiBinding implements Spotify {

    private final Api api;
    private final RestOperations restOperations;
    private static final String SPOTIFY_FOLLOWED_USERS_URL = "https://api.spotify.com/v1/me/following?type=artist";

    public SpotifyTemplate(String accessToken) {
        super(accessToken);

        api = Api.builder()
                .accessToken(accessToken)
                .build();

        restOperations = getRestTemplate();
    }

    @Override
    public User findUser() {
        try {
            return api.getMe()
                    .build()
                    .get();
        } catch (IOException | WebApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SimplePlaylist> findUserPlaylists(String userId) {
        try {
            return api.getPlaylistsForUser(userId)
                    .build()
                    .get()
                    .getItems();
        } catch (IOException | WebApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PlaylistTrack> findStarredTracks(String userId) {
        try {
            return api.getStarred(userId)
                    .build()
                    .get()
                    .getItems();
        } catch (IOException | WebApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PlaylistTrack> findUserPlaylistTracks(String userId, String playlistId) {
        try {
            return api.getPlaylistTracks(userId, playlistId)
                    .build()
                    .get()
                    .getItems();
        } catch (IOException | WebApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SimpleArtist> findUserFollowedArtists(String userId) {
        List<SimpleArtist> result = new ArrayList<>();

        JSONObject page = getFollowedArtistsPage(SPOTIFY_FOLLOWED_USERS_URL);

        String nextPageUrl;
        do {
            collectArtists(result, page.getJSONArray("items"));
            nextPageUrl = nullIfDefault(page.getString("next"));
            if (nextPageUrl != null) {
                page = getFollowedArtistsPage(nextPageUrl);
            }
        } while (nextPageUrl != null);

        return result;
    }

    @Override
    public List<TmSimpleArtist> findSimilarArtists(String artistId) {
        Objects.requireNonNull(artistId);
        List<TmSimpleArtist> result = new ArrayList<>();
        try {
            List<Artist> artists = api.getArtistRelatedArtists(artistId).build().get();
            artists.forEach(artist -> {
                result.add(toSimpleArtist(artist));
            });
            return result;
        } catch (IOException | WebApiException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject getFollowedArtistsPage(String url) {
        return JSONObject
                .fromObject(restOperations.getForObject(url, String.class))
                .getJSONObject("artists");
    }

    private String nullIfDefault(String next) {
        return "null".equals(next) ? null : next;
    }

    private void collectArtists(List<SimpleArtist> result, JSONArray jsonArray) {
        for (int index = 0; index < jsonArray.size(); index++) {
            JSONObject item = jsonArray.getJSONObject(index);
            SimpleArtist artist = new SimpleArtist();
            artist.setId(item.getString("id"));
            artist.setName(item.getString("name"));
            artist.setHref(item.getString("href"));
            artist.setUri(item.getString("uri"));
            result.add(artist);
        }
    }

    private TmSimpleArtist toSimpleArtist(Artist artist) {
        TmSimpleArtist simpleArtist = new TmSimpleArtist();
        simpleArtist.setId(artist.getId());
        simpleArtist.setName(artist.getName());
        simpleArtist.setType(artist.getType());
        simpleArtist.setHref(artist.getHref());
        simpleArtist.setUri(artist.getUri());
        simpleArtist.setExternalUrls(artist.getExternalUrls());
        return simpleArtist;
    }
}