package br.com.humbertofernandes.aluno.api.repository;

import br.com.humbertofernandes.aluno.api.model.Aluno;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


/**
 * @author Humberto Tadeu de Paiva Gomes Fernandes
 */
@Sql(value = "/load-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    public void should_find_aluno_by_name() {
        Optional<Aluno> alunoSaved = alunoRepository.findByNome("Humberto");
        assertThat(alunoSaved.isPresent(), equalTo(Boolean.TRUE));

        Aluno aluno = alunoSaved.get();
        assertThat(aluno.getId(), equalTo(1L));
        assertThat(aluno.getNome(), equalTo("Humberto"));
        assertThat(aluno.getIdade(), equalTo(29));
    }

    @Test
    public void should_not_find_aluno_by_name() {
        Optional<Aluno> alunoSaved = alunoRepository.findByNome("João");
        assertThat(alunoSaved.isPresent(), equalTo(Boolean.FALSE));
    }
}
