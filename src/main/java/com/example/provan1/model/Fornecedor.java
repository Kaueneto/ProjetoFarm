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
        if (!cnpjValido(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido");
        }
        this.id = id;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.telefone = telefone;
        this.email = email;
        this.cidade = cidade;
        this.estado = estado;
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

    private boolean cnpjValido(String cnpj) {
        return cnpj != null && cnpj.matches("\\d{14}");
    }

    @Override
    public String toString() {
        return razaoSocial + " (" + cnpj + ")";
    }


    public long getID() {
        return id;

    }


}
