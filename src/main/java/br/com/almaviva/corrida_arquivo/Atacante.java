package br.com.almaviva.corrida_arquivo;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.almaviva.corrida_arquivo.controller.Alvos;
import br.com.almaviva.corrida_arquivo.controller.Conexao;
import br.com.almaviva.corrida_arquivo.service.ConexaoService;

public class Atacante {
    private static final Logger logger = LoggerFactory.getLogger(Atacante.class);
    private static final String ARQUIVO_ALVOS = "src/main/resources/alvos.txt";

    public static void main(String[] args) {
        List<Conexao> conexoes = Alvos.carregarConexoes(ARQUIVO_ALVOS);

        if (conexoes.isEmpty()) {
            logger.error("Nenhuma conexão encontrada. Atualize o arquivo alvos.txt.");
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(100);

        for (Conexao conexao : conexoes) {
            executor.execute(() -> {
                try (ConexaoService conexaoService = new ConexaoService(conexao)) {
                    conexaoService.conectar();

                    while (true) {
                        String nomeArquivo = "gustavo_" + System.currentTimeMillis() + ".txt";
                        conexaoService.criarArquivoRemoto("/home/" + conexao.getUsuario() + "/GuerraArquivos/", nomeArquivo);
                        Thread.sleep(1); 
                    }
                } catch (Exception e) {
                    logger.error("Erro ao processar conexão: {}", e.getMessage());
                }
            });
        }

        executor.shutdown();
    }
}
