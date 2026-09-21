package br.com.zenon;

import br.com.zenon.enums.TransactionTypeEnum;

import java.math.BigDecimal;

public record Transaction(
        int step,
        TransactionTypeEnum type,
        BigDecimal amount,
        TransactionCustomer origin,
        TransactionCustomer recipient,
        Boolean isFraud,
        Boolean isFlaggedFraud
) {
}
