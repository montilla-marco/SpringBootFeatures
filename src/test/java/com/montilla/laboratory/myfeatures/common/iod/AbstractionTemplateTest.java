package com.montilla.laboratory.myfeatures.common.iod;

import com.montilla.laboratory.myfeatures.common.iod.exception.TierException;
import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractionTemplateTest {

    @Test(expected = NullPointerException.class)
    public void giveNullRepositoryCall_WheBuildAbstractionTemplate_ThenThrowsNullPointerException() {
        RepositoryCall repositoryCall = null;
        TierExceptionHandler exceptionHandler = mock(TierExceptionHandler.class);
        AbstractionTemplate template = new AbstractionTemplate(repositoryCall, exceptionHandler) { };
    }

    @Test(expected = NullPointerException.class)
    public void giveNulTierExceptionHandler_WheBuildAbstractionTemplate_ThenThrowsNullPointerException() {
        RepositoryCall repositoryCall = mock(RepositoryCall.class);;
        TierExceptionHandler exceptionHandler = null;
        AbstractionTemplate template = new AbstractionTemplate(repositoryCall, exceptionHandler) { };
    }

    @Test(expected = NullPointerException.class)
    public void giveNulTierInput_WheBuildAbstractionTemplate_ThenThrowsNullPointerException() {
        RepositoryCall repositoryCall = mock(RepositoryCall.class);;
        TierExceptionHandler exceptionHandler = mock(TierExceptionHandler.class);
        AbstractionTemplate template = new AbstractionTemplate(repositoryCall, exceptionHandler) {
            @Override
            public Object repoConnect(Object input) {
                return super.repoConnect(input);
            }
        };
        template.repoConnect(null);
    }

    @Test
    public void giveNotNullElements_WheAbstractionTemplateRepoConnect_ThenReturnsOutput() {
        RepositoryCall repositoryCall = Mockito.mock(RepositoryCall.class);
        TierExceptionHandler<TierException> exceptionHandler = Mockito.mock(TierExceptionHandler.class);
        Object input = new Object();
        Object output  = new Object();

        when(repositoryCall.call(input)).thenReturn(output);

        AbstractionTemplate template = new AbstractionTemplate(repositoryCall, exceptionHandler) {
            @Override
            public Object repoConnect(Object input) {
                return super.repoConnect(input);
            }
        };

        Object response = template.repoConnect(input);
        assertNotNull(response);
        assertEquals(output, response);
    }
}