package br.com.alura.literalura20.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alura.literalura20.model.Livro;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    // Busca livro por título (ignora maiúsculas/minúsculas)
    Optional<Livro> findByTituloContainingIgnoreCase(String titulo);

    // Lista livros por idioma específico
    List<Livro> findByIdiomaIgnoreCase(String idioma);

    // Top 10 livros mais baixados
    List<Livro> findTop10ByOrderByNumeroDownloadsDesc();

    // Busca livros por nome do autor (através do relacionamento)
    @Query("SELECT l FROM Livro l JOIN l.autores a WHERE a.nome ILIKE %:nomeAutor%")
    List<Livro> findByAutoresNomeContainingIgnoreCase(@Param("nomeAutor") String nomeAutor);

    // Lista todos os livros ordenados por título
    List<Livro> findAllByOrderByTituloAsc();

    // Conta total de livros por idioma
    @Query("SELECT COUNT(l) FROM Livro l WHERE l.idioma = :idioma")
    Long countByIdioma(@Param("idioma") String idioma);
}