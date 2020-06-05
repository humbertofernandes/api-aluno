package br.com.humbertofernandes.aluno.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Humberto Tadeu de Paiva Gomes Fernandes
 */
@Entity
@Table(name = "aluno")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "aluno-1")
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @NotNull(message = "aluno-2")
    @Min(value = 1, message = "aluno-3")
    @Column(name = "idade", nullable = false)
    private Integer idade;

    @JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }

    @JsonIgnore
    public boolean alreadyExist() {
        return getId() != null;
    }
}
