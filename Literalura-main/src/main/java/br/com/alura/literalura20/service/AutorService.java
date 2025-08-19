package br.com.alura.literalura20.service;

import br.com.alura.literalura20.model.Autor;
import br.com.alura.literalura20.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> listarTodos() {
        return autorRepository.findAllByOrderByNomeAsc();
    }

    public List<Autor> buscarPorNome(String nome) {
        return autorRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Optional<Autor> buscarPorNomeExato(String nome) {
        return autorRepository.findByNomeIgnoreCase(nome);
    }

    public List<Autor> buscarVivosNoAno(Integer ano) {
        return autorRepository.findAutoresVivosNoAno(ano);
    }

    public List<Autor> buscarVivos() {
        return autorRepository.findByAnoFalecimentoIsNull();
    }

    public List<Autor> buscarFalecidos() {
        return autorRepository.findByAnoFalecimentoIsNotNull();
    }

    public List<Autor> buscarPorPeriodoNascimento(Integer anoInicio, Integer anoFim) {
        return autorRepository.findByAnoNascimentoBetween(anoInicio, anoFim);
    }

    public List<Autor> buscarPorPeriodoFalecimento(Integer anoInicio, Integer anoFim) {
        return autorRepository.findByAnoFalecimentoBetween(anoInicio, anoFim);
    }

    public List<Autor> buscarComLivros() {
        return autorRepository.findAutoresComLivros();
    }

    public Long contarLivrosDoAutor(Long autorId) {
        return autorRepository.countLivrosByAutorId(autorId);
    }

    public List<Autor> buscarMaisProlificos() {
        return autorRepository.findAutoresMaisProlificos();
    }

    public Autor salvar(Autor autor) {
        return autorRepository.save(autor);
    }

    public Long contarTotal() {
        return autorRepository.count();
    }

    public Optional<Autor> buscarPorId(Long id) {
        return autorRepository.findById(id);
    }

    public void deletar(Long id) {
        autorRepository.deleteById(id);
    }
}