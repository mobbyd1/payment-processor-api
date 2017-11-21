package com.ruhan.transactions.dlo;

import java.io.IOException;

/**
 * Created by ruhandosreis on 20/11/17.
 */
public interface AccountDLO {

    void updateLimits( Long accountId, double credit, double withdrawal ) throws IOException;
}
