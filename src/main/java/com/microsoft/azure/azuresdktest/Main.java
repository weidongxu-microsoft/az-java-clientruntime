package com.microsoft.azure.azuresdktest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.rest.LogLevel;
import com.microsoft.rest.RestClient;
import com.microsoft.rest.ServiceResponseBuilder;
import com.microsoft.rest.credentials.TokenCredentials;
import com.microsoft.rest.serializer.JacksonAdapter;
import okhttp3.Request;
import okhttp3.Response;

public class Main {

    public static void main(String args[]) {
        try {
            TokenCredentials credentials = new ApplicationTokenCredentials(
                    System.getenv("AZURE_CLIENT_ID"),
                    System.getenv("AZURE_TENANT_ID"),
                    System.getenv("AZURE_CLIENT_SECRET"),
                    AzureEnvironment.AZURE
            );

            RestClient restClient = new RestClient.Builder()
                    .withBaseUrl("https://management.azure.com/")
                    .withCredentials(credentials)
                    .withResponseBuilderFactory(new ServiceResponseBuilder.Factory())
                    .withSerializerAdapter(new JacksonAdapter())
                    .withLogLevel(LogLevel.BODY_AND_HEADERS)
                    .build();

            Response response = restClient.httpClient().newCall(new Request.Builder()
                    .url("https://management.azure.com/subscriptions/ec0aa5f7-9e78-40c9-85cd-535c6305b380/resourceGroups/azfluent/providers/Microsoft.RecoveryServices/vaults/azfluent/backupJobs?api-version=2021-12-01")
                    .get()
                    .build()).execute();

            String responseBody = response.body().string();
            System.out.println(responseBody);
            // after getting the string, user will need to find a JSON parser to parse it
            // the lib includes Jackson, but user can use whatever they want
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // parse error.code
            // change it for good response
            System.out.println(jsonNode.get("error").get("code").asText());

            System.exit(0);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
