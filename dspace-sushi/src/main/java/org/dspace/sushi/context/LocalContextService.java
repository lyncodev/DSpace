package org.dspace.sushi.context;

import org.dspace.core.Context;
import org.dspace.sushi.exceptions.InternalSushiServiceException;

import java.sql.SQLException;

public class LocalContextService implements ContextService {
    private static ThreadLocal<Context> holder = new ThreadLocal<>();

    @Override
    public Context getContext() {
        if (holder.get() == null) {
            try {
                holder.set(new Context());
            } catch (SQLException e) {
                throw new InternalSushiServiceException(e);
            }
        }
        return holder.get();
    }
}
