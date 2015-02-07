package org.dspace.sushi.events;

import com.lyncode.sushicounter.events.repository.EventRepository;
import com.lyncode.sushicounter.events.repository.EventRepositoryConfiguration;
import com.lyncode.sushicounter.events.repository.EventRepositoryQueryRequest;
import com.lyncode.sushicounter.events.repository.EventRepositoryQueryResponse;
import org.dspace.services.ConfigurationService;
import org.dspace.sushi.exceptions.SushiServiceException;
import org.dspace.sushi.security.SushiAuthorizationService;
import org.dspace.sushi.solr.SolrItemUsageDataResolver;
import org.niso.schemas.counter.DataType;

public class DSpaceSolrEventRepository implements EventRepository {
    private final ConfigurationService configurationService;
    private final SushiAuthorizationService authorizationService;
    private final SolrItemUsageDataResolver solrUsageDataService;

    public DSpaceSolrEventRepository(ConfigurationService configurationService, SushiAuthorizationService authorizationService, SolrItemUsageDataResolver solrUsageDataService) {
        this.configurationService = configurationService;
        this.authorizationService = authorizationService;
        this.solrUsageDataService = solrUsageDataService;
    }

    @Override
    public EventRepositoryConfiguration configuration() {
        return new EventRepositoryConfiguration() {
            @Override
            public String getPlatformName() {
                return "DSpace";
            }

            @Override
            public String getInstitutionName() {
                return configurationService.getProperty("dspace.name");
            }

            @Override
            public DataType getDataType() {
                return DataType.BOOK;
            }
        };
    }

    @Override
    public EventRepositoryQueryResponse retrieve(EventRepositoryQueryRequest query) {
        if (!authorizationService.isAuthorized(query.getCostumerReference())) {
            return new DSpaceEventRepositoryQueryResponse()
                    .withError(new EventRepositoryQueryResponse.Error("Unauthorized access to this resource"))
                    ;
        }
        return listUsageData(query);
    }

    private EventRepositoryQueryResponse listUsageData(EventRepositoryQueryRequest query) {
        try {
            return new DSpaceEventRepositoryQueryResponse()
                    .withItemUsageData(solrUsageDataService.resolve(query));
        } catch (SushiServiceException e) {
            return new DSpaceEventRepositoryQueryResponse()
                    .withError(e.getError());
        }
    }
}
