package com.example.provan1.dao;

import com.example.provan1.model.Fornecedor;
import com.example.provan1.model.Medicamento;
import com.example.provan1.util.CSVUtil;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MedicamentoDAO {
    private static final String CAMINHO_CSV = "src/main/java/com/example/provan1/datacsv/medicamentos.csv";
    private List<Medicamento> medicamentos;
    private FornecedorDAO fornecedorDAO;

    public MedicamentoDAO(FornecedorDAO fornecedorDAO) {
        this.fornecedorDAO = fornecedorDAO;
        this.medicamentos = carregarMedicamentos();
    }

    public void adicionar(Medicamento medicamento) {
        medicamentos.add(medicamento);
        salvar();
    }

    public void remover(String codigo) {
        medicamentos.removeIf(m -> m.getCodigo().equals(codigo));
        salvar();
    }

    public Optional<Medicamento> buscarPorCodigo(String codigo) {
        return medicamentos.stream()
                .filter(m -> m.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    public List<Medicamento> listarTodos() {
        return medicamentos;
    }

    public List<Medicamento> carregarMedicamentos() {
        List<String[]> dados = CSVUtil.lerCSV(CAMINHO_CSV);
        List<Medicamento> lista = new ArrayList<>();

        for (String[] linha : dados) {
            try {
                String cnpj = linha[8];
                Optional<Fornecedor> f = fornecedorDAO.buscarPorCNPJ(cnpj);

                if (f.isPresent()) {
                    lista.add(new Medicamento(
                            linha[0], linha[1], linha[2], linha[3],
                            LocalDate.parse(linha[4]),
                            Integer.parseInt(linha[5]),
                            new BigDecimal(linha[6]),
                            Boolean.parseBoolean(linha[7]),
                            f.get()
                    ));
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar medicamento: " + e.getMessage());
            }
        }

        return lista;
    }

    private void salvar() {
        List<String[]> dados = new ArrayList<>();
        for (Medicamento m : medicamentos) {
            dados.add(new String[]{
                    m.getCodigo(), m.getNome(), m.getDescricao(), m.getPrincipioAtivo(),
                    m.getDataValidade().toString(), String.valueOf(m.getQuantidadeEstoque()),
                    m.getPreco().toString(), String.valueOf(m.isControlado()),
                    m.getFornecedor().getCnpj(),
                    m.getFornecedor().getRazaoSocial()
            });
        }

        CSVUtil.salvarCSV(CAMINHO_CSV, dados);
    }

    // Relatórios com Stream API

    public List<Medicamento> medicamentosVencendo30Dias() {
        LocalDate hoje = LocalDate.now();
        return medicamentos.stream()
                .filter(m -> !m.getDataValidade().isBefore(hoje) &&
                        m.getDataValidade().isBefore(hoje.plusDays(30)))
                .collect(Collectors.toList());
    }

    public List<Medicamento> estoqueBaixo() {
        return medicamentos.stream()
                .filter(m -> m.getQuantidadeEstoque() < 5)
                .collect(Collectors.toList());
    }

    public Map<Fornecedor, BigDecimal> valorTotalEstoquePorFornecedor() {
        return medicamentos.stream()
                .collect(Collectors.groupingBy(
                        Medicamento::getFornecedor,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                m -> m.getPreco().multiply(BigDecimal.valueOf(m.getQuantidadeEstoque())),
                                BigDecimal::add
                        )
                ));
    }

    public Map<Boolean, List<Medicamento>> medicamentosControlados() {
        return medicamentos.stream()
                .collect(Collectors.groupingBy(Medicamento::isControlado));
    }

    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public boolean excluir(String codigo) {
        boolean removido = medicamentos.removeIf(m -> m.getCodigo().equals(codigo));
        if (removido) {
            salvarMedicamentos(); // Atualiza CSV se remover
        }
        return removido;
    }
    public void salvarMedicamentos() {
        List<String[]> dados = medicamentos.stream()
                .map(m -> new String[]{
                        m.getCodigo(),
                        m.getNome(),
                        m.getDescricao(),
                        m.getPrincipioAtivo(),
                        m.getDataValidade().toString(),
                        String.valueOf(m.getQuantidadeEstoque()),
                        m.getPreco().toString(),
                        String.valueOf(m.isControlado()),
                        m.getFornecedor().getCnpj(),
                        m.getFornecedor().getRazaoSocial()
                })
                .collect(Collectors.toList());

        CSVUtil.salvarCSV(CAMINHO_CSV, dados);
    }


}
