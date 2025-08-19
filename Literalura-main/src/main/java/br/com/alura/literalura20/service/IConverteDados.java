package br.com.alura.literalura20.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
