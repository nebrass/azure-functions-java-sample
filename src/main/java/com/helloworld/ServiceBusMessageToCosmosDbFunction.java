package com.helloworld;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.ServiceBusQueueTrigger;
import org.json.JSONObject;

public class ServiceBusMessageToCosmosDbFunction {

    @FunctionName("ServiceBusMessageToCosmosDb")
    public void run(
            @ServiceBusQueueTrigger(
                    name = "messageTrigger",
                    queueName = "hello-world-app-queue",
                    connection = "ServiceBusConnection"
            ) String student,
            @CosmosDBOutput(
                    name = "database",
                    databaseName = "university",
                    collectionName = "students",
                    connectionStringSetting = "CosmosDbConnection")
                    OutputBinding<String> outputItem,
            final ExecutionContext context) {
        context.getLogger().info(String.format("Service Bus message trigger processed a request: %s", student));

        // This line will be used to validate the received JSON
        String jsonValue = new JSONObject(student).toString();

        context.getLogger().info(String.format("Document to be saved in DB: %s", jsonValue));

        outputItem.setValue(jsonValue);
    }
}
