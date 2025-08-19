package br.com.alura.literalura20.model;

import br.com.alura.literalura20.dto.DadosLivroDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "livros")
@Data
@NoArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String titulo;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores;

    @Column(name = "idioma")
    private String idioma; // Apenas um idioma conforme especificação

    @Column(name = "numero_downloads")
    private Double numeroDownloads;

    public Livro(DadosLivroDTO dadosLivro) {
    }

    // Método para criar livro a partir dos dados da API
    public static Livro fromDadosLivro(DadosLivroDTO dadosLivro) {
        Livro livro = new Livro();
        livro.setTitulo(dadosLivro.titulo());
        livro.setNumeroDownloads(dadosLivro.numeroDownloads() != null ? dadosLivro.numeroDownloads() : 0.0);

        // Pega apenas o primeiro idioma da lista conforme especificação
        if (dadosLivro.idiomas() != null && !dadosLivro.idiomas().isEmpty())
            livro.setIdioma(dadosLivro.idiomas().get(0));

        return livro;
    }

    // Método toString personalizado para melhor apresentação
    @Override
    public String toString() {
        String autoresNomes = "";
        if (autores != null && !autores.isEmpty())
            autoresNomes = autores.stream()
                    .map(Autor::getNome)
                    .collect(Collectors.joining(", "));

        return String.format("""
            ---------- LIVRO ----------
            Título: %s
            Autor(es): %s
            Idioma: %s
            Número de downloads: %.0f
            ---------------------------
            """, titulo, autoresNomes, idioma != null ? idioma : "N/A", numeroDownloads);
    }
}