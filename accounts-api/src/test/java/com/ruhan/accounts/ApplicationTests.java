package com.ruhan.accounts;

import com.google.gson.Gson;
import com.ruhan.accounts.dao.AccountDAO;
import com.ruhan.accounts.model.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by ruhandosreis on 19/11/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ApplicationTests {

    @Autowired
    AccountDAO dao;

    @Autowired
    MockMvc mockMvc;

    @Before
    public void init() {
        final Account account1 = new Account();

        account1.setId(Long.valueOf(1));
        account1.setAvailableWithdrawLimit( 10d);
        account1.setAvailableCreditLimit( 1d );

        final Account account2 = new Account();

        account2.setId(Long.valueOf(2));
        account2.setAvailableWithdrawLimit( 0d );
        account2.setAvailableCreditLimit( 10d );

        dao.save( account1 );
        dao.save( account2 );
    }

    @Test
    public void testGetAll() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/accounts/limits"))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$", hasSize(2)) )
                .andExpect( jsonPath("$[0].id", is(1)) )
                .andExpect( jsonPath("$[0].availableCreditLimit", is(1d)) )
                .andExpect( jsonPath("$[0].availableWithdrawLimit", is(10d)) )
                .andExpect( jsonPath("$[1].id", is(2)) )
                .andExpect( jsonPath("$[1].availableCreditLimit", is(10d)) )
                .andExpect( jsonPath("$[1].availableWithdrawLimit", is(0d)) );


    }

    @Test
    public void testPatchAdd() throws Exception {
        final Map<String, Map> outerMap = new HashMap<>();
        final Map<String, Double> innerMapCreditLimit = new HashMap<>();
        final Map<String, Double> innerMapWithdrawLimit = new HashMap<>();

        innerMapCreditLimit.put("amount", 100d);
        outerMap.put("available_credit_limit", innerMapCreditLimit);

        innerMapWithdrawLimit.put("amount", 1d);
        outerMap.put("available_withdraw_limit", innerMapWithdrawLimit);

        final Gson gson = new Gson();
        final String json = gson.toJson(outerMap);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/v1/accounts/1")
                .contentType( MediaType.APPLICATION_JSON ).content( json ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(1 ) ) )
                .andExpect( jsonPath( "$.availableCreditLimit", is( 101d ) ) )
                .andExpect( jsonPath( "$.availableWithdrawLimit", is( 11d ) ) );

    }

    @Test
    public void testPatchSub() throws Exception {
        final Map<String, Map> outerMap = new HashMap<>();
        final Map<String, Double> innerMapCreditLimit = new HashMap<>();
        final Map<String, Double> innerMapWithdrawLimit = new HashMap<>();

        innerMapCreditLimit.put("amount", -10d);
        outerMap.put("available_credit_limit", innerMapCreditLimit);

        innerMapWithdrawLimit.put("amount", -100d);
        outerMap.put("available_withdraw_limit", innerMapWithdrawLimit);

        final Gson gson = new Gson();
        final String json = gson.toJson(outerMap);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/v1/accounts/2")
                        .contentType( MediaType.APPLICATION_JSON ).content( json ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(2 ) ) )
                .andExpect( jsonPath( "$.availableCreditLimit", is( 0d ) ) )
                .andExpect( jsonPath( "$.availableWithdrawLimit", is( -100d ) ) );
    }

}
