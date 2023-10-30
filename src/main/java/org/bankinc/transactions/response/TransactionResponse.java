package org.bankinc.transactions.response;

import lombok.Data;

@Data
public class TransactionResponse {

    private String codigoRespuesta;
    private String mensaje;
    private String estadoTransaccion;
    private String numeroReferencia;

    // getters and setters
}
