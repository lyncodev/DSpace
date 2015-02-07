package org.dspace.sushi.security;

import com.google.common.base.Optional;

public class NullAuthorizationService implements SushiAuthorizationService {
    @Override
    public boolean isAuthorized(Optional<String> costumerId) {
        return true;
    }
}
