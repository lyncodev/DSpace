package org.dspace.sushi.cxf;

import com.lyncode.sushicounter.SushiServiceImpl;
import org.apache.solr.client.solrj.SolrServerException;
import org.dspace.services.ConfigurationService;
import org.dspace.sushi.context.ContextService;
import org.dspace.sushi.context.LocalContextService;
import org.dspace.sushi.events.DSpaceSolrEventRepository;
import org.dspace.sushi.item.DSpaceItemResolver;
import org.dspace.sushi.item.ItemResolver;
import org.dspace.sushi.security.NullAuthorizationService;
import org.dspace.sushi.solr.DSpaceSolrItemUsageDataResolver;
import org.dspace.sushi.solr.SolrItemUsageDataReport;
import org.dspace.sushi.solr.reports.BR1Release4ItemUsageStatisticsReport;
import org.dspace.sushi.solr.reports.DSpaceUsageStatisticsReport;
import org.dspace.sushi.solr.reports.UsageStatisticsReport;
import org.dspace.sushi.solr.reports.UsageStatisticsReportFactory;
import org.dspace.sushi.solr.service.DSpaceSolrQueryService;
import org.dspace.sushi.solr.service.SolrQueryService;
import org.dspace.sushi.solr.service.SolrServerResolver;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Arrays.asList;

public class SushiServiceFactory {
    @Autowired
    private ConfigurationService configurationService;

    public SushiServiceImpl create() throws SolrServerException {
        final ContextService contextService = new LocalContextService();
        final ItemResolver itemResolver = new DSpaceItemResolver(contextService);
        SolrServerResolver solrServerResolver = new SolrServerResolver();

        NullAuthorizationService authorizationService = new NullAuthorizationService();
        SolrQueryService solrQueryService = new DSpaceSolrQueryService(solrServerResolver.getServer());

        SolrItemUsageDataReport br1 = new DSpaceUsageStatisticsReport("BR1", "4", solrQueryService, new UsageStatisticsReportFactory() {
            @Override
            public UsageStatisticsReport create(LocalDate start, LocalDate end) {
                return new BR1Release4ItemUsageStatisticsReport(itemResolver, start, end);
            }
        });

        return new SushiServiceImpl(new DSpaceSolrEventRepository(configurationService,
                authorizationService,
                new DSpaceSolrItemUsageDataResolver(asList(br1))
        ));
    }
}
