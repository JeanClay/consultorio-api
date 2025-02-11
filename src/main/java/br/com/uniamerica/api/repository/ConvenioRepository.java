package br.com.uniamerica.api.repository;

import br.com.uniamerica.api.entity.Convenio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * @author Eduardo Sganderla
 *
 * @since 1.0.0, 31/03/2022
 * @version 1.0.0
 */
@Repository
public interface ConvenioRepository extends JpaRepository<Convenio, Long> {

    /**
     *
     * @param idConvenio
     * @param dataEx
     */
    @Modifying
    @Query("UPDATE Convenio convenio " +
            "SET convenio.excluido = :dataEx " +
            "WHERE convenio.id = :convenio")
    public void updateDataExcluido(@Param("convenio") Long idConvenio
                                 , @Param("dataEx")LocalDateTime dataEx);
}
