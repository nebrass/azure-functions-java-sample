package com.helloworld;

import java.util.Random;

public class FunctionsUtils {
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String EMPTY = "empty";
    public static final String DOCUMENT_TO_BE_SAVED = "Document to be saved in DB: %s";
    public static final String JAVA_HTTP_TRIGGER_PROCESSED_A_REQUEST = "Java HTTP trigger processed a request.";
    public static final String DOCUMENT_CREATED_SUCCESSFULLY = "Document created successfully in DB.";
    public static final String DOCUMENT_TO_BE_SENT_TO_QUEUE = "Document to be sent to Queue: %s";
    public static final String SERVICE_BUS_MESSAGE_TRIGGER_PROCESSED_A_REQUEST = "Service Bus message trigger processed a request: %s";

    public static int randomLong() {
        return Math.abs(new Random().nextInt());
    }
}
