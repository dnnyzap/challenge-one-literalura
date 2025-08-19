package br.com.alura.literalura20.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivroDTO(
        @JsonAlias("id") Long id,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DadosAutorDTO> autores,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Double numeroDownloads
) {
}
