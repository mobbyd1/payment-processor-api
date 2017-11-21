package com.ruhan.transactions.controller;

import com.ruhan.transactions.dlo.TransactionDLO;
import com.ruhan.transactions.dto.PaymentDTO;
import com.ruhan.transactions.dto.TransactionDTO;
import com.ruhan.transactions.model.OperationEnum;
import com.ruhan.transactions.model.OperationType;
import com.ruhan.transactions.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ruhandosreis on 20/11/17.
 */
@Controller
@RequestMapping("/v1")
public class TransactionController extends AbstractControllerWithErrorHandling {

    @Autowired
    private TransactionDLO transactionDLO;

    private static String API_DATE_FORMAT = "yyyy-mm-dd";

    @RequestMapping(value="/transactions", method = RequestMethod.GET)
    public ResponseEntity getAll() {

        final List<TransactionDTO> transactionDTOS = new ArrayList<>();
        final List<Transaction> transactions = transactionDLO.getAllTransactions();

        final DateFormat dateFormat = new SimpleDateFormat(API_DATE_FORMAT);

        for( final Transaction transaction : transactions ) {
            final TransactionDTO transactionDTO = new TransactionDTO();

            final Long id = transaction.getId();
            transactionDTO.setId( id );

            final Long accountId = transaction.getAccountId();
            transactionDTO.setAccountId( accountId );

            final OperationType operationType = transaction.getOperationType();
            transactionDTO.setOperation( operationType.getOperation().name() );
            transactionDTO.setOperationTypeId( operationType.getId() );

            final Double amount = transaction.getAmount();
            transactionDTO.setAmount( amount );

            final Double balance = transaction.getBalance();
            transactionDTO.setBalance( balance );

            final Date eventDate = transaction.getEventDate();
            final String eventDateStr = dateFormat.format( eventDate );

            transactionDTO.setEventDate( eventDateStr );

            final Date dueDate = transaction.getDueDate();
            final String dueDateStr = dateFormat.format(dueDate);

            transactionDTO.setDueDate( dueDateStr );

            transactionDTOS.add( transactionDTO );
        }

        return new ResponseEntity( transactionDTOS, HttpStatus.OK );
    }

    @RequestMapping(value = "/payments", method = RequestMethod.POST)
    public ResponseEntity registerPayment(@RequestBody List<PaymentDTO> paymentDTOs) {

        try {
            for (final PaymentDTO paymentDTO : paymentDTOs) {
                transactionDLO.registerPayment(paymentDTO);
            }
        } catch ( Throwable e ) {
            return new ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR );
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody TransactionDTO transactionDTO) {

        try {
            transactionDLO.registerTransaction(transactionDTO);
        } catch ( Throwable e ) {
            return new ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR );
        }

        return new ResponseEntity( HttpStatus.OK );
    }

    @RequestMapping(value = "/transactions/operations", method = RequestMethod.GET)
    public ResponseEntity getAvailableOperations() {
        final OperationEnum[] values = OperationEnum.values();
        return new ResponseEntity( values, HttpStatus.OK );
    }
}
