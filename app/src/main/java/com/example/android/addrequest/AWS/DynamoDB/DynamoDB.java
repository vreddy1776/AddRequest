package com.example.android.addrequest.AWS.DynamoDB;

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

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class DynamoDB {

    // Constant for logging
    private static final String TAG = DynamoDB.class.getSimpleName();

    // Declare a DynamoDBMapper object
    DynamoDBMapper dynamoDBMapper;


    public void commDynamoDB(final Context context) {


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


    public void createTicket(int id, String userID, String title, String description, String date) {

        final RequestsDO ticket = new RequestsDO();

        ticket.setId((double) id);
        ticket.setUserID(userID);
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


    public void scanTickets(final Context context) {

        new Thread(new Runnable() {
            @Override
            public void run() {

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
                stringBuilder.append("[");

                int length = result.size();
                // Loop through query results
                for (int i = 0; i < length; i++) {
                    String jsonFormOfItem = gson.toJson(result.get(i));
                    stringBuilder.append(jsonFormOfItem);
                    if (i < length - 1){
                        stringBuilder.append(",");
                    }
                }
                stringBuilder.append("]");


                // Add your code here to deal with the data result
                Log.d(TAG, stringBuilder.toString());

                DynamoSyncBulk syncBulk = new DynamoSyncBulk();
                syncBulk.bulkPopulate(context,stringBuilder.toString());

                if (result.isEmpty()) {
                    // There were no items matching your query.
                }
            }
        }).start();
    }


    public void scanTicketsWithUserID(final Context context, final String userID) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
                        .withAttributeValueList(new AttributeValue().withS("Trial"));

                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        //.withHashKeyValues(note)
                        .withRangeKeyCondition("id", rangeKeyCondition)
                        .withConsistentRead(false);

                Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
                eav.put(":x", new AttributeValue().withS(userID));

                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                        .withFilterExpression("userID = :x")
                        .withExpressionAttributeValues(eav);

                //PaginatedList<RequestsDO> result = dynamoDBMapper.query(RequestsDO.class, queryExpression);
                PaginatedList<RequestsDO> result = dynamoDBMapper.scan(RequestsDO.class, scanExpression);


                Gson gson = new Gson();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[");

                int length = result.size();
                // Loop through query results
                for (int i = 0; i < length; i++) {
                    String jsonFormOfItem = gson.toJson(result.get(i));
                    stringBuilder.append(jsonFormOfItem);
                    if (i < length - 1){
                        stringBuilder.append(",");
                    }
                }
                stringBuilder.append("]");


                // Add your code here to deal with the data result
                Log.d(TAG, stringBuilder.toString());

                DynamoSyncBulk syncBulk = new DynamoSyncBulk();
                syncBulk.bulkPopulate(context,stringBuilder.toString());

                if (result.isEmpty()) {
                    // There were no items matching your query.
                }
            }
        }).start();
    }


    public void deleteTicket(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                RequestsDO ticket = new RequestsDO();

                ticket.setId((double) id);    //partition key

                dynamoDBMapper.delete(ticket);

                // Item deleted
            }
        }).start();
    }


    public void updateTicket(int id, String title, String description, String date) {
        final RequestsDO ticket = new RequestsDO();

        ticket.setId((double) id);
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setDate(date);

        new Thread(new Runnable() {
            @Override
            public void run() {

                dynamoDBMapper.save(ticket);

                // Item updated
            }
        }).start();
    }


}
