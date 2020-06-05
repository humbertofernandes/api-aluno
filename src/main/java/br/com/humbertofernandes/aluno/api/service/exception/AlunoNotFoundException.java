package br.com.humbertofernandes.aluno.api.service.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Humberto Tadeu de Paiva Gomes Fernandes
 */
public class AlunoNotFoundException extends BusinessException {

    private static final long serialVersionUID = 2499933133299296917L;

    public AlunoNotFoundException() {
        super("aluno-5", HttpStatus.NOT_FOUND);
    }
}
