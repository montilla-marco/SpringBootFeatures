package com.montilla.laboratory.myfeatures.common.tier;

import com.montilla.laboratory.myfeatures.common.iod.AbstractionCall;
import com.montilla.laboratory.myfeatures.common.iod.RepositoryCall;
import com.montilla.laboratory.myfeatures.common.iod.exception.TierException;
import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


public class AbstractionCallTest {

    @Test(expected = NullPointerException.class)
    public void giveNullTierCall_WhenExecuteTierFunctionCall_ThenThrowsNullPointerException() {
        AbstractionCall.executeTierFunctionWithExceptionHandler(null);
    }

    @Test(expected = NullPointerException.class)
    public void giveNoNullTierCallAndNullExceptionHandler_WhenBuildTierFunction_ThenThrowsNullPointerException() {
        RepositoryCall call = Mockito.mock(RepositoryCall.class);
        AbstractionCall.executeTierFunctionWithExceptionHandler(call).exceptionHandler(null);
    }

    @Test(expected = NullPointerException.class)
    public void giveNullInput_WhenBuildTierFunctionAndApply_ThenThrowsNullPointerException() {
        RepositoryCall call = Mockito.mock(RepositoryCall.class);
        TierExceptionHandler<TierException> exceptionHandler = Mockito.mock(TierExceptionHandler.class);
        AbstractionCall.executeTierFunctionWithExceptionHandler(call).exceptionHandler(exceptionHandler).apply(null);
    }

    @Test(expected = TierException.class)
    public void givenAllParams_WhenExecuteTierFunctionCallWithExceptionHandler_ThenTierCallThrowsDataException() {
        RepositoryCall call = Mockito.mock(RepositoryCall.class);
        Object input = new Object();
        TierExceptionHandler<TierException> exceptionHandler = Mockito.mock(TierExceptionHandler.class);
        TierException tierException = new TierException("XXX");
        when(call.call(input)).thenThrow(tierException);
        when(exceptionHandler.handleRuntimeException(tierException)).thenReturn(tierException);

        AbstractionCall
                .executeTierFunctionWithExceptionHandler(call)
                .exceptionHandler(exceptionHandler)
                .apply(input);
    }

    @Test
    public void givenAllParams_WhenExecuteTierFunctionCallWithExceptionHandler_ThenTierCallReturnOutput() {
        RepositoryCall call = Mockito.mock(RepositoryCall.class);
        Object input = new Object();
        Object output  = new Object();
        TierExceptionHandler<TierException> exceptionHandler = Mockito.mock(TierExceptionHandler.class);
        when(call.call(input)).thenReturn(output);

        Object response = AbstractionCall
                .executeTierFunctionWithExceptionHandler(call)
                .exceptionHandler(exceptionHandler)
                .apply(input);
        assertNotNull(response);
        assertEquals(output, response);

    }
}