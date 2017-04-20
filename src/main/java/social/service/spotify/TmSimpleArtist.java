package social.service.spotify;

import com.wrapper.spotify.models.SimpleArtist;

import java.util.Objects;

public class TmSimpleArtist extends SimpleArtist {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TmSimpleArtist that = (TmSimpleArtist) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
