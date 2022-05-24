package br.com.uniamerica.api.repository;

import br.com.uniamerica.api.entity.Agenda;
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
 * @since 1.0.0, 31/03/2022
 * @version 1.0.0
 */
@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    /**
     *
     * @param idAgenda
     * @param dataEx
     */
    @Modifying
    @Query("UPDATE Agenda agenda "+
            "SET agenda.excluido = :dataEx " +
            "WHERE agenda.id = :agenda")
    public void updateDataExcluido(@Param("agenda") Long idAgenda,
                                   @Param("dataEx") LocalDateTime dataEx);


    /**
     *
     * @param idMedico
     * @param dataCheck
     * @return
     */
    @Query("FROM Agenda agenda " +
            "WHERE agenda.medico = :idMedico " +
            "AND agenda.excluido is null " +
            "AND agenda.status = 'aprovado' " +
            "AND agenda.encaixe is false " +
            "AND :dataCheck BETWEEN agenda.dataDe and agenda.dataAte")
    public List<Agenda> checkConflictMedico(@Param("idMedico") Long idMedico, @Param("dataCheck") LocalDateTime dataCheck);

    /**
     *
     * @param idPaciente
     * @param dataCheck
     * @return
     */
    @Query("FROM Agenda agenda " +
            "WHERE agenda.paciente = :idPaciente " +
            "AND agenda.excluido is null " +
            "AND agenda.status = 'aprovado' " +
            "AND agenda.encaixe is false " +
            "AND :dataCheck BETWEEN agenda.dataDe and agenda.dataAte")
    public List<Agenda> checkConflictPaciente(@Param("idPaciente") Long idPaciente, @Param("dataCheck") LocalDateTime dataCheck);


    /**
     *
     * @param dataDe
     * @param dataAte
     * @param idMedico
     * @param idPaciente
     * @param idAgenda
     * @return
     */
    @Query("FROM Agenda agenda " +
            "WHERE ( " +
            ":dataDe BETWEEN agenda.dataDe AND agenda.dataAte " +
            "OR " +
            ":dataAte BETWEEN agenda.dataDe AND agenda.dataAte " +
            ") " +
            "AND (:medico = agenda.medico OR :paciente = agenda.paciente) " +
            "AND :agenda <> agenda")
    public List<Agenda> haveConflict(
            @Param("dataDe") LocalDateTime dataDe,
            @Param("dataAte") LocalDateTime dataAte,
            @Param("medico") Long idMedico,
            @Param("paciente") Long idPaciente,
            @Param("agenda") Long idAgenda);
}
