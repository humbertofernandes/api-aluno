package br.com.humbertofernandes.aluno.api.service;

import br.com.humbertofernandes.aluno.api.model.Aluno;
import br.com.humbertofernandes.aluno.api.repository.AlunoRepository;
import br.com.humbertofernandes.aluno.api.service.exception.AlunoAlreadyExistsException;
import br.com.humbertofernandes.aluno.api.service.exception.AlunoNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Humberto Tadeu de Paiva Gomes Fernandes
 */
@Service
public class AlunoService {

    private AlunoRepository alunoRepository;

    public AlunoService(@Autowired AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public List<Aluno> getAll() {
        return alunoRepository.findAll();
    }

    public Aluno findById(final Long id) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(id);
        return alunoOptional.orElseThrow(AlunoNotFoundException::new);
    }

    public Aluno save(final Aluno aluno) {
        verifyIsAlunoExists(aluno);
        return alunoRepository.save(aluno);
    }

    public Aluno update(final Long id, final Aluno aluno) {
        Aluno alunoSaved = findById(id);
        BeanUtils.copyProperties(aluno, alunoSaved, "id");
        return save(alunoSaved);
    }

    public void delete(Long id) {
        Optional<Aluno> alunoSaved = alunoRepository.findById(id);

        if (!alunoSaved.isPresent()) {
            throw new AlunoNotFoundException();
        }

        alunoRepository.delete(alunoSaved.get());
    }

    private void verifyIsAlunoExists(final Aluno aluno) {
        Optional<Aluno> alunoByNome = alunoRepository.findByNome(aluno.getNome());

        if (alunoByNome.isPresent() && (aluno.isNew() || isUpdatingToDifferentAluno(aluno, alunoByNome))) {
            throw new AlunoAlreadyExistsException();
        }
    }

    private boolean isUpdatingToDifferentAluno(Aluno aluno, Optional<Aluno> alunoByNome) {
        return aluno.alreadyExist() && !alunoByNome.get().equals(aluno);
    }
}
