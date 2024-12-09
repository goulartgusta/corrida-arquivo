package br.com.almaviva.corrida_arquivo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Alvos {
    private static final Logger logger = LoggerFactory.getLogger(Alvos.class);

    public static List<Conexao> carregarConexoes(String arquivoAlvos) {
        List<Conexao> conexoes = new ArrayList<>();
        File arquivo = new File(arquivoAlvos);

        if (!arquivo.exists()) {
            logger.error("Arquivo alvos.txt não encontrado em: {}", arquivoAlvos);
            return conexoes;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length == 3) {
                    conexoes.add(new Conexao(partes[0], partes[1], partes[2]));
                } else {
                    logger.warn("Linha inválida em alvos.txt: {}", linha);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao carregar conexões: {}", e.getMessage());
        }

        return conexoes;
    }
}
