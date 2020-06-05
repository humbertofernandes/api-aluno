package br.com.humbertofernandes.aluno.api.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Humberto Tadeu de Paiva Gomes Fernandes
 */
@Getter
public class ResourceCreatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 122038791109586036L;

    private final HttpServletResponse response;
    private final Long id;

    public ResourceCreatedEvent(Object source, HttpServletResponse response, Long id) {
        super(source);
        this.response = response;
        this.id = id;
    }
}
