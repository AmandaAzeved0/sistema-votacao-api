package org.sicredi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "associado")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Associado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    @Column( nullable = false)
    private String telefone;

    @Column( nullable = false)
    private String endereco;

    @Column( name = "apto_para_votar")
    private boolean aptoParaVotar;
}
