package com.montilla.laboratory.myfeatures.common.iod;

import com.montilla.laboratory.myfeatures.common.iod.exception.TierException;
import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;
import com.montilla.laboratory.myfeatures.common.iod.mapping.TierMapper;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IoDConnectorTemplateTest {

    @Test(expected = NullPointerException.class)
    public void giveNullTierMapper_WheBuildIoDTemplate_ThenThrowsNullPointerException() {
        AbstractionCall abstractionCall = mock(AbstractionCall.class);
        TierMapper responseMapper = mock(TierMapper.class);
        TierExceptionHandler exceptionHandler = mock(TierExceptionHandler.class);
        IoDConnectorTemplate ioDConnectorTemplate = new IoDConnectorTemplate(null, abstractionCall, responseMapper, exceptionHandler) {
            @Override
            public Object execute(Object input) {
                return super.execute(input);
            }
        };
    }

    @Test(expected = NullPointerException.class)
    public void giveNullAbstractionCall_WheBuildIoDTemplate_ThenThrowsNullPointerException() {
        TierMapper requestMapper = mock(TierMapper.class);
        AbstractionCall abstractionCall = null;
        TierMapper responseMapper = mock(TierMapper.class);
        TierExceptionHandler exceptionHandler = mock(TierExceptionHandler.class);
        IoDConnectorTemplate ioDConnectorTemplate = new IoDConnectorTemplate(requestMapper, abstractionCall, responseMapper, exceptionHandler) {
            @Override
            public Object execute(Object input) {
                return super.execute(input);
            }
        };
    }

    @Test(expected = NullPointerException.class)
    public void giveNullResponseMapper_WheBuildIoDTemplate_ThenThrowsNullPointerException() {
        TierMapper requestMapper = mock(TierMapper.class);
        AbstractionCall abstractionCall = mock(AbstractionCall.class);
        TierMapper responseMapper = null;
        TierExceptionHandler exceptionHandler = mock(TierExceptionHandler.class);
        IoDConnectorTemplate ioDConnectorTemplate = new IoDConnectorTemplate(requestMapper, abstractionCall, responseMapper, exceptionHandler) {
            @Override
            public Object execute(Object input) {
                return super.execute(input);
            }
        };
    }

    @Test(expected = NullPointerException.class)
    public void giveNullTierExceptionHandler_WheBuildIoDTemplate_ThenThrowsNullPointerException() {
        TierMapper requestMapper = mock(TierMapper.class);
        AbstractionCall abstractionCall = mock(AbstractionCall.class);
        TierMapper responseMapper = mock(TierMapper.class);
        TierExceptionHandler exceptionHandler = null;
        IoDConnectorTemplate ioDConnectorTemplate = new IoDConnectorTemplate(requestMapper, abstractionCall, responseMapper, exceptionHandler) {
            @Override
            public Object execute(Object input) {
                return super.execute(input);
            }
        };
    }

    @Test
    public void givenAllParams_WhenExecute_ThenReturnGoodOutput() {
        TierExceptionHandler<TierException> exceptionHandler = mock(TierExceptionHandler.class);
        TierMapper<Object, Object> upTierRequestMapper = mock(TierMapper.class);
        RepositoryCall repositoryCall = mock(RepositoryCall.class);
        TierMapper<Object, Object> lowTierResponseMapper = mock(TierMapper.class);
        AbstractionCall tierExecutor = AbstractionCall.executeTierFunctionWithExceptionHandler(repositoryCall).exceptionHandler(exceptionHandler);

        Object upTierRequest = new Object();
        Object lowTierRequest = new Object();
        Object lowTierResponse = new Object();
        Object upTierResponse = new Object();
        when(upTierRequestMapper.map(upTierRequest)).thenReturn(lowTierRequest);
        when(repositoryCall.call(lowTierRequest)).thenReturn(lowTierResponse);
        when(lowTierResponseMapper.map(lowTierResponse)).thenReturn(upTierResponse);

        IoDConnectorTemplate ioDConnectorTemplate = new IoDConnectorTemplate(upTierRequestMapper, tierExecutor, lowTierResponseMapper, exceptionHandler) {
            @Override
            public Object execute(Object input) {
                return super.execute(input);
            }
        };

        Object response = ioDConnectorTemplate.execute(upTierRequest);
        assertNotNull(response);
        assertEquals(upTierResponse, response);
    }
}