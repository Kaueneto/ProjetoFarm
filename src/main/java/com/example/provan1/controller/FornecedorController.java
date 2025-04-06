package com.example.provan1.controller;
import com.example.provan1.util.idGenerator;
import com.example.provan1.dao.FornecedorDAO;
import com.example.provan1.model.Fornecedor;
import com.example.provan1.util.CSVUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FornecedorController {

    @FXML
    private TextField txtRazaoSocial;

    @FXML
    private TextField txtCnpj;

    @FXML
    private TextField txtTelefone;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtCidade;

    @FXML
    private TextField txtEstado;

    @FXML
    private Button btnCadastrar;

    @FXML
    private Button btnFechar;


    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private static final String CAMINHO_CSV = "src/main/java/com/example/provan1/datacsv/fornecedores.csv";
    private final idGenerator idGenerator = new idGenerator();

    @FXML
    private void cadastrarFornecedor() {

        String razaoSocial = txtRazaoSocial.getText().trim();
        String cnpj = txtCnpj.getText().replaceAll("\\D", "");
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();
        String cidade = txtCidade.getText().trim();
        String estado = txtEstado.getText().trim();

        if (razaoSocial.isEmpty() || cnpj.isEmpty()) {
            exibirAlerta("Erro", "Preencha a razão social e o CNPJ.");
            return;
        }

        if (fornecedorDAO.buscarPorCNPJ(cnpj).isPresent()) {
            exibirAlerta("Erro", "Fornecedor com este CNPJ já está cadastrado.");
            return;
        }

        try {
            long id = idGenerator.generateID();
            Fornecedor fornecedor = new Fornecedor(id, cnpj, razaoSocial, telefone, email, cidade, estado);




            fornecedorDAO.getFornecedores().add(fornecedor);
            salvarCSV(); // salva com o ID também

            exibirAlerta("Sucesso", "Fornecedor cadastrado com sucesso!");
            limparCampos();
        } catch (IllegalArgumentException e) {
            exibirAlerta("Erro", e.getMessage());
        }
    }


    private void salvarCSV() {
        List<String[]> dados = new ArrayList<>();
        for (Fornecedor f : fornecedorDAO.getFornecedores()) {
            dados.add(new String[]{
                    String.valueOf(f.getID()),
                    f.getCnpj(),
                    f.getRazaoSocial(),
                    f.getTelefone(),
                    f.getEmail(),
                    f.getCidade(),
                    f.getEstado()
            });
        }

        CSVUtil.salvarCSV(CAMINHO_CSV, dados);
    }


    @FXML
    private void limparCampos() {
        txtRazaoSocial.clear();
        txtCnpj.clear();
        txtTelefone.clear();
        txtEmail.clear();
        txtCidade.clear();
        txtEstado.clear();
    }

    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) btnFechar.getScene().getWindow();
        stage.close();
    }

    private void exibirAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
