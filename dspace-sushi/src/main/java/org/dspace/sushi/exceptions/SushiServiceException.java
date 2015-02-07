package org.dspace.sushi.exceptions;

import com.lyncode.sushicounter.events.repository.EventRepositoryQueryResponse;

public class SushiServiceException extends Exception {
    private final EventRepositoryQueryResponse.Error error;

    public SushiServiceException(EventRepositoryQueryResponse.Error error) {
        this.error = error;
    }

    public EventRepositoryQueryResponse.Error getError() {
        return error;
    }
}
