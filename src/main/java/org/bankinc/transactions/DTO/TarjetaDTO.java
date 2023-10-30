package org.bankinc.transactions.DTO;

import lombok.Data;

@Data
public class TarjetaDTO {
    private Long id;
    private String titular;
    private String tipo;
    private String telefono;
    private String estado;
    private String panEnmascarado;
    private String pan;

}
