package social.service;

import social.service.keyword.CompositeKeywordCollector;
import social.service.keyword.KeywordCollector;
import social.service.keyword.KeywordCollectorImpl;
import social.service.spotify.Spotify;
import social.service.spotify.TmSimpleArtist;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;
import org.apache.commons.math3.util.Pair;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

@Named
public class SpotifyKeywordService extends SocialKeywordService<Spotify> {

    @Inject
    private Function<String, Pair<String, Double>> spotifyKeywordRanker;

    @Override
    protected Class<Spotify> apiType() {
        return Spotify.class;
    }

    @Override
    protected KeywordCollector keywordCollector(Spotify api) {
        return new CompositeKeywordCollector(artistKeywordCollector(api));
    }

    private KeywordCollector artistKeywordCollector(Spotify api) {
        String userId = api.findUser().getId();

        KeywordCollector tracks = create(getArtists(api, userId));
        KeywordCollector starredTracks = create(getStarredArtists(api, userId));
        KeywordCollector followedArtists = create(getFollowedArtists(api, userId));
        KeywordCollector similarToFollowedArtists = create(getSimilarArtists(api, userId));

        return new CompositeKeywordCollector(tracks, starredTracks, followedArtists, similarToFollowedArtists);
    }

    private KeywordCollector create(Stream<SimpleArtist> artists) {
        return KeywordCollectorImpl.<SimpleArtist>builder()
                .src(artists)
                .extractKeywords(artist -> Stream.of(spotifyKeywordRanker.apply(artist.getName())))
                .mapSource(artist -> "Artist name: " + artist.getName())
                .build();
    }

    private Stream<SimpleArtist> getArtists(Spotify api, String userId) {
        return api.findUserPlaylists(userId)
                .stream()
                .filter(playlist -> userId.equals(playlist.getOwner().getId()))
                .flatMap(playlist -> api.findUserPlaylistTracks(userId, playlist.getId()).stream())
                .map(PlaylistTrack::getTrack)
                .map(Track::getArtists)
                .flatMap(List::stream);
    }

    private Stream<SimpleArtist> getStarredArtists(Spotify api, String userId) {
        return api.findStarredTracks(userId)
                .stream()
                .map(PlaylistTrack::getTrack)
                .map(Track::getArtists)
                .flatMap(List::stream);
    }

    private Stream<SimpleArtist> getFollowedArtists(Spotify api, String userId) {
        return api.findUserFollowedArtists(userId)
                .stream();
    }

    private Stream<SimpleArtist> getSimilarArtists(Spotify api, String userId) {
        Set<SimpleArtist> result = new HashSet<>();
        api.findUserFollowedArtists(userId)
                .forEach(followedArtist -> {
                    List<TmSimpleArtist> similarArtists = api.findSimilarArtists(followedArtist.getId());
                    result.addAll(similarArtists);
                });
        return result.stream();
    }
}
