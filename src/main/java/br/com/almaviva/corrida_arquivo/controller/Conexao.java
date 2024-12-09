package br.com.almaviva.corrida_arquivo.controller;

public class Conexao {
    private final String usuario;
    private final String host;
    private final String senha;

    public Conexao(String usuario, String host, String senha) {
        this.usuario = usuario;
        this.host = host;
        this.senha = senha;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getHost() {
        return host;
    }

    public String getSenha() {
        return senha;
    }
}
