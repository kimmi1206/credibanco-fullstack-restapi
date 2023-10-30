package org.bankinc.transactions.response;

import lombok.Data;

@Data
public class EnrolarResponse {
    private String codigoRespuesta;
    private String mensaje;
    private String panEnmascarado;
}
