package org.bankinc.transactions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tarjetas")
public class Tarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pan", length = 19, nullable = false, unique = true)
    private String pan;

    @Column(name = "titular", nullable = false)
    private String titular;

    @Column(name = "cedula", length = 15, nullable = false)
    private String cedula;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "telefono", length = 10, nullable = false)
    private String telefono;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "numero_validacion", nullable = false)
    private int numeroValidacion;

    @Column(name = "pan_enmascarado", nullable = false)
    private String panEnmascarado;

}
