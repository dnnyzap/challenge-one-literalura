package br.com.alura.literalura20.model;

import br.com.alura.literalura20.dto.DadosAutorDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "autores")
@Data
@NoArgsConstructor
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "ano_nascimento")
    private Integer anoNascimento;

    @Column(name = "ano_falecimento")
    private Integer anoFalecimento;

    public Autor(DadosAutorDTO dadosAutor) {
    }

    // Método para criar autor a partir dos dados da API
    public static Autor fromDadosAutor(DadosAutorDTO dadosAutor) {
        Autor autor = new Autor(dadosAutor);
        autor.setNome(dadosAutor.nome());
        autor.setAnoNascimento(dadosAutor.anoNascimento());
        autor.setAnoFalecimento(dadosAutor.anoFalecimento());
        return autor;
    }

    // Método toString personalizado para melhor apresentação
    @Override
    public String toString() {
        return String.format("""
            Autor: %s
            Nascimento: %s
            Falecimento: %s
            """, nome,
                anoNascimento != null ? anoNascimento.toString() : "N/A",
                anoFalecimento != null ? anoFalecimento.toString() : "N/A");
    }
}
