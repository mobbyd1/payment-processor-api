package com.ruhan.accounts.controller;

import com.ruhan.accounts.dlo.AccountDLO;
import com.ruhan.accounts.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ruhandosreis on 19/11/17.
 */
@Controller
@RequestMapping("/v1")
public class AccountController extends AbstractControllerWithErrorHandling {

    @Autowired
    private AccountDLO accountDLO;

    @RequestMapping(value = "/accounts/limits", method = RequestMethod.GET)
    public ResponseEntity getAll() {
        final List<Account> all = accountDLO.getAll();

        final List<AccountDTO> dtos = new ArrayList<>();
        for( final Account account : all ) {

            final AccountDTO dto = new AccountDTO();

            final Long id = account.getId();
            final Double availableCreditLimit = account.getAvailableCreditLimit();
            final Double availableWithdrawLimit = account.getAvailableWithdrawLimit();

            dto.setId( id );
            dto.setAvailableCreditLimit( availableCreditLimit );
            dto.setAvailableWithdrawLimit( availableWithdrawLimit );

            dtos.add( dto );
        }

        return new ResponseEntity(dtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.PATCH)
    public ResponseEntity updateLimits(@PathVariable Long id, @RequestBody Map<String, Map> limits) {
        final String availableCreditLimitStr = ( String ) limits
                    .get("available_credit_limit")
                    .get("amount")
                    .toString();

        final String availableWithdrawLimitStr = ( String ) limits
                    .get("available_withdraw_limit")
                    .get("amount")
                    .toString();

        accountDLO.updateLimits( id
                    , Double.valueOf( availableCreditLimitStr )
                    , Double.valueOf( availableWithdrawLimitStr ) );

        final Account account = accountDLO.getById(id);

        final Double availableWithdrawLimit = account.getAvailableWithdrawLimit();
        final Double availableCreditLimit = account.getAvailableCreditLimit();

        final AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId( id );
        accountDTO.setAvailableCreditLimit( availableCreditLimit );
        accountDTO.setAvailableWithdrawLimit( availableWithdrawLimit );

        return new ResponseEntity( accountDTO, HttpStatus.OK );
    }
}
