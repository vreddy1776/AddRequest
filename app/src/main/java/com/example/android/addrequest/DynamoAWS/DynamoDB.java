package com.example.android.addrequest.DynamoAWS;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import com.amazonaws.models.nosql.RequestsDO;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.example.android.addrequest.AddTicketActivity;
import com.google.gson.Gson;


import java.io.File;
import java.util.Random;

public class DynamoDB {

    // Constant for logging
    private static final String TAG = DynamoDB.class.getSimpleName();

    // Declare a DynamoDBMapper object
    DynamoDBMapper dynamoDBMapper;

    public void accessDynamoDB(Context context) {

        AWSMobileClient.getInstance().initialize(context).execute();
        commDynamoDB(context);

    }


    private void commDynamoDB(final Context context) {


        // AWSMobileClient enables AWS user credentials to access your table
        AWSMobileClient.getInstance().initialize(context).execute();

        AWSCredentialsProvider credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();


        // Add code to instantiate a AmazonDynamoDBClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(configuration)
                .build();

        // other activity code ...

    }


    public void readTicket(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                RequestsDO ticket = dynamoDBMapper.load(
                        RequestsDO.class,
                        id);

                // Item read
                Log.d(TAG, ticket.toString());

            }
        }).start();
    }


    public void createTicket(int id, String title, String description, String date) {

        final RequestsDO ticket = new RequestsDO();

        ticket.setId((double) id);
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setDate(date);

        Log.d(TAG, ticket.toString());


        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(ticket);
                // Item saved
            }
        }).start();
    }


    public void queryNews() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                //RequestsDO ticket = new RequestsDO();
                //ticket.setId((double) id);
                //ticket.setArticleId("Article1");


                Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
                        .withAttributeValueList(new AttributeValue().withS("Trial"));

                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        //.withHashKeyValues(note)
                        .withRangeKeyCondition("id", rangeKeyCondition)
                        .withConsistentRead(false);

                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

                //PaginatedList<RequestsDO> result = dynamoDBMapper.query(RequestsDO.class, queryExpression);
                PaginatedList<RequestsDO> result = dynamoDBMapper.scan(RequestsDO.class, scanExpression);


                Gson gson = new Gson();
                StringBuilder stringBuilder = new StringBuilder();

                // Loop through query results
                for (int i = 0; i < result.size(); i++) {
                    String jsonFormOfItem = gson.toJson(result.get(i));
                    stringBuilder.append(jsonFormOfItem + "\n\n");
                }

                // Add your code here to deal with the data result
                Log.d(TAG, stringBuilder.toString());

                if (result.isEmpty()) {
                    // There were no items matching your query.
                }
            }
        }).start();
    }


}
