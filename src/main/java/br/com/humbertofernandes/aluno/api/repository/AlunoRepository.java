package br.com.humbertofernandes.aluno.api.repository;

import br.com.humbertofernandes.aluno.api.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Humberto Tadeu de Paiva Gomes Fernandes
 */
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByNome(String nome);
}
