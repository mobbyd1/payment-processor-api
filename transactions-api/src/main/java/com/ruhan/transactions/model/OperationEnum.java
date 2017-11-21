package com.ruhan.transactions.model;

import com.ruhan.transactions.ApplicationContextProvider;
import com.ruhan.transactions.strategy.OperationStrategy;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

/**
 * Created by ruhandosreis on 20/11/17.
 */
public enum OperationEnum {
    COMPRA_A_VISTA,
    COMPRA_PARCELADA,
    SAQUE,
    PAGAMENTO;

    public Optional<OperationStrategy> getStrategy() {
        final ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        final OperationStrategy bean = applicationContext.getBeansOfType(OperationStrategy.class).get(this.name());

        if( bean == null ) {
            return Optional.empty();
        }

        return Optional.of( bean );
    }
}
