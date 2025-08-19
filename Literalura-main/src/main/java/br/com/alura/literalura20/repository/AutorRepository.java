package br.com.alura.literalura20.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alura.literalura20.model.Autor;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    // Busca autor por nome (ignora maiúsculas/minúsculas)
    List<Autor> findByNomeContainingIgnoreCase(String nome);

    // Busca autor por nome exato
    Optional<Autor> findByNomeIgnoreCase(String nome);

    // Lista todos os autores ordenados por nome
    List<Autor> findAllByOrderByNomeAsc();

    // Busca autores que estavam vivos em um determinado ano
    @Query("SELECT a FROM Autor a WHERE (a.anoNascimento <= :ano) AND (a.anoFalecimento IS NULL OR a.anoFalecimento >= :ano)")
    List<Autor> findAutoresVivosNoAno(@Param("ano") Integer ano);

    // Busca autores por período de nascimento
    @Query("SELECT a FROM Autor a WHERE a.anoNascimento BETWEEN :anoInicio AND :anoFim")
    List<Autor> findByAnoNascimentoBetween(@Param("anoInicio") Integer anoInicio, @Param("anoFim") Integer anoFim);

    // Busca autores por período de falecimento
    @Query("SELECT a FROM Autor a WHERE a.anoFalecimento BETWEEN :anoInicio AND :anoFim")
    List<Autor> findByAnoFalecimentoBetween(@Param("anoInicio") Integer anoInicio, @Param("anoFim") Integer anoFim);

    // Lista autores ainda vivos (ano de falecimento null)
    List<Autor> findByAnoFalecimentoIsNull();

    // Lista autores já falecidos (ano de falecimento não null)
    List<Autor> findByAnoFalecimentoIsNotNull();

    // Busca autores com livros cadastrados no sistema
    @Query("SELECT DISTINCT a FROM Autor a JOIN Livro l JOIN l.autores la WHERE la.id = a.id")
    List<Autor> findAutoresComLivros();

    // Conta quantos livros um autor tem
    @Query("SELECT COUNT(l) FROM Livro l JOIN l.autores a WHERE a.id = :autorId")
    Long countLivrosByAutorId(@Param("autorId") Long autorId);

    // Busca autores mais prolíficos (com mais livros)
    @Query("SELECT a, COUNT(l) as totalLivros FROM Autor a JOIN Livro l JOIN l.autores la WHERE la.id = a.id GROUP BY a ORDER BY COUNT(l) DESC")
    List<Autor> findAutoresMaisProlificos();
}