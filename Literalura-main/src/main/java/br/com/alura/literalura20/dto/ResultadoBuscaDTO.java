package br.com.alura.literalura20.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultadoBuscaDTO(
        @JsonAlias("results") List<DadosLivroDTO> resultados
) {
}
