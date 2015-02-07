package org.dspace.sushi.events;

import com.google.common.base.Optional;
import com.lyncode.sushicounter.events.model.ItemUsageData;
import com.lyncode.sushicounter.events.repository.EventRepositoryQueryRequest;
import com.lyncode.sushicounter.events.repository.EventRepositoryQueryResponse;
import org.dspace.services.ConfigurationService;
import org.dspace.sushi.exceptions.SushiServiceException;
import org.dspace.sushi.security.SushiAuthorizationService;
import org.dspace.sushi.solr.SolrItemUsageDataResolver;
import org.junit.Test;
import org.niso.schemas.counter.DataType;

import java.util.ArrayList;
import java.util.Collections;

import static java.util.Collections.emptyList;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DSpaceSolrEventRepositoryTest {
    public static final String REPOSITORY_NAME = "NAME";
    private final ConfigurationService configurationService = mock(ConfigurationService.class);
    private final SushiAuthorizationService authorizationService = mock(SushiAuthorizationService.class);
    private final SolrItemUsageDataResolver usageDataResolver = mock(SolrItemUsageDataResolver.class);
    private DSpaceSolrEventRepository underTest = new DSpaceSolrEventRepository(configurationService, authorizationService, usageDataResolver);

    @Test
    public void configurationMustReturnDSpaceAsPlatform() throws Exception {
        // when
        String result = underTest.configuration().getPlatformName();

        // then
        assertThat(result, equalTo("DSpace"));
    }

    @Test
    public void configurationNameMustReturnDSpaceNameProperty() throws Exception {
        given(configurationService.getProperty("dspace.name")).willReturn(REPOSITORY_NAME);

        // when
        String result = underTest.configuration().getInstitutionName();

        // then
        assertThat(result, equalTo(REPOSITORY_NAME));
    }

    @Test
    public void configurationMustReturnBookAsDataType() throws Exception {
        // when
        DataType result = underTest.configuration().getDataType();

        // then
        assertThat(result, equalTo(DataType.BOOK));
    }

    @Test
    public void retrieveMustReturnErrorIfCostumerUnauthorized() throws Exception {
        given(authorizationService.isAuthorized(any(Optional.class))).willReturn(false);

        // when
        EventRepositoryQueryResponse result = underTest.retrieve(request().build());

        // then
        assertThat(result.events(), is(empty()));
        assertThat(result.errors(), is(not(empty())));
    }

    @Test
    public void retrieveShouldDelegateToTheItemDataUsageResolver() throws Exception {
        EventRepositoryQueryRequest request = request().build();
        given(authorizationService.isAuthorized(any(Optional.class))).willReturn(true);
        given(usageDataResolver.resolve(request)).willReturn(new ArrayList<ItemUsageData>() {{ add(mock(ItemUsageData.class)); }});

        // when
        EventRepositoryQueryResponse result = underTest.retrieve(request);

        // then
        verify(usageDataResolver).resolve(request);
        assertThat(result.errors(), is(empty()));
        assertThat(result.events(), is(not(empty())));
    }

    @Test
    public void retrieveShouldDelegateToTheItemDataUsageResolverAndShowErrorIfSushiServiceExceptionIsThrown() throws Exception {
        EventRepositoryQueryRequest request = request().build();
        EventRepositoryQueryResponse.Error error = new EventRepositoryQueryResponse.Error("message");
        given(authorizationService.isAuthorized(any(Optional.class))).willReturn(true);
        given(usageDataResolver.resolve(request)).willThrow(new SushiServiceException(error));

        // when
        EventRepositoryQueryResponse result = underTest.retrieve(request);

        // then
        verify(usageDataResolver).resolve(request);
        assertThat(result.errors(), hasItem(error));
        assertThat(result.events(), is(empty()));
    }

    private EventRepositoryQueryRequest.Builder request() {
        return new EventRepositoryQueryRequest.Builder();
    }
}