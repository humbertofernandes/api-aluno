package br.com.humbertofernandes.aluno.api.controller;

import br.com.humbertofernandes.aluno.api.event.ResourceCreatedEvent;
import br.com.humbertofernandes.aluno.api.model.Aluno;
import br.com.humbertofernandes.aluno.api.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Humberto Tadeu de Paiva Gomes Fernandes
 */
@RestController
@RequestMapping("/aluno")
public class AlunoController {

    private final ApplicationEventPublisher publisher;
    private final AlunoService alunoService;

    @Autowired
    public AlunoController(ApplicationEventPublisher publisher, AlunoService alunoService) {
        this.publisher = publisher;
        this.alunoService = alunoService;
    }

    @GetMapping
    public ResponseEntity<List<Aluno>> all() {
        List<Aluno> list = alunoService.getAll();
        return ResponseEntity.status(list.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> findId(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Aluno> create(@Valid @RequestBody Aluno aluno, HttpServletResponse response) {
        Aluno alunoSave = alunoService.save(aluno);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, alunoSave.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoSave);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> update(@PathVariable Long id, @Valid @RequestBody Aluno aluno) {
        return ResponseEntity.status(HttpStatus.OK).body(alunoService.update(id, aluno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
