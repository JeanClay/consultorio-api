package br.com.uniamerica.api.repository;

import br.com.uniamerica.api.entity.Secretaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * @author Eduardo Sganderla
 *
 * @since 1.0.0, 05/04/2022
 * @version 1.0.0
 */
@Repository
public interface SecretariaRepository extends JpaRepository<Secretaria, Long> {

    /**
     *
     * @param idSecretaria
     * @param dataEx
     */
    @Modifying
    @Query("UPDATE Secretaria secretaria " +
            "SET secretaria.excluido = :dataEx " +
            "WHERE secretaria.id = :secretaria")
    public void updateDataExcluido(@Param("secretaria") Long idSecretaria
                                 , @Param("dataEx")LocalDateTime dataEx);
}
