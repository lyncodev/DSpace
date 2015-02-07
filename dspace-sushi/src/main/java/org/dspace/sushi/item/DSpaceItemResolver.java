package org.dspace.sushi.item;

import org.dspace.content.Item;
import org.dspace.sushi.context.ContextService;
import org.dspace.sushi.exceptions.InternalSushiServiceException;

import java.sql.SQLException;

public class DSpaceItemResolver implements ItemResolver {
    private final ContextService contextService;

    public DSpaceItemResolver(ContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public ItemWrapper resolve(int itemId) {
        try {
            return new ItemWrapper(Item.find(contextService.getContext(), itemId));
        } catch (SQLException e) {
            throw new InternalSushiServiceException(e);
        }
    }
}
