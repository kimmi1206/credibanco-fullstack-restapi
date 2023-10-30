package org.bankinc.transactions.controller;

import org.bankinc.transactions.DTO.TransactionDTO;
import org.bankinc.transactions.model.Transaction;
import org.bankinc.transactions.response.TransactionResponse;
import org.bankinc.transactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transacciones")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/crear")
    public ResponseEntity<TransactionResponse> crearTransaccion(@RequestBody TransactionDTO request) {
        TransactionResponse response = transactionService.crearTransaccion(request.getPan(), request.getReferencia(),
                request.getTotalCompra(), request.getDireccionCompra());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/anular")
    public ResponseEntity<TransactionResponse> anularTransaccion(@RequestBody TransactionDTO request) {
        TransactionResponse response = transactionService.anularTransaccion(request.getPan(), request.getReferencia(),
                request.getTotalCompra());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transacciones/tarjeta-pan/{pan}")
    public ResponseEntity<List<TransactionDTO>> obtenerTransaccionesPorPan(@PathVariable String pan) {

        return ResponseEntity.ok(transactionService.buscarTransaccionesPorPan(pan)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/transacciones/tarjeta-id/{id}")
    public ResponseEntity<List<TransactionDTO>> obtenerTransaccionesPorTarjetaId(@PathVariable Long id) {

        return ResponseEntity.ok(transactionService.buscarTransaccionesPorTarjetaId(id)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    private TransactionDTO convertToDto(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setPan(transaction.getTarjeta().getPan());
        transactionDTO.setPanEnmascarado(transaction.getTarjeta().getPanEnmascarado());
        transactionDTO.setReferencia(transaction.getReferencia());
        transactionDTO.setTotalCompra(transaction.getTotalCompra());
        transactionDTO.setDireccionCompra(transaction.getDireccionCompra());
        transactionDTO.setEstado(transaction.getEstado());
        return transactionDTO;
    }
}
