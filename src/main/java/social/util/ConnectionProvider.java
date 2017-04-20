package social.util;

import org.springframework.social.connect.Connection;

import java.util.Optional;

public interface ConnectionProvider<T> {

    Optional<Connection<T>> getConnection();

}
