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
import java.util.Random;

import static com.helloworld.HttpToCosmosDBStoreBindingFunction.EMPTY;

/**
 * Azure Functions with HTTP Trigger and CosmosDB output.
 */
public class HttpToCosmosDBStoreAnnotationFunction {

    @FunctionName("CosmosDBStoreAnnotation")
    @CosmosDBOutput(name = "database",
            databaseName = "university",
            collectionName = "students",
            connectionStringSetting = "CosmosDbConnection")
    public String run(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        String name = EMPTY;
        String email = EMPTY;

        // Parse query parameter
        if (request.getBody().isPresent()) {
            JSONObject jsonObject = new JSONObject(request.getBody().get());
            name = jsonObject.getString("name");
            email = jsonObject.getString("email");
        }

        // Generate random ID
        final long id = Math.abs(new Random().nextInt());

        // Generate document
        final String database = new Student(id, name, email).toString();

        context.getLogger().info(String.format("Document to be saved: %s", database));

        return database;
    }
}
