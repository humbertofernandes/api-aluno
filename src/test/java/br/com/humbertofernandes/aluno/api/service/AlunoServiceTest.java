package br.com.humbertofernandes.aluno.api.service;

import br.com.humbertofernandes.aluno.api.model.Aluno;
import br.com.humbertofernandes.aluno.api.repository.AlunoRepository;
import br.com.humbertofernandes.aluno.api.service.exception.AlunoAlreadyExistsException;
import br.com.humbertofernandes.aluno.api.service.exception.AlunoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * @author Humberto Tadeu de Paiva Gomes Fernandes
 */
@ExtendWith(SpringExtension.class)
public class AlunoServiceTest {

    public static final long ID = 1L;
    public static final String NOME = "Humberto";
    public static final int IDADE = 29;
    public static final long ID2 = 2L;
    public static final int IDADE2 = 28;
    public static final String NOME2 = "Thais";

    private Aluno newAluno;
    private Aluno updateAluno;
    private Aluno updatedAlunoInDatabase;
    private Aluno alunoInDatabase;

    private AlunoService alunoService;

    @MockBean
    private AlunoRepository alunoRepositoryMocked;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        alunoService = new AlunoService(alunoRepositoryMocked);

        createInstanceAlunoInDataBase();
        createInstanceNewAluno();
        createUpdateAluno();
        createUpdatedAlunoInDatabase();
    }

    @Test
    public void should_get_all_alunos() {
        when(alunoRepositoryMocked.findAll()).thenReturn(Collections.singletonList(alunoInDatabase));
        List<Aluno> alunoAll = alunoService.getAll();

        assertThat(alunoAll.size(), equalTo(1));
        assertThat(alunoAll.get(0).getId(), equalTo(ID));
        assertThat(alunoAll.get(0).getNome(), equalTo(NOME));
        assertThat(alunoAll.get(0).getIdade(), equalTo(IDADE));
    }

    @Test
    public void should_get_all_alunos_and_return_empty_list() {
        List<Aluno> alunoAll = alunoService.getAll();
        assertThat(alunoAll.size(), equalTo(0));
    }

    @Test
    public void should_get_aluno_by_id() {
        when(alunoRepositoryMocked.findById(ID)).thenReturn(Optional.of(alunoInDatabase));

        Aluno alunoFound = alunoService.findById(ID);

        assertThat(alunoFound.getId(), equalTo(ID));
        assertThat(alunoFound.getNome(), equalTo(NOME));
        assertThat(alunoFound.getIdade(), equalTo(IDADE));
    }

    @Test
    public void should_return_exception_of_not_found_when_there_is_no_student_with_id() {
        assertThrows(AlunoNotFoundException.class, () -> alunoService.findById(ID));
    }

    @Test
    public void should_create_new_aluno() {
        when(alunoRepositoryMocked.save(newAluno)).thenReturn(alunoInDatabase);

        Aluno alunoSaved = alunoService.save(newAluno);

        assertThat(alunoSaved.getId(), equalTo(ID));
        assertThat(alunoSaved.getNome(), equalTo(NOME));
        assertThat(alunoSaved.getIdade(), equalTo(IDADE));
    }

    @Test
    public void should_deny_creation_of_aluno_that_exists() {
        when(alunoRepositoryMocked.findByNome("Humberto")).thenReturn(Optional.of(alunoInDatabase));
        assertThrows(AlunoAlreadyExistsException.class, () -> alunoService.save(newAluno));
    }

    @Test
    void should_update_aluno() {

        when(alunoRepositoryMocked.findById(ID)).thenReturn(Optional.of(alunoInDatabase));
        when(alunoService.save(alunoInDatabase)).thenReturn(updatedAlunoInDatabase);

        Aluno alunoSaved = alunoService.update(ID, updateAluno);

        assertThat(alunoSaved.getId(), equalTo(ID));
        assertThat(alunoSaved.getNome(), equalTo(NOME2));
        assertThat(alunoSaved.getIdade(), equalTo(IDADE2));
    }

    @Test
    public void should_deny_update_of_aluno_that_exists() {
        Aluno alunoInDatabase2 = new Aluno();
        alunoInDatabase2.setId(ID2);
        alunoInDatabase2.setNome(NOME2);
        alunoInDatabase2.setIdade(IDADE2);

        when(alunoRepositoryMocked.findById(ID2)).thenReturn(Optional.of(alunoInDatabase2));
        when(alunoRepositoryMocked.findByNome(NOME2)).thenReturn(Optional.of(alunoInDatabase));
        assertThrows(AlunoAlreadyExistsException.class, () -> alunoService.update(ID2, updateAluno));
    }

    @Test
    public void should_deny_update_of_aluno_that_not_found() {
        assertThrows(AlunoNotFoundException.class, () -> alunoService.update(ID2, updateAluno));
    }

    @Test
    void should_delete_a_aluno() {
        when(alunoRepositoryMocked.findById(ID)).thenReturn(Optional.of(alunoInDatabase));
        alunoService.delete(ID);
    }

    @Test
    void should_deny_delete_a_aluno() {
        assertThrows(AlunoNotFoundException.class, () -> alunoService.delete(ID));
    }

    private void createInstanceNewAluno() {
        newAluno = new Aluno();
        newAluno.setNome(NOME);
        newAluno.setIdade(IDADE);
    }

    private void createInstanceAlunoInDataBase() {
        alunoInDatabase = new Aluno();
        alunoInDatabase.setId(ID);
        alunoInDatabase.setNome(NOME);
        alunoInDatabase.setIdade(IDADE);
    }

    private void createUpdateAluno() {
        updateAluno = new Aluno();
        updateAluno.setNome("Thais");
        updateAluno.setIdade(28);
    }

    private void createUpdatedAlunoInDatabase() {
        updatedAlunoInDatabase = new Aluno();
        updatedAlunoInDatabase.setId(ID);
        updatedAlunoInDatabase.setNome("Thais");
        updatedAlunoInDatabase.setIdade(28);
    }
}
