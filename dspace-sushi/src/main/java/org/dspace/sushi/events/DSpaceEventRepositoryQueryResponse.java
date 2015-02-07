package org.dspace.sushi.events;

import com.lyncode.sushicounter.events.model.ItemUsageData;
import com.lyncode.sushicounter.events.repository.EventRepositoryQueryResponse;

import java.util.ArrayList;
import java.util.Collection;

public class DSpaceEventRepositoryQueryResponse implements EventRepositoryQueryResponse {
    private Collection<Error> errors = new ArrayList<>();
    private Collection<ItemUsageData> usageData = new ArrayList<>();

    public DSpaceEventRepositoryQueryResponse withError (Error error) {
        this.errors.add(error);
        return this;
    }

    public DSpaceEventRepositoryQueryResponse withItemUsageData (Collection<ItemUsageData> usageData) {
        this.usageData.addAll(usageData);
        return this;
    }


    @Override
    public Collection<Error> errors() {
        return errors;
    }

    @Override
    public Collection<ItemUsageData> events() {
        return usageData;
    }


}
