package org.dspace.sushi.solr;

import com.lyncode.sushicounter.events.model.ItemUsageData;
import com.lyncode.sushicounter.events.repository.EventRepositoryQueryRequest;
import com.lyncode.sushicounter.events.repository.EventRepositoryQueryResponse;
import org.dspace.sushi.exceptions.SushiServiceException;

import java.util.Collection;

public class DSpaceSolrItemUsageDataResolver implements SolrItemUsageDataResolver {
    private final Collection<SolrItemUsageDataReport> reports;

    public DSpaceSolrItemUsageDataResolver(Collection<SolrItemUsageDataReport> reports) {
        this.reports = reports;
    }

    @Override
    public Collection<ItemUsageData> resolve(EventRepositoryQueryRequest request) throws SushiServiceException {
        if (!request.getReportType().isPresent()) throw new SushiServiceException(new EventRepositoryQueryResponse.Error("Report name undefined"));
        if (!request.getReportVersion().isPresent()) throw new SushiServiceException(new EventRepositoryQueryResponse.Error("Report release version undefined"));

        for (SolrItemUsageDataReport report : reports) {
            if (report.is(request.getReportType().get(), request.getReportVersion().get()))
                return report.report(request.getStart(), request.getEnd());
        }

        throw new SushiServiceException(new EventRepositoryQueryResponse.Error("Report not supported"));
    }
}
