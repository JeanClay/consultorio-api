package br.com.uniamerica.api.controller;

import br.com.uniamerica.api.entity.Agenda;
import br.com.uniamerica.api.entity.Especialidade;
import br.com.uniamerica.api.repository.AgendaRepository;
import br.com.uniamerica.api.repository.EspecialidadeRepository;
import br.com.uniamerica.api.service.EspecialidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Eduardo Sganderla
 *
 * @since 1.0.0, 07/04/2022
 * @version 1.0.0
 */
@Controller//diz que e uma controler
@RequestMapping("/api/especialidades")//diz qual e a url responsavel pela controller
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService especialidadeService;

    @GetMapping("/{idEspecialidade}")
    public ResponseEntity<Especialidade> findById(@PathVariable("idEspecialidade") Long id){
        return ResponseEntity.ok().body(this.especialidadeService.findById(id).get());
    }

    @GetMapping
    public ResponseEntity<Page<Especialidade>> listByAllPage(Pageable pageable){
        return ResponseEntity.ok().body(this.especialidadeService.listAll(pageable));
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Especialidade especialidade){
        try{
            this.especialidadeService.insert(especialidade);
            return ResponseEntity.ok().body("Especialidade Cadastrada com Sucesso");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{idEspecialidade}")
    public ResponseEntity<?> update(@RequestBody Especialidade especialidade,
                                    @PathVariable Long idEspecialidade){
        try{
            this.especialidadeService.update(idEspecialidade,especialidade);
            return ResponseEntity.ok().body("Especialidade Atualizada com Sucesso");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/status/{idEspecialidade}")
    public ResponseEntity<?> updateStatus(@RequestBody Especialidade especialidade,
                                          @PathVariable Long idEspecialidade){
        try{
            this.especialidadeService.updateDataExcluido(idEspecialidade,especialidade);
            return ResponseEntity.ok().body("Especialidade Atualizada com Sucesso");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
