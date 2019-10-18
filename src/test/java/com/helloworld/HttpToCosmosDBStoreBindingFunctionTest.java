package com.helloworld;

import com.microsoft.azure.functions.*;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static com.helloworld.FunctionsUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * Unit test for Function class.
 */
public class HttpToCosmosDBStoreBindingFunctionTest {
    /**
     * Unit test for HttpTriggerJava method.
     */
    @Test
    public void testHttpTriggerJava() throws Exception {
        // Setup
        @SuppressWarnings("unchecked") final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);
        @SuppressWarnings("unchecked") final OutputBinding<String> outputBinding = mock(OutputBinding.class);

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put(NAME, "Nebrass");
        queryParams.put(EMAIL, "lnibrass@gmail.com");

        doReturn(queryParams).when(req).getQueryParameters();

        final Optional<String> queryBody = Optional.empty();
        doReturn(queryBody).when(req).getBody();

        doAnswer(new Answer<HttpResponseMessage.Builder>() {
            @Override
            public HttpResponseMessage.Builder answer(InvocationOnMock invocation) {
                HttpStatus status = (HttpStatus) invocation.getArguments()[0];
                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
            }
        }).when(req).createResponseBuilder(any(HttpStatus.class));

        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        // Invoke
        final HttpResponseMessage ret =
                new HttpToCosmosDBStoreBindingFunction()
                        .run(
                                req,
                                outputBinding,
                                context
                        );

        // Verify
        assertEquals(HttpStatus.OK, ret.getStatus());
        assertEquals(DOCUMENT_CREATED_SUCCESSFULLY, ret.getBody());
    }
}
