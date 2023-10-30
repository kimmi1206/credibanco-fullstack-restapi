package org.bankinc.transactions.service;

import org.bankinc.transactions.exception.TarjetaException;
import org.bankinc.transactions.model.Tarjeta;
import org.bankinc.transactions.repository.TarjetaRepository;
import org.bankinc.transactions.response.EnrolarResponse;
import org.bankinc.transactions.response.TarjetaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TarjetaService {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    public TarjetaResponse crearTarjeta(Tarjeta request) throws TarjetaException {

        request.setEstado("Creada");
        request.setNumeroValidacion((int) (Math.random() * 100) + 1);
        String panEnmascarado = enmascararPan(request.getPan());
        request.setPanEnmascarado(panEnmascarado);

        TarjetaResponse response = new TarjetaResponse();
        response.setCodigoRespuesta("00");
        response.setMensaje("Exito");
        response.setNumeroValidacion(request.getNumeroValidacion());
        response.setPanEnmascarado(panEnmascarado);
        tarjetaRepository.save(request);
        return response;
    }

    public List<Tarjeta> obtenerTarjetas() {

        return tarjetaRepository.findAll();
    }

    public EnrolarResponse enrolarTarjeta(String pan, int numeroValidacion) throws TarjetaException {
        Optional<Tarjeta> tarjetaOptional = tarjetaRepository.findByPan(pan);
        EnrolarResponse enrolarResponse = new EnrolarResponse();
        if (!tarjetaOptional.isPresent()) {
            enrolarResponse.setCodigoRespuesta("01");
            enrolarResponse.setMensaje("Tarjeta no existe");
            enrolarResponse.setPanEnmascarado(enmascararPan(pan));
            return enrolarResponse;
        }
        Tarjeta tarjeta = tarjetaOptional.get();
        if (tarjeta.getEstado().equals("Enrolada")) {
            enrolarResponse.setCodigoRespuesta("01");
            enrolarResponse.setMensaje("Tarjeta ya está enrolada");
            enrolarResponse.setPanEnmascarado(enmascararPan(pan));
        } else if (tarjeta.getNumeroValidacion() != numeroValidacion) {
            enrolarResponse.setCodigoRespuesta("02");
            enrolarResponse.setMensaje("Número de validación inválido");
            enrolarResponse.setPanEnmascarado(enmascararPan(pan));
        } else {
            enrolarResponse.setCodigoRespuesta("00");
            enrolarResponse.setMensaje("Exito");
            enrolarResponse.setPanEnmascarado(enmascararPan(pan));
            tarjeta.setEstado("Enrolada");
            tarjetaRepository.save(tarjeta);
        }

        return enrolarResponse;
    }

    public Tarjeta consultarTarjeta(String pan) throws TarjetaException {
        Optional<Tarjeta> tarjetaOptional = tarjetaRepository.findByPan(pan);
        if (!tarjetaOptional.isPresent()) {
            throw new TarjetaException("Tarjeta no existe", "01", enmascararPan(pan));
        }
        Tarjeta tarjeta = tarjetaOptional.get();
        tarjeta.setPanEnmascarado(enmascararPan(tarjeta.getPan()));
        return tarjeta;
    }

    public void eliminarTarjeta(String pan, int numeroValidacion) throws TarjetaException {
        Optional<Tarjeta> tarjetaOptional = tarjetaRepository.findByPan(pan);
        if (!tarjetaOptional.isPresent()) {
            throw new TarjetaException("Tarjeta no existe", "01", enmascararPan(pan));
        }
        Tarjeta tarjeta = tarjetaOptional.get();
        if (tarjeta.getNumeroValidacion() != numeroValidacion) {
            throw new TarjetaException("Número de validación inválido", "02", enmascararPan(pan));
        }
        tarjetaRepository.delete(tarjeta);
    }

    public String enmascararPan(String pan) {
        String primerosSeisDigitos = pan.substring(0, 6);
        String ultimosCuatroDigitos = pan.substring(pan.length() - 4);
        String caracteresRelleno = "****";
        return primerosSeisDigitos + caracteresRelleno + ultimosCuatroDigitos;
    }

    public Map<String, List<Tarjeta>> obtenerTarjetasAgrupadasPorCedula() {
        List<Tarjeta> tarjetas = tarjetaRepository.findAll();
        Map<String, List<Tarjeta>> tarjetasPorCedula = tarjetas.stream()
                .collect(Collectors.groupingBy(Tarjeta::getCedula));
        return tarjetasPorCedula;
    }
}
