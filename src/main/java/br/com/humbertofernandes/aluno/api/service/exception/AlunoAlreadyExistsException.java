package br.com.humbertofernandes.aluno.api.service.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Humberto Tadeu de Paiva Gomes Fernandes
 */
public class AlunoAlreadyExistsException extends BusinessException {

    private static final long serialVersionUID = 7699063453846558338L;

    public AlunoAlreadyExistsException() {
        super("aluno-4", HttpStatus.BAD_REQUEST);
    }
}
