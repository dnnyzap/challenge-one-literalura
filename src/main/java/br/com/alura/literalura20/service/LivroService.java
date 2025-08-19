package br.com.alura.literalura20.service;

import br.com.alura.literalura20.dto.DadosLivroDTO;
import br.com.alura.literalura20.dto.ResultadoBuscaDTO;
import br.com.alura.literalura20.model.Autor;
import br.com.alura.literalura20.model.Livro;
import br.com.alura.literalura20.repository.AutorRepository;
import br.com.alura.literalura20.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private ConsumoApi consumoApi;

    @Autowired
    private ConverteDados converteDados;

    private final String ENDERECO_BASE = "https://gutendx.com/books/";

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public List<Livro> listarPorIdioma(String idioma) {
        return livroRepository.findByIdiomaIgnoreCase(idioma);
    }

    public List<Livro> top10MaisBaixados() {
        return livroRepository.findTop10ByOrderByNumeroDownloadsDesc();
    }

    public List<Livro> buscarPorAutor(String nomeAutor) {
        return livroRepository.findByAutoresNomeContainingIgnoreCase(nomeAutor);
    }

    public Optional<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public Livro buscarESalvarDaApi(String nomeLivro) {
        try {
            String json = consumoApi.obterDados(ENDERECO_BASE + "?search=" + 
                    nomeLivro.replace(" ", "%20").toLowerCase());

            ResultadoBuscaDTO resultados = converteDados.obterDados(json, ResultadoBuscaDTO.class);

            if (resultados.resultados() == null || resultados.resultados().isEmpty())
                throw new RuntimeException("Nenhum livro encontrado com esse título na API");

            DadosLivroDTO dadosLivro = resultados.resultados().get(0);

            // Verifica se já existe
            Optional<Livro> livroExistente = livroRepository.findByTituloContainingIgnoreCase(dadosLivro.titulo());
            if (livroExistente.isPresent())
                return livroExistente.get();

            // Cria novo livro
            Livro novoLivro = new Livro(dadosLivro);

            // Processa os autores
            if (dadosLivro.autores() != null && !dadosLivro.autores().isEmpty()) {
                List<Autor> autoresProcessados = dadosLivro.autores().stream()
                        .map(dadosAutor -> {
                            Optional<Autor> autorExistente = autorRepository.findByNomeIgnoreCase(dadosAutor.nome());
                            return autorExistente.orElseGet(() -> {
                                Autor novoAutor = new Autor(dadosAutor);
                                return autorRepository.save(novoAutor);
                            });
                        })
                        .collect(Collectors.toList());

                novoLivro.setAutores(autoresProcessados);
            }

            return livroRepository.save(novoLivro);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar livro na API: " + e.getMessage());
        }
    }

    public Long contarPorIdioma(String idioma) {
        return livroRepository.countByIdioma(idioma);
    }

    public Long contarTotal() {
        return livroRepository.count();
    }
}