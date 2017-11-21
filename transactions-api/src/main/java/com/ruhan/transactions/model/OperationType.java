package com.ruhan.transactions.model;

import javax.persistence.*;

/**
 * Created by ruhandosreis on 20/11/17.
 */
@Entity
@Table(name = "OPERATION_TYPES", schema = "transaction")
public class OperationType {

    @Id
    @Column(name = "operation_type_id")
    private Long id;

    @Column(name = "description")
    @Enumerated(EnumType.STRING)
    private OperationEnum operation;

    @Column(name = "charge_order")
    private int chargeOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationEnum getOperation() {
        return operation;
    }

    public void setOperation(OperationEnum operation) {
        this.operation = operation;
    }

    public int getChargeOrder() {
        return chargeOrder;
    }

    public void setChargeOrder(int chargeOrder) {
        this.chargeOrder = chargeOrder;
    }
}
