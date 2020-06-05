package br.com.humbertofernandes.aluno.api.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Humberto Tadeu de Paiva Gomes Fernandes
 */
@RequiredArgsConstructor
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -4283908950021089762L;

    private final String code;
    private final HttpStatus status;
}
