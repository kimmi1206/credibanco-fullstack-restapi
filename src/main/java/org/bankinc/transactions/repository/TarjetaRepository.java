package org.bankinc.transactions.repository;

import org.bankinc.transactions.model.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {

    Optional<Tarjeta> findByPan(String pan);

    Optional<Tarjeta> findTarjetaById(Long id);

}
