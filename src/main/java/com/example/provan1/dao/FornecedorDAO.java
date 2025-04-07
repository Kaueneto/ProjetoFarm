package com.example.provan1.dao;

import com.example.provan1.model.Fornecedor;
import com.example.provan1.util.CSVUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FornecedorDAO {
    private static final String CAMINHO_CSV = "src/main/java/com/example/provan1/datacsv/fornecedores.csv";
    private final List<Fornecedor> fornecedores;

    public FornecedorDAO() {
        fornecedores = carregarFornecedores();
    }

    public List<Fornecedor> getFornecedores() {
        return fornecedores;
    }

    public List<Fornecedor> carregarFornecedores() {
        List<String[]> dados = CSVUtil.lerCSV(CAMINHO_CSV);
        List<Fornecedor> lista = new ArrayList<>();

        for (String[] linha : dados) {
            if (linha.length < 7) {
                System.err.println("Linha inválida no CSV de fornecedores, ignorada: " + String.join(";", linha));
                continue;
            }

            try {
                long id = Long.parseLong(linha[0]);
                String cnpj = linha[1];
                String razaoSocial = linha[2];
                String telefone = linha[3];
                String email = linha[4];
                String cidade = linha[5];
                String estado = linha[6];

                lista.add(new Fornecedor(id, cnpj, razaoSocial, telefone, email, cidade, estado));
            } catch (Exception e) {
              //  System.err.println("Erro ao carregar fornecedor (linha ignorada): " + e.getMessage());
            }
        }

        return lista;
    }

    public Optional<Fornecedor> buscarPorCNPJ(String cnpj) {
        return fornecedores.stream()
                .filter(f -> f.getCnpj().equals(cnpj))
                .findFirst();
    }
    public Optional<Fornecedor> buscarPorRazaoSocial(String razaoSocial) {
        return fornecedores.stream()
                .filter(f -> f.getRazaoSocial().equalsIgnoreCase(razaoSocial))
                .findFirst();
    }
}
