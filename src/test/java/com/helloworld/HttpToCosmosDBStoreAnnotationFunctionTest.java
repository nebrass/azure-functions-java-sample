package com.helloworld;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static com.helloworld.FunctionsUtils.EMAIL;
import static com.helloworld.FunctionsUtils.NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * Unit test for Function class.
 */
public class HttpToCosmosDBStoreAnnotationFunctionTest {
    /**
     * Unit test for HttpTriggerJava method.
     */
    @Test
    public void testHttpTriggerJava() throws Exception {
        // Setup
        @SuppressWarnings("unchecked") final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put(NAME, "Nebrass");
        queryParams.put(EMAIL, "lnibrass@gmail.com");

        doReturn(queryParams).when(req).getQueryParameters();

        JSONObject jsonObject = new JSONObject(queryParams);
        doReturn(Optional.of(jsonObject.toString())).when(req).getBody();

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
        final String response = new HttpToCosmosDBStoreAnnotationFunction().run(req, context);
        JSONObject responseJsonObject = new JSONObject(response);

        // Verify
        assertEquals(responseJsonObject.getString(NAME), "Nebrass");
        assertEquals(responseJsonObject.getString(EMAIL), "lnibrass@gmail.com");
    }
}
