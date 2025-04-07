package com.example.provan1.model;

public class Fornecedor {
    public long id;
    private String cnpj;
    private String razaoSocial;
    private String telefone;
    private String email;
    private String cidade;
    private String estado;

    public Fornecedor(long id, String cnpj, String razaoSocial, String telefone, String email, String cidade, String estado) {
        this.id = id;
        this.cnpj = validarcnpj(cnpj);
        this.razaoSocial = razaoSocial;
        this.telefone = telefone;
        this.email = email;
        this.cidade = cidade;
        this.estado = estado;
    }

    public Fornecedor(String razaoSocial, String cnpj) {
        this.id = 0L;
        this.razaoSocial = razaoSocial;
        this.cnpj = validarcnpj(cnpj);
        this.telefone = "";
        this.email = "";
        this.cidade = "";
        this.estado = "";
    }

    private String validarcnpj(String cnpjOriginal) {
        if (cnpjOriginal == null) {
            System.out.println("Aviso: CNPJ nulo fornecido.");
            return "00000000000000";
        }

       String cnpjNumerico = cnpjOriginal.replaceAll("\\D", "");

        if (cnpjNumerico.length() != 14) {
            System.out.println("Aviso: CNPJ inválido fornecido: " + cnpjOriginal);
            return "00000000000000"; // Ou retornar "" se preferir
        }

        return cnpjNumerico;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public long getID() {
        return id;
    }

    @Override
    public String toString() {
        return razaoSocial + " (" + cnpj + ")";
    }
}