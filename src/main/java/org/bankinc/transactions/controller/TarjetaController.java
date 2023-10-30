package org.bankinc.transactions.controller;

import org.bankinc.transactions.DTO.TarjetaDTO;
import org.bankinc.transactions.exception.TarjetaException;
import org.bankinc.transactions.model.Tarjeta;
import org.bankinc.transactions.response.EnrolarResponse;
import org.bankinc.transactions.response.TarjetaResponse;
import org.bankinc.transactions.service.TarjetaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaController {

    @Autowired
    private TarjetaService tarjetaService;

    @PostMapping("/crear")
    public ResponseEntity<TarjetaResponse> crearTarjeta(@RequestBody Tarjeta request) throws TarjetaException {

        TarjetaResponse response = tarjetaService.crearTarjeta(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/enrolar")
    public ResponseEntity<EnrolarResponse> enrolarTarjeta(@RequestParam String pan, @RequestParam int numeroValidacion)
            throws TarjetaException {
        EnrolarResponse response = tarjetaService.enrolarTarjeta(pan, numeroValidacion);
        TarjetaDTO tarjetaDTO = new TarjetaDTO();
        BeanUtils.copyProperties(response, tarjetaDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{pan}")
    public ResponseEntity<TarjetaDTO> consultarTarjeta(@PathVariable String pan) throws TarjetaException {
        Tarjeta tarjeta = tarjetaService.consultarTarjeta(pan);
        TarjetaDTO tarjetaDTO = new TarjetaDTO();
        BeanUtils.copyProperties(tarjeta, tarjetaDTO);
        return ResponseEntity.ok(tarjetaDTO);
    }

    @GetMapping("/")
    public ResponseEntity<List<TarjetaDTO>> consultarTarjetas() throws TarjetaException {
        List<TarjetaDTO> tarjetas = tarjetaService.obtenerTarjetas()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tarjetas);
    }

    @DeleteMapping("/{pan}")
    public ResponseEntity<Void> eliminarTarjeta(@PathVariable String pan, @RequestParam int numeroValidacion)
            throws TarjetaException {
        tarjetaService.eliminarTarjeta(pan, numeroValidacion);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/por-cedula")
    public ResponseEntity<Map<String, List<TarjetaDTO>>> obtenerTarjetasAgrupadasPorCedula() {
        Map<String, List<Tarjeta>> tarjetasPorCedula = tarjetaService.obtenerTarjetasAgrupadasPorCedula();
        Map<String, List<TarjetaDTO>> tarjetasDTOporCedula = new HashMap<>();

        for (Map.Entry<String, List<Tarjeta>> entry : tarjetasPorCedula.entrySet()) {
            String cedula = entry.getKey();
            List<Tarjeta> tarjetas = entry.getValue();
            List<TarjetaDTO> tarjetasDTO = new ArrayList<>();

            for (Tarjeta tarjeta : tarjetas) {
                TarjetaDTO tarjetaDTO = new TarjetaDTO();
                tarjetaDTO.setTitular(tarjeta.getTitular());
                tarjetaDTO.setTipo(tarjeta.getTipo());
                tarjetaDTO.setTelefono(tarjeta.getTelefono());
                tarjetaDTO.setEstado(tarjeta.getEstado());
                tarjetaDTO.setPanEnmascarado(tarjeta.getPanEnmascarado());

                tarjetasDTO.add(tarjetaDTO);
            }

            tarjetasDTOporCedula.put(cedula, tarjetasDTO);
        }

        return ResponseEntity.ok(tarjetasDTOporCedula);
    }

    private TarjetaDTO convertToDto(Tarjeta tarjeta) {
        TarjetaDTO tarjetaDto = new TarjetaDTO();
        tarjetaDto.setTitular(tarjeta.getTitular());
        tarjetaDto.setTipo(tarjeta.getTipo());
        tarjetaDto.setTelefono(tarjeta.getTelefono());
        tarjetaDto.setEstado(tarjeta.getEstado());
        tarjetaDto.setPanEnmascarado(tarjeta.getPanEnmascarado());
        tarjetaDto.setId(tarjeta.getId());
        return tarjetaDto;
    }

}
