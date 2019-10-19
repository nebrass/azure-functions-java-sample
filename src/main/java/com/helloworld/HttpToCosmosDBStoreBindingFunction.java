package com.helloworld;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.json.JSONObject;

import java.util.Optional;
import java.util.Random;

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
        context.getLogger().info("Java HTTP trigger processed a request.");

        String name = "empty";
        String email = "empty";

        // Parse query parameter
        if (request.getBody().isPresent()) {
            JSONObject jsonObject = new JSONObject(request.getBody().get());
            name = jsonObject.getString("name");
            email = jsonObject.getString("email");
        }

        // Generate random ID
        final String id = String.valueOf(Math.abs(new Random().nextInt()));

        // Generate document
        Student student = new Student(id, name, email);
        final String database = student.toString();

        context.getLogger().info(String.format("Document to be saved in DB: %s", database));

        outputItem.setValue(database);

        // return the document to calling client.
        return request.createResponseBuilder(HttpStatus.OK)
                .body(database)
                .build();
    }
}
