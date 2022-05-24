package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Agenda;
import br.com.uniamerica.api.entity.Secretaria;
import br.com.uniamerica.api.entity.StatusAgenda;
import br.com.uniamerica.api.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    public Optional<Agenda> findById(Long id){
        return this.agendaRepository.findById(id);
    }

    public Page<Agenda> listAll(Pageable pageable){
        return this.agendaRepository.findAll(pageable);
    }

    @Transactional
    public void updateDataExcluido(Long id, Agenda agenda){
        if(id == agenda.getId()){
            this.agendaRepository.updateDataExcluido(agenda.getId(), LocalDateTime.now());
        }else{
            throw new RuntimeException();
        }
    }

    @Transactional
    public void setAprovado(Long id, Agenda agenda){
        if (agenda != null){
            if (id == agenda.getId()) {
                if ( agenda.getStatus().valor.equals("Pendente")) {
                     agenda.setStatus(StatusAgenda.aprovado);
                    this.agendaRepository.save(agenda);
                    //this.historicoRepository.salvarNovo(agenda);
                } else {
                    throw new RuntimeException(" Status Diferente de Pendente");
                }
            } else {
                throw new RuntimeException(" Id diferente de Id Agenda");
            }
        }
    }

    @Transactional
    public void setRejeitado(Long id, Agenda agenda){
        if(id == agenda.getId()){
            if (agenda.getStatus().valor.equals("Pendente")) {
                agenda.setStatus(StatusAgenda.rejeitado);
                this.agendaRepository.save(agenda);
                //this.historicoRepository.salvarNovo(agenda);
            }else{
                throw new RuntimeException(" Status Diferente de Pendente");
            }
        }else{
            throw new RuntimeException(" Id diferente de Id Agenda");
        }
    }

    @Transactional
    public void setCancelado(Long id, Agenda agenda){
        if(id == agenda.getId()){
            if(agenda.getStatus().equals(StatusAgenda.aprovado) || agenda.getStatus().equals(StatusAgenda.pendente)){
                agenda.setStatus(StatusAgenda.cancelado);
                this.agendaRepository.save(agenda);
                //this.historicoRepository.salvarNovo(agenda);}
            }else{
                throw new RuntimeException();
            }
        }else{
            throw new RuntimeException();
        }
    }

    @Transactional
    public void setCompareceu(Long id, Agenda agenda){
        if(id == agenda.getId()){
            if(agenda.getStatus().valor.equals("Aprovado")) {
                agenda.setStatus(StatusAgenda.compareceu);
                this.agendaRepository.save(agenda);
                //this.historicoRepository.salvarNovo(agenda);
            }
        }else{
            throw new RuntimeException();
        }
    }

    @Transactional
    public void SetNaoCompareceu(Long id, Agenda agenda){
        if(id == agenda.getId()){
            if(agenda.getStatus().valor.equals("Aprovado")) {
                agenda.setStatus(StatusAgenda.nao_compareceu);
                this.agendaRepository.save(agenda);
                //this.historicoRepository.salvarNovo(agenda);
            }
        }else{
            throw new RuntimeException();
        }
    }


    public boolean isValidData(Agenda agenda){
        return true;
    }

    public boolean isEncaixe(Agenda agenda){
        if(agenda!=null){
            return agenda.getEncaixe();
        }
        return false;
    }
    public boolean isWeekend(Agenda agenda){
        if(agenda!=null) {
            if (agenda.getDataDe().getDayOfWeek().name().equals("SUNDAY") || agenda.getDataDe().getDayOfWeek().name().equals("SATURDAY")) {
                return true;
            }
        }
        return false;
    }
    public boolean isPast(Agenda agenda){
        if (agenda!=null){
            if (agenda.getDataDe().isAfter(LocalDateTime.now()) || agenda.getDataAte().isAfter(LocalDateTime.now())){
                return true;
            }
        }
        return false;
    }

    public boolean validTime(Agenda agenda){
        if (agenda!=null){
            if(agenda.getDataAte().isAfter(agenda.getDataDe())){
                return true;
            }
        }
        return false;
    }

    public boolean isRightTime(Agenda agenda){
        LocalTime manhaInicio = LocalTime.of(8,0);
        LocalTime manhaFim = LocalTime.of(12,0);
        LocalTime tardeInicio = LocalTime.of(14,0);
        LocalTime tardeFim = LocalTime.of(18,0);
        LocalTime horaAtual = agenda.getDataDe().toLocalTime();

        if(horaAtual.isAfter(manhaInicio) && horaAtual.isBefore(manhaFim)){
            return true;
        }
        if (horaAtual.isAfter(tardeInicio) && horaAtual.isBefore(tardeFim)){
            return true;
        }
        return false;
    }

    public boolean checkConflictMedico(Agenda agenda){
        List<Agenda> conflitos = this.agendaRepository.checkConflictMedico(agenda.getMedico().getId(),agenda.getDataDe());
        switch (conflitos.size()){
            case 0:
                return false;
            case 1:
                return agenda.getId().equals(conflitos.get(0).getId()) ? false : true;
            case 2:
                return true;
        }
        return false;
    }

    public boolean checkConflictPaciente(Agenda agenda){
        List<Agenda> conflitos = this.agendaRepository.checkConflictPaciente(agenda.getMedico().getId(),agenda.getDataDe());
        switch (conflitos.size()){
            case 0:
                return false;
            case 1:
                return agenda.getId().equals(conflitos.get(0).getId()) ? false : true;
            case 2:
                return true;
        }
        return false;
    }
    @Transactional
    public void updateAgendaTransaction(Agenda agenda){
        this.agendaRepository.save(agenda);
    }
//
//    public boolean checkConflictProfessor(Agenda agenda){
//        List<Agenda> conflitos = this.agendaRepository.haveConflict(
//                agenda.getDataDe(),
//                agenda.getDataAte(),
//                agenda.getMedico().getId(),
//                agenda.getPaciente().getId(),
//                agenda.getId());
//
//    }

    public void updateAgendaProfessor(Agenda agenda){
        Assert.isTrue(!isPast(agenda), "Esta no Passado");
        Assert.isTrue(validTime(agenda), "Hora ate antes de hora de");
        Assert.isTrue(isRightTime(agenda), "Agendamento fora de horario comercial");
        Assert.isTrue(!isWeekend(agenda), "Agendamento no fim de semana");
//        Assert.isTrue();
        if(isEncaixe(agenda)){

        }
    }
    public void updateAgenda(Agenda agenda, Secretaria secretaria){
        if(!isPast(agenda)){
            if(validTime(agenda)){
                if(isRightTime(agenda)){
                    if(!isWeekend(agenda)){
                        if(!isEncaixe(agenda)){
                            if(checkConflictMedico(agenda)){
                                if(checkConflictPaciente(agenda)){
                                    agenda.setStatus(StatusAgenda.aprovado);
                                    updateAgendaTransaction(agenda);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateAgenda(Agenda agenda){
        if(!isPast(agenda)){
            if(validTime(agenda)){
                if(isRightTime(agenda)){
                    if(!isWeekend(agenda)){
                        if(!isEncaixe(agenda)){
                            if(checkConflictMedico(agenda)){
                                if(checkConflictPaciente(agenda)){
                                    agenda.setStatus(StatusAgenda.pendente);
                                    updateAgendaTransaction(agenda);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void insertAgenda(Agenda agenda, Secretaria secretaria){
        if(!isPast(agenda)){
            if(validTime(agenda)){
                if(isRightTime(agenda)){
                    if(!isWeekend(agenda)){
                        if(!isEncaixe(agenda)){
                            if(checkConflictMedico(agenda)){
                                if(checkConflictPaciente(agenda)){
                                    agenda.setStatus(StatusAgenda.aprovado);
                                    updateAgenda(agenda);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void insertAgenda(Agenda agenda){
        if(!isPast(agenda)){
            if(validTime(agenda)){
                if(isRightTime(agenda)){
                    if(!isWeekend(agenda)){
                        if(!isEncaixe(agenda)){
                            if(checkConflictMedico(agenda)){
                                if(checkConflictPaciente(agenda)){
                                    agenda.setStatus(StatusAgenda.pendente);
                                    updateAgenda(agenda);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
