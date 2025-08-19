package br.com.alura.literalura20.principal;

import br.com.alura.literalura20.dto.ResultadoBuscaDTO;
import br.com.alura.literalura20.model.Autor;
import br.com.alura.literalura20.model.Livro;
import br.com.alura.literalura20.repository.AutorRepository;
import br.com.alura.literalura20.repository.LivroRepository;
import br.com.alura.literalura20.service.ConsumoApi;
import br.com.alura.literalura20.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO_BASE = "https://gutendx.com/books/";

    private LivroRepository livroRepository;
    private AutorRepository autorRepository;

    private List<Livro> livros = new ArrayList<>();

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void exibirMenu() {
        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    ========== LITERALURA ==========
                     1 - Buscar livro por título
                     2 - Listar livros registrados
                     3 - Listar autores registrados
                     4 - Listar autores vivos em determinado ano
                     5 - Listar livros em determinado idioma
                     6 - Top 10 livros mais baixados
                     7 - Buscar livros por autor
                     8 - Estatísticas dos livros
                     9 - Buscar autor por nome
                                      
                     0 - Sair
                    ================================
                    """;

            System.out.println(menu + "Escolha uma opção: ");

            try {
                opcao = leitura.nextInt();
                leitura.nextLine();

                switch (opcao) {
                    case 1:
                        buscarLivroPorTitulo();
                        break;
                    case 2:
                        listarLivrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosNoAno();
                        break;
                    case 5:
                        listarLivrosPorIdioma();
                        break;
                    case 6:
                        top10LivrosMaisBaixados();
                        break;
                    case 7:
                        buscarLivrosPorAutor();
                        break;
                    case 8:
                        exibirEstatisticas();
                        break;
                    case 9:
                        buscarAutorPorNome();
                        break;
                    case 0:
                        System.out.println("Encerrando o programa...");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor, digite um número válido!");
                leitura.nextLine();
                opcao = -1;
            }
        }
    }

    private void buscarLivroPorTitulo() {
        System.out.print("Digite o nome do livro que deseja buscar: ");
        var nomeLivro = leitura.nextLine();

        if (nomeLivro.trim().isEmpty()) {
            System.out.println("Nome do livro não pode ser vazio!");
            return;
        }

        try {
            var json = consumo.obterDados(ENDERECO_BASE + "?search=" +
                    nomeLivro.replace(" ", "%20").toLowerCase());

            var resultados = conversor.obterDados(json, ResultadoBuscaDTO.class);

            if (resultados.resultados() == null || resultados.resultados().isEmpty()) {
                System.out.println("Nenhum livro encontrado com esse título.");
                return;
            }

            var dadosLivro = resultados.resultados().get(0);

            Optional<Livro> livroExistente = livroRepository.findByTituloContainingIgnoreCase(dadosLivro.titulo());

            if (livroExistente.isPresent()) {
                System.out.println("Livro já registrado na base de dados:");
                System.out.println(livroExistente.get());
                return;
            }

            Livro novoLivro = new Livro(dadosLivro);

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

            livroRepository.save(novoLivro);
            System.out.println("\n*** LIVRO ENCONTRADO E SALVO ***");
            System.out.println(novoLivro);

        } catch (Exception e) {
            System.out.println("Erro ao buscar livro: " + e.getMessage());
        }
    }

    private void listarLivrosRegistrados() {
        livros = livroRepository.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado na base de dados.");
            return;
        }

        System.out.println("\n*** LIVROS REGISTRADOS ***");
        livros.stream()
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado na base de dados.");
            return;
        }

        System.out.println("\n*** AUTORES REGISTRADOS ***");
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNome))
                .forEach(System.out::println);
    }

    private void listarAutoresVivosNoAno() {
        System.out.print("Digite o ano que deseja pesquisar: ");
        try {
            var ano = leitura.nextInt();
            leitura.nextLine();

            List<Autor> autoresVivos = autorRepository.findAutoresVivosNoAno(ano);

            if (autoresVivos.isEmpty()) {
                System.out.println("Nenhum autor encontrado vivo no ano " + ano);
                return;
            }

            System.out.println("\n*** AUTORES VIVOS NO ANO " + ano + " ***");
            autoresVivos.forEach(System.out::println);

        } catch (InputMismatchException e) {
            System.out.println("Por favor, digite um ano válido!");
            leitura.nextLine();
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.println("Digite o idioma que deseja pesquisar:"
                + "es - Espanhol" + "en - Inglês" + "fr - Francês"
                + "pt - Português" + "Idioma: ");

        var idioma = leitura.nextLine().toLowerCase().trim();

        if (idioma.length() != 2) {
            System.out.println("Digite um código de idioma válido (2 letras)!");
            return;
        }

        // Usando o método correto que existe no LivroRepository
        List<Livro> livrosPorIdioma = livroRepository.findByIdiomaIgnoreCase(idioma);

        if (livrosPorIdioma.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma especificado.");
            return;
        }

        String nomeIdioma = obterNomeIdioma(idioma);
        System.out.println("\n*** LIVROS EM " + nomeIdioma.toUpperCase() + " ***");
        livrosPorIdioma.stream()
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(System.out::println);
    }

    private void top10LivrosMaisBaixados() {
        List<Livro> topLivros = livroRepository.findTop10ByOrderByNumeroDownloadsDesc();

        if (topLivros.isEmpty()) {
            System.out.println("Nenhum livro registrado na base de dados.");
            return;
        }

        System.out.println("\n*** TOP 10 LIVROS MAIS BAIXADOS ***");
        for (int i = 0; i < topLivros.size(); i++) {
            Livro livro = topLivros.get(i);
            System.out.println((i + 1) + "º - " + livro.getTitulo() +
                    " (" + livro.getNumeroDownloads() + " downloads)");
        }
    }

    private void buscarLivrosPorAutor() {
        System.out.print("Digite o nome do autor: ");
        var nomeAutor = leitura.nextLine();

        if (nomeAutor.trim().isEmpty()) {
            System.out.println("Nome do autor não pode ser vazio!");
            return;
        }

        List<Livro> livrosDoAutor = livroRepository.findByAutoresNomeContainingIgnoreCase(nomeAutor);

        if (livrosDoAutor.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o autor: " + nomeAutor);
            return;
        }

        System.out.println("\n*** LIVROS DO AUTOR: " + nomeAutor.toUpperCase() + " ***");
        livrosDoAutor.forEach(System.out::println);
    }

    private void exibirEstatisticas() {
        List<Livro> todosLivros = livroRepository.findAll();

        if (todosLivros.isEmpty()) {
            System.out.println("Nenhum dado disponível para estatísticas.");
            return;
        }

        long totalLivros = todosLivros.size();
        long totalAutores = autorRepository.count();

        DoubleSummaryStatistics stats = todosLivros.stream()
                .collect(Collectors.summarizingDouble(Livro::getNumeroDownloads));

        System.out.println("\n*** ESTATÍSTICAS DA BIBLIOTECA ***"
                + "Total de livros registrados: " + totalLivros
                + "Total de autores registrados: " + totalAutores
                + "Média de downloads: " + String.format("%.0f", stats.getAverage())
                + "Livro mais baixado: " + String.format("%.0f", stats.getMax()) + " downloads"
                + "Livro menos baixado: " + String.format("%.0f", stats.getMin()) + " downloads");
    }

    private void buscarAutorPorNome() {
        System.out.print("Digite o nome do autor: ");
        var nomeAutor = leitura.nextLine();

        if (nomeAutor.trim().isEmpty()) {
            System.out.println("Nome do autor não pode ser vazio!");
            return;
        }

        List<Autor> autoresEncontrados = autorRepository.findByNomeContainingIgnoreCase(nomeAutor);

        if (autoresEncontrados.isEmpty()) {
            System.out.println("Nenhum autor encontrado com o nome: " + nomeAutor);
            return;
        }

        System.out.println("\n*** AUTORES ENCONTRADOS ***");
        autoresEncontrados.forEach(autor -> {
            System.out.println(autor);
            List<Livro> livrosDoAutor = livroRepository.findByAutoresNomeContainingIgnoreCase(autor.getNome());
            if (!livrosDoAutor.isEmpty()) {
                System.out.println("Livros: ");
                livrosDoAutor.forEach(livro -> System.out.println("  - " + livro.getTitulo()));
            }
            System.out.println();
        });
    }

    private String obterNomeIdioma(String codigo) {
        return switch (codigo) {
            case "pt" -> "Português";
            case "en" -> "Inglês";
            case "es" -> "Espanhol";
            case "fr" -> "Francês";
            default -> codigo.toUpperCase();
        };
    }
}