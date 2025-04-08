package com.example.provan1.dao;

import com.example.provan1.model.Medicamento;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioDAO {

    private final MedicamentoDAO medicamentoDAO;

    public RelatorioDAO() {
        this.medicamentoDAO = new MedicamentoDAO(new FornecedorDAO()); // ou receber via construtor
    }

    public List<Medicamento> listarControlados() {
        return medicamentoDAO.carregarMedicamentos().stream()
                .filter(Medicamento::isControlado)
                .collect(Collectors.toList());
    }

    public List<Medicamento> listarEstoqueBaixo(int limite) {
        return medicamentoDAO.carregarMedicamentos().stream()
                .filter(m -> m.getQuantidadeEstoque() < limite)
                .collect(Collectors.toList());
    }



    public List<Medicamento> listarVencendoProximos30Dias() {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(30);

        return medicamentoDAO.getMedicamentos().stream()
                .filter(m -> {
                    LocalDate validade = m.getDataValidade();
                    return validade != null && !validade.isBefore(hoje) && !validade.isAfter(limite);
                })
                .toList();
    }


}