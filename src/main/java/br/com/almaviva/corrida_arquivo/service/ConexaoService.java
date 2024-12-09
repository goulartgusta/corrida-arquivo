package br.com.almaviva.corrida_arquivo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import br.com.almaviva.corrida_arquivo.controller.Conexao;

public class ConexaoService implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(ConexaoService.class);
    private final Conexao conexao;
    private Session sessao;

    public ConexaoService(Conexao conexao) {
        this.conexao = conexao;
    }

    public void conectar() throws JSchException {
        JSch jsch = new JSch();
        sessao = jsch.getSession(conexao.getUsuario(), conexao.getHost());
        sessao.setPassword(conexao.getSenha());
        sessao.setConfig("StrictHostKeyChecking", "no");
        sessao.connect();
        logger.info("Conectado a {}", conexao.getHost());
    }

    public void criarArquivoRemoto(String diretorioRemoto, String nomeArquivo) {
        try {
            String comando = String.format("echo 'Conteúdo do arquivo: %s' > %s%s", nomeArquivo, diretorioRemoto, nomeArquivo);
            ChannelExec canal = (ChannelExec) sessao.openChannel("exec");
            canal.setCommand(comando);
            canal.connect();

            canal.disconnect();
            logger.info("Arquivo criado remotamente: {}/{}", diretorioRemoto, nomeArquivo);
        } catch (Exception e) {
            logger.error("Erro ao criar arquivo remoto: {}", e.getMessage());
        }
    }

    @Override
    public void close() {
        if (sessao != null && sessao.isConnected()) {
            sessao.disconnect();
            logger.info("Sessão encerrada.");
        }
    }
}
