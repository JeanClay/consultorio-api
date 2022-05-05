package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Paciente;
import br.com.uniamerica.api.entity.TipoAtendimento;
import br.com.uniamerica.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public Optional<Paciente> findById(Long id){
        return this.pacienteRepository.findById(id);
    }

    public Page<Paciente> listAll(Pageable pageable){
        return this.pacienteRepository.findAll(pageable);
    }

    @Transactional
    public void insert(Paciente paciente){
        if(isConvenio(paciente)){
            if (validConvenio(paciente)){
                this.pacienteRepository.save(paciente);
            }else{
                throw new RuntimeException();
            }
        }else{
            this.pacienteRepository.save(paciente);
        }
    }

    @Transactional
    public void update(Long id,Paciente paciente) {
        if (id == paciente.getId()) {
            if (isConvenio(paciente)) {
                if (validConvenio(paciente)) {
                    this.pacienteRepository.save(paciente);
                } else {
                    throw new RuntimeException();
                }
            } else {
                this.pacienteRepository.save(paciente);
            }
        } else {
            throw new RuntimeException();
        }

    }

    @Transactional
    public void updateDataExcluido(Long id, Paciente paciente){
        if(id == paciente.getId()){
            this.pacienteRepository.updateDataExcluido(paciente.getId(), LocalDateTime.now());
        }else{
            throw new RuntimeException();
        }
    }

    public boolean isConvenio (Paciente paciente){
        if (paciente.getTipoAtendimento().equals(TipoAtendimento.convenio)){
            return true;
        }else{
            return false;
        }
    }

    public boolean validConvenio (Paciente paciente){
        if (paciente.getNumeroCartaoConvenio() != null &&
            paciente.getDataVencimento() != null &&
            paciente.getConvenio() != null){
            return true;
        }else{
            return false;
        }
    }

    public void insert2 (Paciente paciente){
        this.validarFormulario(paciente);
        this.saveTransactional(paciente);
    }
    public void update2(Paciente paciente){
        this.validarFormulario(paciente);
        this.saveTransactional(paciente);

    }
    @Transactional
    public void saveTransactional(Paciente paciente){
        this.pacienteRepository.save(paciente);
    }

    public void validarFormulario(Paciente paciente){
        if(paciente.getTipoAtendimento().equals(TipoAtendimento.convenio)){
            if (paciente.getConvenio()== null || paciente.getConvenio().getId() == null){
                throw new RuntimeException(" Convenio nao informado.");
            }
            if (paciente.getNumeroCartaoConvenio() == null){
                throw new RuntimeException(" Numero do cartao convenio nao informado ");
            }
            if (paciente.getDataVencimento() == null){
                throw new RuntimeException(" Data de vencimento nao informada ");
            }
            if (paciente.getDataVencimento().compareTo(LocalDateTime.now()) > 0){
                throw new RuntimeException(" Data de vencimento expirada ");
            }
        }
        if (paciente.getTipoAtendimento().equals(TipoAtendimento.particular)){
            paciente.setConvenio(null);
            paciente.setNumeroCartaoConvenio(null);
            paciente.setDataVencimento(null);
        }

    }
}
