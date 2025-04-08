package com.example.provan1.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Medicamento {
    private String codigo;
    private String nome;
    private String descricao;
    private String principioAtivo;
    private LocalDate dataValidade;
    private int quantidadeEstoque;
    private BigDecimal preco;
    private boolean controlado;
    private Fornecedor fornecedor;

    public Medicamento(String codigo,
                       String nome,
                       String descricao,
                       String principioAtivo,
                       LocalDate dataValidade,
                       int quantidadeEstoque,
                       BigDecimal preco,
                       boolean controlado,
                       Fornecedor fornecedor) {

        if (!codigo.matches("[A-Za-z0-9]{7}"))
            throw new IllegalArgumentException("Código deve conter 7 caracteres alfanuméricos");
        if (nome == null || nome.trim().length() < 3)
            throw new IllegalArgumentException("Nome não pode ser vazio ou menor que 3 caracteres");
        if (dataValidade.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Data de validade não pode ser passada");
        if (quantidadeEstoque < 0)
            throw new IllegalArgumentException("Quantidade em estoque não pode ser negativa");
        if (preco.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Preço deve ser maior que zero");

        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.principioAtivo = principioAtivo;
        this.dataValidade = dataValidade;
        this.quantidadeEstoque = quantidadeEstoque;
        this.preco = preco;
        this.controlado = controlado;
        this.fornecedor = fornecedor;

    }




    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getPrincipioAtivo() {
        return principioAtivo;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public boolean isControlado() {
        return controlado;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    @Override
    public String toString() {
        return nome + " [" + codigo + "] - Estoque: " + quantidadeEstoque;
    }
}
