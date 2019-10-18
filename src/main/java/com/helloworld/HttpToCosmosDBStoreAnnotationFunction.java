package com.helloworld;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.json.JSONObject;

import java.util.Optional;

import static com.helloworld.FunctionsUtils.*;

/**
 * Azure Functions with HTTP Trigger and CosmosDB output.
 */
public class HttpToCosmosDBStoreAnnotationFunction {

    @FunctionName("CosmosDBStoreAnnotation")
    @CosmosDBOutput(
            name = "database",
            databaseName = "university",
            collectionName = "students",
            connectionStringSetting = "CosmosDbConnection")
    public String run(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
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
        final String database = new JSONObject(student).toString();

        context.getLogger().info(String.format(DOCUMENT_TO_BE_SAVED, database));

        return database;
    }
}
