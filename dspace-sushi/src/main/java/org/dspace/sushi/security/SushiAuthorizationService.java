package org.dspace.sushi.security;

import com.google.common.base.Optional;

public interface SushiAuthorizationService {
    boolean isAuthorized (Optional<String> costumerId);
}
