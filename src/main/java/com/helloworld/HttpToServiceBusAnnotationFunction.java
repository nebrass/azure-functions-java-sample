package com.helloworld;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.ServiceBusQueueOutput;
import org.json.JSONObject;

import java.util.Optional;
import java.util.Random;

import static com.helloworld.FunctionsUtils.EMPTY;

/**
 * Azure Functions with HTTP Trigger and CosmosDB output.
 */
public class HttpToServiceBusAnnotationFunction {
    @FunctionName("ServiceBusStoreAnnotation")
    @ServiceBusQueueOutput(
            name = "hello-world-app-queue-output",
            queueName = "hello-world-app-queue",
            connection = "ServiceBusConnection")
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
        final String id = String.valueOf(new Random().nextInt());

        // Generate Student
        Student student = new Student(id, name, email);
        final String message = student.toString();

        context.getLogger().info(String.format("Document to be sent to Queue: %s", message));

        return message;
    }
}
