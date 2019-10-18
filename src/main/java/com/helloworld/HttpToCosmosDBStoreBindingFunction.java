package com.helloworld;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.util.Optional;

import static com.helloworld.FunctionsUtils.*;

/**
 * Azure Functions with HTTP Trigger and CosmosDB output binding.
 */
public class HttpToCosmosDBStoreBindingFunction {

    @FunctionName("CosmosDBStoreBinding")
    public HttpResponseMessage run(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            @CosmosDBOutput(
                    name = "database",
                    databaseName = "university",
                    collectionName = "students",
                    connectionStringSetting = "CosmosDbConnection")
                    OutputBinding<String> outputItem,
            final ExecutionContext context) {
        context.getLogger().info(JAVA_HTTP_TRIGGER_PROCESSED_A_REQUEST);

        String name = EMPTY;
        String email = EMPTY;

        // Parse query parameter
        if (request.getBody().isPresent()) {
            JSONObject jsonObject = new JSONObject(request.getBody().get());
            name = jsonObject.getString(NAME);
            email = jsonObject.getString(EMAIL);
        }

        // Generate random ID
        final String id = String.valueOf(randomLong());

        // Generate document
        Student student = new Student(id, name, email);
        final String database = JSONWriter.valueToString(student);

        context.getLogger().info(String.format(DOCUMENT_TO_BE_SAVED, database));

        outputItem.setValue(database);

        // return a different document to the browser or calling client.
        return request.createResponseBuilder(HttpStatus.OK)
                .body(DOCUMENT_CREATED_SUCCESSFULLY)
                .build();
    }
}
