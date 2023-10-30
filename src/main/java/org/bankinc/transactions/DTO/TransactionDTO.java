package org.bankinc.transactions.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDTO {
    private String pan;
    private String panEnmascarado;
    private String referencia;
    private BigDecimal totalCompra;
    private String direccionCompra;
    private String estado;

}
