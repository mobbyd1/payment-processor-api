package com.ruhan.transactions;

import com.google.gson.Gson;
import com.ruhan.transactions.dao.OperationTypeDAO;
import com.ruhan.transactions.dao.PaymentTrackingDAO;
import com.ruhan.transactions.dao.TransactionDAO;
import com.ruhan.transactions.dlo.AccountDLO;
import com.ruhan.transactions.dto.TransactionDTO;
import com.ruhan.transactions.model.OperationEnum;
import com.ruhan.transactions.model.OperationType;
import com.ruhan.transactions.model.PaymentTracking;
import com.ruhan.transactions.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ruhandosreis on 20/11/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ApplicationTests {

    @MockBean
    AccountDLO accountDLO;

    @Autowired
    OperationTypeDAO operationTypeDAO;

    @Autowired
    PaymentTrackingDAO paymentTrackingDAO;

    @Autowired
    TransactionDAO transactionDAO;

    @Autowired
    MockMvc mockMvc;

    @Before
    public void init() throws Exception {

        paymentTrackingDAO.deleteAll();
        transactionDAO.deleteAll();
        operationTypeDAO.deleteAll();

        final OperationType compraAvista = new OperationType();
        compraAvista.setId(1l);
        compraAvista.setChargeOrder(2);
        compraAvista.setOperation(OperationEnum.COMPRA_A_VISTA);

        final OperationType compraParcelada = new OperationType();
        compraParcelada.setId(2l);
        compraParcelada.setChargeOrder(1);
        compraParcelada.setOperation(OperationEnum.COMPRA_PARCELADA);

        final OperationType saque = new OperationType();
        saque.setId(3l);
        saque.setChargeOrder(0);
        saque.setOperation(OperationEnum.SAQUE);

        final OperationType pagamento = new OperationType();
        pagamento.setId(4l);
        pagamento.setChargeOrder(0);
        pagamento.setOperation(OperationEnum.PAGAMENTO);

        operationTypeDAO.save( compraAvista );
        operationTypeDAO.save( compraParcelada );
        operationTypeDAO.save( saque );
        operationTypeDAO.save( pagamento );

        Mockito.doNothing().when( accountDLO ).updateLimits(Mockito.anyLong(), Mockito.anyDouble(), Mockito.anyDouble());
    }

    @Test
    public void testGetTransactions() throws Exception {

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

        final Date eventDate = dateFormat.parse("2017-01-01");
        final Date dueDate = dateFormat.parse("2017-02-01");

        final Transaction transaction = new Transaction();
        transaction.setId(1l);
        transaction.setAccountId(1l);

        final OperationType operation = operationTypeDAO.findOne(2l);
        transaction.setOperationType(operation);

        transaction.setAmount(-5000d);
        transaction.setBalance(-4000d);
        transaction.setDueDate(dueDate);
        transaction.setEventDate(eventDate);

        transactionDAO.save( transaction );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/transactions"))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$", hasSize(1) ) )
                .andExpect( jsonPath( "$[0].account_id", is( 1 ) ) )
                .andExpect( jsonPath( "$[0].operation_type_id", is( 2 ) ) )
                .andExpect( jsonPath( "$[0].amount", is( -5000.0 ) ) )
                .andExpect( jsonPath( "$[0].balance", is( -4000.0 ) ) )
                .andExpect( jsonPath( "$[0].event_date", is( "2017-01-01" ) ) )
                .andExpect( jsonPath( "$[0].due_date", is( "2017-02-01" ) ) );

        paymentTrackingDAO.deleteAll();
        transactionDAO.deleteAll();
    }

    @Test
    public void testAddTransaction() throws Exception {
        final Map<String, Object> map = new HashMap<>();

        map.put("account_id", 1l);
        map.put("operation_type_id", 2l);
        map.put("amount", 100d);

        final Gson gson = new Gson();
        final String json = gson.toJson(map);

        mockMvc.perform( MockMvcRequestBuilders.post("/v1/transactions")
                        .contentType( MediaType.APPLICATION_JSON ).content( json ) )
                        .andExpect( status().isOk() );

        final List<Transaction> transactions = new ArrayList<>();
        transactionDAO.findAll().forEach(transactions::add);

        final Transaction transaction = transactions.get(0);

        assertEquals( Long.valueOf(1l), transaction.getAccountId() );
        assertEquals( Long.valueOf(2l), transaction.getOperationType().getId() );
        assertEquals( Double.valueOf( -100d ), transaction.getAmount());
        assertEquals( Double.valueOf( -100d ), transaction.getBalance());

        paymentTrackingDAO.deleteAll();
        transactionDAO.deleteAll();
    }

    @Test
    public void testPayment() throws Exception {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

        final Date eventDate1 = dateFormat.parse("2017-01-01");
        final Date dueDate1 = dateFormat.parse("2017-02-01");

        final Date eventDate2 = dateFormat.parse("2016-01-01");
        final Date dueDate2 = dateFormat.parse("2017-02-01");

        final Transaction transaction1 = new Transaction();
        transaction1.setId(1l);
        transaction1.setAccountId(1l);

        final OperationType compraParcelada = operationTypeDAO.findOne(2l);
        transaction1.setOperationType(compraParcelada);

        transaction1.setAmount(-5000d);
        transaction1.setBalance(-5000d);
        transaction1.setDueDate(dueDate1);
        transaction1.setEventDate(eventDate1);

        final Transaction transaction2 = new Transaction();
        transaction2.setId(2l);
        transaction2.setAccountId(1l);

        final OperationType operation = operationTypeDAO.findOne(2l);
        transaction2.setOperationType(operation);

        transaction2.setAmount(-100d);
        transaction2.setBalance(-100d);
        transaction2.setDueDate(dueDate2);
        transaction2.setEventDate(eventDate2);

        transactionDAO.save( transaction1 );
        transactionDAO.save( transaction2 );

        final Map<String, Object> payment = new HashMap<>();
        payment.put("account_id", 1);
        payment.put("amount", 200);

        final Gson gson = new Gson();
        final String json = gson.toJson(Arrays.asList(payment));

        mockMvc.perform( MockMvcRequestBuilders.post("/v1/payments")
                .contentType( MediaType.APPLICATION_JSON ).content( json ) )
                .andExpect( status().isOk() );

        final Transaction t1 = transactionDAO.findOne(1l);
        assertEquals( Double.valueOf(-4900), t1.getBalance() );

        final Transaction t2 = transactionDAO.findOne(2l);
        assertEquals( Double.valueOf(0), t2.getBalance() );

        final PaymentTracking pt1 = paymentTrackingDAO.findOne(1l);
        assertEquals( Long.valueOf(3l), pt1.getCreditTransactionId() );
        assertEquals( Long.valueOf(2l), pt1.getDebitTransactionId() );
        assertEquals( Double.valueOf(100), pt1.getAmount() );

        final PaymentTracking pt2 = paymentTrackingDAO.findOne(2l);
        assertEquals( Long.valueOf(3l), pt2.getCreditTransactionId() );
        assertEquals( Long.valueOf(1l), pt2.getDebitTransactionId() );
        assertEquals( Double.valueOf(100), pt2.getAmount() );

        paymentTrackingDAO.deleteAll();
        transactionDAO.deleteAll();
    }
}
