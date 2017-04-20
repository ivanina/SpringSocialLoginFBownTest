package social.service.spotify;

import java.util.List;

import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.User;

public interface Spotify {

    User findUser();

    List<SimplePlaylist> findUserPlaylists(String userId);

    List<PlaylistTrack> findUserPlaylistTracks(String userId, String playlistId);

    List<PlaylistTrack> findStarredTracks(String userId);

    List<SimpleArtist> findUserFollowedArtists(String userId);

    List<TmSimpleArtist> findSimilarArtists(String artistId);
}