package com.helloworld;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.ServiceBusQueueTrigger;
import org.json.JSONObject;

import static com.helloworld.FunctionsUtils.DOCUMENT_TO_BE_SAVED;

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
        context.getLogger().info(String.format(FunctionsUtils.SERVICE_BUS_MESSAGE_TRIGGER_PROCESSED_A_REQUEST, student));

        String jsonValue = new JSONObject(student).toString();

        context.getLogger().info(String.format(DOCUMENT_TO_BE_SAVED, jsonValue));

        outputItem.setValue(jsonValue);
    }
}
