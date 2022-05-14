package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Agenda;
import br.com.uniamerica.api.entity.StatusAgenda;
import br.com.uniamerica.api.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
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
        if(id == agenda.getId()){
            if (agenda.getStatus().valor.equals("Pendente")) {
                agenda.setStatus(StatusAgenda.aprovado);
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
            agenda.setStatus(StatusAgenda.cancelado);
            this.agendaRepository.save(agenda);
            //this.historicoRepository.salvarNovo(agenda);
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

//    consultorio nao abre no domingo(ver se e domingo e se for, nao deixar agendar)
    public boolean isSunday(Agenda agenda){
        if(agenda.getDataDe().getDayOfWeek())
    }

//    public boolean isValidAgenda(Agenda agenda){
//        if (agenda.getDataDe().compareTo(LocalDateTime.now()) == 1){
//            if (agenda.getDataAte().compareTo(LocalDateTime.now()) == -1) {
//                if (agenda.getStatus().equals(StatusAgenda.compareceu) || agenda.getStatus().equals(StatusAgenda.nao_compareceu)) {
//                    return false;
//                }
//            }
//            if (agenda.getStatus().equals(StatusAgenda.pendente)){
//                return false;
//            }
//        }
//        return true;
//    }


}
