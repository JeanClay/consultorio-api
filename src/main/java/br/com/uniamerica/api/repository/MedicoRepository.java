package br.com.uniamerica.api.repository;

import br.com.uniamerica.api.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Eduardo Sganderla
 *
 * @since 1.0.0, 22/03/2022
 * @version 1.0.0
 */
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    /**
     * @see Medico#Medico(Long, String, String)
     */
    @Query("SELECT new Medico(medico.id, medico.nome, medico.crm) FROM Medico medico")
    public List<Medico> listTable();

    /**
     *
     * @param idMedico
     * @param dataEx
     */
    @Modifying
    @Query("UPDATE Medico medico " +
            "SET medico.excluido = :dataEx " +
            "WHERE medico.id = :medico")
    public void updateDataExcluido(@Param("medico")Long idMedico
            , @Param("dataEx") LocalDateTime dataEx);
}
