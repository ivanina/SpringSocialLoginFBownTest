package social.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Set;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class KeywordMetadata {

    private Set<Object> sources = Collections.emptySet();
    private Double rank = 0.0;

}
