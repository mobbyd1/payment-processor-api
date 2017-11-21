package com.ruhan.transactions.dlo;

import com.google.gson.Gson;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruhandosreis on 20/11/17.
 */
@Component
public class AccountDLOImpl implements AccountDLO {

    public void updateLimits(Long accountId, double credit, double withdrawal) throws IOException {
        final String postUrl = "http://accounts-api:8082/v1/accounts/" + accountId;// put in your url

        final HttpClient httpClient = HttpClientBuilder.create().build();
        final HttpPatch put = new HttpPatch(postUrl);

        final Map<String, Map> outerMap = new HashMap<>();
        final Map<String, Double> innerMapCreditLimit = new HashMap<>();
        final Map<String, Double> innerMapWithdrawLimit = new HashMap<>();

        innerMapCreditLimit.put("amount", credit);
        outerMap.put("available_credit_limit", innerMapCreditLimit);

        innerMapWithdrawLimit.put("amount", withdrawal);
        outerMap.put("available_withdraw_limit", innerMapWithdrawLimit);

        final Gson gson = new Gson();
        final String json = gson.toJson(outerMap);

        final StringEntity postingString = new StringEntity( json );
        put.setEntity(postingString);
        put.setHeader("Content-type", "application/json");

        httpClient.execute(put);
    }
}
