package com.montilla.laboratory.myfeatures.common.tier;

import com.montilla.laboratory.myfeatures.common.iod.AbstractionCall;
import com.montilla.laboratory.myfeatures.common.iod.IoDConnector;
import com.montilla.laboratory.myfeatures.common.iod.RepositoryCall;
import com.montilla.laboratory.myfeatures.common.iod.exception.TierException;
import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;
import com.montilla.laboratory.myfeatures.common.iod.mapping.TierMapper;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IoDConnectorTest {

    @Test(expected = NullPointerException.class)
    public void givenNullExceptionHandler_WhenBuildConnector_ThenThrowsNullPointerException() {
        IoDConnector.buildWithExceptionHandler(null);
    }

    @Test
    public void givenNotNullExceptionHandler_WhenBuildConnector_ThenReturnAnInstanceOfIt() {
        TierExceptionHandler<TierException> exceptionHandler = mock(TierExceptionHandler.class);
        assertNotNull(IoDConnector.buildWithExceptionHandler(exceptionHandler));
    }

    @Test(expected = NullPointerException.class)
    public void givenNullUpTierRequestMapper_WhenBuildConnectorApplyUpTierRequestMapper_ThenThrowsNullPointerException() {
        TierExceptionHandler<TierException> exceptionHandler = mock(TierExceptionHandler.class);
        IoDConnector.buildWithExceptionHandler(exceptionHandler)
                     .applyUpTierRequestMapper(null);
    }

    @Test(expected = NullPointerException.class)
    public void givenNotNullUpTierRequestMapper_WhenBuildConnectorApplyUpTierRequestMapper_ThenMapperReturnsNull() {
        TierExceptionHandler<TierException> exceptionHandler = mock(TierExceptionHandler.class);
        TierMapper<Object, Object> upTierRequestMapper = mock(TierMapper.class);
        TierMapper<Object, Object> upTierResponsetMapper = mock(TierMapper.class);
        AbstractionCall abstractionCall = mock(AbstractionCall.class);
        IoDConnector<Object, Object> ioDConnector = IoDConnector.buildWithExceptionHandler(exceptionHandler)
                .applyUpTierRequestMapper(upTierRequestMapper)
                .executeCall(abstractionCall)
                .applyUpTierResponseMapper(upTierResponsetMapper);
        Object upTierRequest = new Object();
        when(upTierRequestMapper.map(upTierRequest)).thenReturn(null);

        ioDConnector.connect(upTierRequest);
    }

    @Test(expected = NullPointerException.class)
    public void givenNotNullUpTierRequestMapperAndNullTierExecutor_WhenBuildConnectorApplyUpTierRequestMapperAndExecute_ThenThrowsNullPointerException() {
        TierExceptionHandler<TierException> exceptionHandler = mock(TierExceptionHandler.class);
        TierMapper<Object, Object> upTierRequestMapper = mock(TierMapper.class);
        TierMapper<Object, Object> upTierResponsetMapper = mock(TierMapper.class);
        IoDConnector<Object, Object> ioDConnector =
                IoDConnector
                        .buildWithExceptionHandler(exceptionHandler)
                        .applyUpTierRequestMapper(upTierRequestMapper)
                        .executeCall(null)
                        .applyUpTierResponseMapper(upTierResponsetMapper);

        Object upTierRequest = new Object();
        when(upTierRequestMapper.map(upTierRequest)).thenReturn(new Object());

        ioDConnector.connect(upTierRequest);
    }

    @Test(expected = NullPointerException.class)
    public void givenNotNullUpTierRequestMapperAndNotNullTierExecutor_WhenBuildConnectorApplyUpTierRequestMapperAndExecute_ThenTierExecutorReturnsNullThrowsNullPointerException() {
        TierExceptionHandler<TierException> exceptionHandler = mock(TierExceptionHandler.class);
        TierMapper<Object, Object> upTierRequestMapper = mock(TierMapper.class);
        TierMapper<Object, Object> upTierResponsetMapper = mock(TierMapper.class);
        AbstractionCall<Object, Object> tierExecutor  = mock(AbstractionCall.class);

        IoDConnector<Object, Object> ioDConnector =
                IoDConnector
                        .buildWithExceptionHandler(exceptionHandler)
                        .applyUpTierRequestMapper(upTierRequestMapper)
                        .executeCall(tierExecutor)
                        .applyUpTierResponseMapper(upTierResponsetMapper);

        Object upTierRequest = new Object();
        when(upTierRequestMapper.map(upTierRequest)).thenReturn(new Object());
        when(tierExecutor.apply(upTierRequest)).thenReturn(null);

        ioDConnector.connect(upTierRequest);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullUpTierResponseMapper_WhenApplyUpTierResponseMapper_ThenThrowsNullPointerException() {
        TierExceptionHandler<TierException> exceptionHandler = mock(TierExceptionHandler.class);
        TierMapper<Object, Object> upTierRequestMapper = mock(TierMapper.class);
        RepositoryCall repositoryCall = mock(RepositoryCall.class);

        AbstractionCall tierExecutor = AbstractionCall.executeTierFunctionWithExceptionHandler(repositoryCall).exceptionHandler(exceptionHandler);

        IoDConnector<Object, Object> ioDConnector =
                IoDConnector
                        .buildWithExceptionHandler(exceptionHandler)
                        .applyUpTierRequestMapper(upTierRequestMapper)
                        .executeCall(tierExecutor)
                        .applyUpTierResponseMapper(null);

        Object upTierRequest = new Object();
        Object lowTierRequest = new Object();
        Object lowTierResponse = new Object();
        when(upTierRequestMapper.map(upTierRequest)).thenReturn(lowTierRequest);
        when(repositoryCall.call(lowTierRequest)).thenReturn(lowTierResponse);

        ioDConnector.connect(upTierRequest);
    }

    @Test(expected = NullPointerException.class)
    public void givenNotNullParameters_WhenApplyUpTierResponseMapper_ThenLowTierResponseMapperReturnNull() {
        TierExceptionHandler<TierException> exceptionHandler = mock(TierExceptionHandler.class);
        TierMapper<Object, Object> upTierRequestMapper = mock(TierMapper.class);
        RepositoryCall repositoryCall = mock(RepositoryCall.class);
        TierMapper<Object, Object> lowTierResponseMapper = mock(TierMapper.class);
        AbstractionCall tierExecutor = AbstractionCall.executeTierFunctionWithExceptionHandler(repositoryCall).exceptionHandler(exceptionHandler);

        IoDConnector<Object, Object> ioDConnector =
                IoDConnector
                        .buildWithExceptionHandler(exceptionHandler)
                        .applyUpTierRequestMapper(upTierRequestMapper)
                        .executeCall(tierExecutor)
                        .applyUpTierResponseMapper(lowTierResponseMapper);

        Object upTierRequest = new Object();
        Object lowTierRequest = new Object();
        Object lowTierResponse = new Object();
        when(upTierRequestMapper.map(upTierRequest)).thenReturn(lowTierRequest);
        when(repositoryCall.call(lowTierRequest)).thenReturn(lowTierResponse);

        ioDConnector.connect(upTierRequest);
    }

    @Test
    public void givenNotNullParameters_WhenApplyUpTierResponseMapper_ThenReturnUpTierResponse() {
        TierExceptionHandler<TierException> exceptionHandler = mock(TierExceptionHandler.class);
        TierMapper<Object, Object> upTierRequestMapper = mock(TierMapper.class);
        RepositoryCall repositoryCall = mock(RepositoryCall.class);
        TierMapper<Object, Object> lowTierResponseMapper = mock(TierMapper.class);
        AbstractionCall tierExecutor = AbstractionCall.executeTierFunctionWithExceptionHandler(repositoryCall).exceptionHandler(exceptionHandler);

        IoDConnector<Object, Object> ioDConnector =
                IoDConnector
                        .buildWithExceptionHandler(exceptionHandler)
                        .applyUpTierRequestMapper(upTierRequestMapper)
                        .executeCall(tierExecutor)
                        .applyUpTierResponseMapper(lowTierResponseMapper);

        Object upTierRequest = new Object();
        Object lowTierRequest = new Object();
        Object lowTierResponse = new Object();
        Object upTierResponse = new Object();
        when(upTierRequestMapper.map(upTierRequest)).thenReturn(lowTierRequest);
        when(repositoryCall.call(lowTierRequest)).thenReturn(lowTierResponse);
        when(lowTierResponseMapper.map(lowTierResponse)).thenReturn(upTierResponse);

        Object connect = ioDConnector.connect(upTierRequest);
        assertNotNull(connect);
        assertEquals(upTierResponse, connect);
    }


    @Test(expected = TierException.class)
    public void givenNotNullParameters_WhenApplyUpTierResponseMapper_ThenTierFunctionThrowsDataException() {
        TierExceptionHandler<TierException> exceptionHandler = mock(TierExceptionHandler.class);
        TierException tierException = new TierException("XXX");
        TierMapper<Object, Object> upTierRequestMapper = mock(TierMapper.class);
        RepositoryCall repositoryCall = mock(RepositoryCall.class);
        TierMapper<Object, Object> lowTierResponseMapper = mock(TierMapper.class);
        AbstractionCall tierExecutor = AbstractionCall.executeTierFunctionWithExceptionHandler(repositoryCall).exceptionHandler(exceptionHandler);

        IoDConnector<Object, Object> ioDConnector =
                IoDConnector
                        .buildWithExceptionHandler(exceptionHandler)
                        .applyUpTierRequestMapper(upTierRequestMapper)
                        .executeCall(tierExecutor)
                        .applyUpTierResponseMapper(lowTierResponseMapper);

        Object upTierRequest = new Object();
        Object lowTierRequest = new Object();
        when(upTierRequestMapper.map(upTierRequest)).thenReturn(lowTierRequest);
        when(repositoryCall.call(lowTierRequest)).thenThrow(tierException);
        when(exceptionHandler.handleRuntimeException(tierException)).thenReturn(tierException);

        ioDConnector.connect(upTierRequest);
    }
}