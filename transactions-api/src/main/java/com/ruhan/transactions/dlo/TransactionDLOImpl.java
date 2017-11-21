package com.ruhan.transactions.dlo;

import com.ruhan.transactions.dao.OperationTypeDAO;
import com.ruhan.transactions.dao.PaymentTrackingDAO;
import com.ruhan.transactions.dao.TransactionDAO;
import com.ruhan.transactions.dto.PaymentDTO;
import com.ruhan.transactions.dto.TransactionDTO;
import com.ruhan.transactions.model.OperationEnum;
import com.ruhan.transactions.model.OperationType;
import com.ruhan.transactions.model.PaymentTracking;
import com.ruhan.transactions.model.Transaction;
import com.ruhan.transactions.strategy.OperationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by ruhandosreis on 20/11/17.
 */
@Component
public class TransactionDLOImpl implements TransactionDLO {

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private OperationTypeDAO operationTypeDAO;

    @Autowired
    private PaymentTrackingDAO paymentTrackingDAO;

    @Override
    public List<Transaction> getAllTransactions() {
        final List<Transaction> transactions = new ArrayList<>();
        final Iterable<Transaction> all = transactionDAO.findAll();

        all.forEach( transactions::add );

        return transactions;
    }

    @Override
    public Transaction getTransactionById(Long id) {
        final Transaction transaction = transactionDAO.findOne(id);
        return transaction;
    }

    @Override
    public void registerPayment(PaymentDTO paymentDTO) throws Throwable {
        final Long accountId = paymentDTO.getAccountId();
        final Double amount = paymentDTO.getAmount();

        final List<Transaction> pendingTransactions = transactionDAO.getPendingTransactions(accountId);

        Double totalPayed = 0.0;
        final List<Pair<Long, Double>> tracking = new ArrayList<>();

        for( final Transaction transaction : pendingTransactions ) {

            final Double balance = transaction.getBalance();
            Double currentAmount = amount - totalPayed;

            if( currentAmount == 0.0 ) {
                break;
            }

            final double diff = balance + currentAmount;
            if( diff > 0 ) {
                transaction.setBalance( 0d );
                currentAmount = currentAmount - diff;

            } else {
                transaction.setBalance( diff );
            }

            final Transaction updatedTransaction = transactionDAO.save(transaction);

            final OperationEnum operation = updatedTransaction.getOperationType().getOperation();
            final Optional<OperationStrategy> strategy = operation.getStrategy();

            if( strategy.isPresent() ) {
                strategy.get().execute( accountId, currentAmount );
            }

            tracking.add( Pair.of( transaction.getId(), currentAmount ) );

            totalPayed += currentAmount;
        }

        final Transaction transaction = new Transaction();
        final OperationType paymentOperation
                = operationTypeDAO.findOne(4l);

        transaction.setAccountId( accountId );
        transaction.setOperationType( paymentOperation );
        transaction.setAmount( amount );
        transaction.setBalance( amount - totalPayed );
        transaction.setEventDate( new Date() );
        transaction.setDueDate( new Date() );

        final Transaction savedPayment = transactionDAO.save(transaction);

        for( final Pair<Long, Double> track : tracking ) {
            final Long transactionId = track.getFirst();
            final Double transactionAmount = track.getSecond();

            final PaymentTracking paymentTracking = new PaymentTracking();
            paymentTracking.setCreditTransactionId( savedPayment.getId() );
            paymentTracking.setDebitTransactionId( transactionId );
            paymentTracking.setAmount( transactionAmount );

            paymentTrackingDAO.save( paymentTracking );
        }
    }

    @Override
    public void registerTransaction(TransactionDTO transactionDTO) throws Throwable {

        final Long accountId = transactionDTO.getAccountId();
        final Long operationTypeId = transactionDTO.getOperationTypeId();
        final Double amount = transactionDTO.getAmount();

        final OperationType operationType = operationTypeDAO.findOne(operationTypeId);
        final OperationEnum operation = operationType.getOperation();

        if ( OperationEnum.PAGAMENTO.equals( operationType.getOperation() ) ) {
            final PaymentDTO paymentDTO = new PaymentDTO();

            paymentDTO.setAccountId( accountId );
            paymentDTO.setAmount( amount );

            registerPayment( paymentDTO );

        } else {

            final Transaction transaction = new Transaction();
            transaction.setAccountId( accountId );
            transaction.setOperationType( operationType );
            transaction.setAmount( amount * -1 );
            transaction.setBalance( amount * -1 );
            transaction.setEventDate( new Date() );
            transaction.setDueDate( new Date() );

            transactionDAO.save( transaction );

            Optional<OperationStrategy> strategy = operation.getStrategy();
            if( strategy.isPresent() ) {
                strategy.get().execute( accountId, amount * -1 );
            }
        }

    }
}
