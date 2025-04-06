package com.example.provan1.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CSVUtil {

    public static List<String[]> lerCSV(String caminhoArquivo) {
        List<String[]> linhas = new ArrayList<>();

        try (BufferedReader leitor = Files.newBufferedReader(Paths.get(caminhoArquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                // Separando por ponto e vírgula
                String[] campos = linha.split(";");
                linhas.add(campos);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        }

        return linhas;
    }

    public static void salvarCSV(String caminhoArquivo, List<String[]> dados) {
        try (BufferedWriter escritor = Files.newBufferedWriter(Paths.get(caminhoArquivo))) {
            for (String[] linha : dados) {
                String linhaCSV = String.join(";", linha);
                escritor.write(linhaCSV);
                escritor.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo CSV: " + e.getMessage());
        }
    }
}
