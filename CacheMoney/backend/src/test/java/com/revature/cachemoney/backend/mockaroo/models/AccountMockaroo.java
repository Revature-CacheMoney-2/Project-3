package com.revature.cachemoney.backend.mockaroo.models;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.cachemoney.backend.beans.models.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.io.IOException;

public class AccountMockaroo {


    /**
     * This test ensures the date retrieved from the Mockaroo Mock API is in JSON format.
     * @throws IOException this is thrown if no data is retrieved.
     */
    @Test
    void givenResponseContainsJson() throws IOException {
        String jsonMimeType = "application/json";
        HttpUriRequest request = new HttpGet("https://my.api.mockaroo.com/user.json?key=a0f3f440");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        Assertions.assertEquals(jsonMimeType, mimeType);
    }

    /**
     * This test accesses the supplied Mockaroo Mock API endpoint,
     * ensuring a user can be retried and added to a User object.
     * @throws IOException thrown if there is an issue with data retrieval.
     */
    @Test
    void getGetUserById() throws IOException {

        //Player ronaldo = new ObjectMapper().readValue(jsonString, Player.class);
        HttpUriRequest request = new HttpGet("https://my.api.mockaroo.com/user/123.json?key=a0f3f440");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        String jsonFromResponse = EntityUtils.toString(response.getEntity());

        User users = new ObjectMapper().readValue(jsonFromResponse, User.class);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser allUsers = jsonFactory.createParser(jsonFromResponse);


    }

}