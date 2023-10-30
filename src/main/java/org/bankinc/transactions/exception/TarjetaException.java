package org.bankinc.transactions.exception;

import lombok.Data;

@Data
public class TarjetaException extends RuntimeException {

    private String codigo;
    private String mensaje;

    private String pan;

    public TarjetaException(String mensaje, String codigo, String pan) {
        super(mensaje);
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.pan = pan;
    }

}
