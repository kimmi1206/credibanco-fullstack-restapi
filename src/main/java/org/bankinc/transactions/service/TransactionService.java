package org.bankinc.transactions.service;

import org.bankinc.transactions.model.Tarjeta;
import org.bankinc.transactions.model.Transaction;
import org.bankinc.transactions.repository.TarjetaRepository;
import org.bankinc.transactions.repository.TransactionRepository;
import org.bankinc.transactions.response.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TarjetaRepository tarjetaRepository;

    public TransactionResponse crearTransaccion(String pan, String referencia, BigDecimal totalCompra,
            String direccionCompra) {
        TransactionResponse response = new TransactionResponse();
        Optional<Tarjeta> tarjetaOptional = tarjetaRepository.findByPan(pan);

        if (tarjetaOptional.isPresent()) {
            Tarjeta tarjeta = tarjetaOptional.get();
            if (tarjeta.getEstado().equals("Enrolada")) {
                Transaction transaction = new Transaction();
                transaction.setTarjeta(tarjeta);
                transaction.setReferencia(referencia);
                transaction.setTotalCompra(totalCompra);
                transaction.setDireccionCompra(direccionCompra);
                transaction.setFechaCreacion(LocalDateTime.now());
                transaction.setFechaActualizacion(LocalDateTime.now());
                transaction.setEstado("Aprobada");
                transactionRepository.save(transaction);
                response.setCodigoRespuesta("00");
                response.setMensaje("Compra exitosa");
                response.setEstadoTransaccion("Aprobada");
                response.setNumeroReferencia(referencia);
            } else {
                response.setCodigoRespuesta("01");
                response.setMensaje("Tarjeta no enrolada");
            }
        } else {
            response.setCodigoRespuesta("02");
            response.setMensaje("Tarjeta no existe");
        }

        return response;
    }

    public List<Transaction> buscarTransaccionesPorPan(String pan) {
        Optional<Tarjeta> tarjetaOptional = tarjetaRepository.findByPan(pan);
        return transactionRepository.findByTarjeta(tarjetaOptional.get());

    }

    public List<Transaction> buscarTransaccionesPorTarjetaId(Long id) {
        Optional<Tarjeta> tarjetaOptional = tarjetaRepository.findTarjetaById(id);

        return transactionRepository.findByTarjeta(tarjetaOptional.get());

    }

    public TransactionResponse anularTransaccion(String pan, String referencia, BigDecimal totalCompra) {
        TransactionResponse response = new TransactionResponse();
        Optional<Transaction> transactionOptional = transactionRepository.findByTarjetaPanAndReferencia(pan,
                referencia);

        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            LocalDateTime fechaCreacion = transaction.getFechaCreacion();
            LocalDateTime fechaActual = LocalDateTime.now();
            long minutosTranscurridos = ChronoUnit.MINUTES.between(fechaCreacion, fechaActual);

            if (minutosTranscurridos <= 5) {
                BigDecimal totalCompraTransaccion = transaction.getTotalCompra();

                if (totalCompra.compareTo(totalCompraTransaccion) == 0) {
                    transaction.setEstado("Anulada");
                    transactionRepository.save(transaction);
                    response.setCodigoRespuesta("00");
                    response.setMensaje("Compra anulada");
                    response.setNumeroReferencia(referencia);
                } else {
                    response.setCodigoRespuesta("01");
                    response.setMensaje("Número de referencia inválido");
                }
            } else {
                response.setCodigoRespuesta("02");
                response.setMensaje("No se puede anular transacción");
            }
        } else {
            response.setCodigoRespuesta("01");
            response.setMensaje("Número de referencia inválido");
        }

        return response;
    }
}
