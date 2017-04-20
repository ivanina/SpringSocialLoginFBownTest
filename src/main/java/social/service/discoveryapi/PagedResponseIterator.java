package social.service.discoveryapi;

import com.ticketmaster.api.discovery.DiscoveryApi;
import com.ticketmaster.api.discovery.response.PagedResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Iterator;

@RequiredArgsConstructor
public class PagedResponseIterator<T> implements Iterator<PagedResponse<T>> {

    private final DiscoveryApi discoveryApi;
    @NonNull
    private PagedResponse<T> currentPage;
    private boolean pristine = true;

    @Override
    public boolean hasNext() {
        return currentPage != null &&
                currentPage.getContent() != null &&
                (pristine || currentPage.getNextPageLink() != null);
    }

    @Override
    public PagedResponse<T> next() {
        if (pristine) {
            pristine = false;
            return currentPage;
        }
        try {
            currentPage = discoveryApi.nextPage(currentPage);
            return currentPage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
