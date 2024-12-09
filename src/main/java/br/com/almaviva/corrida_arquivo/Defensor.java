package br.com.almaviva.corrida_arquivo;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Defensor {

    private static final String PASTA_PUBLICA = "/home/almaviva-linux/GuerraArquivos";

    public static void main(String[] args) {
        Path pasta = Paths.get(PASTA_PUBLICA);

        if (!Files.exists(pasta)) {
            System.err.println("Pasta pública não encontrada: " + PASTA_PUBLICA);
            return;
        }

        System.out.println("Monitorando a pasta pública...");

        while (true) {
            File[] arquivos = pasta.toFile().listFiles();
            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    processarItem(arquivo);
                }
            }

            try {
                Thread.sleep(1); 
            } catch (InterruptedException e) {
                System.err.println("Erro ao pausar o monitoramento: " + e.getMessage());
            }
        }
    }

    private static void processarItem(File item) {
        try {
            if (item.isDirectory()) {
                excluirDiretorioRecursivamente(item);
            } else {
                Files.delete(item.toPath());
                System.out.println("Arquivo removido: " + item.getName());
            }
        } catch (Exception e) {
            System.err.println("Erro ao processar " + item.getName() + ": " + e.getMessage());
        }
    }

    private static void excluirDiretorioRecursivamente(File diretorio) {
        File[] conteudo = diretorio.listFiles();
        if (conteudo != null) {
            for (File item : conteudo) {
                processarItem(item);
            }
        }
        try {
            Files.delete(diretorio.toPath());
            System.out.println("Diretório removido: " + diretorio.getName());
        } catch (Exception e) {
            System.err.println("Erro ao remover diretório " + diretorio.getName() + ": " + e.getMessage());
        }
    }
}
