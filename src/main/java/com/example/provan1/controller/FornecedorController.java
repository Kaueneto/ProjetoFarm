package com.example.provan1.controller;

import com.example.provan1.util.idGenerator;
import com.example.provan1.dao.FornecedorDAO;
import com.example.provan1.model.Fornecedor;
import com.example.provan1.util.CSVUtil;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FornecedorController implements Initializable {

    @FXML private TextField txtRazaoSocial;
    @FXML private TextField txtCnpj;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCidade;
    @FXML private TextField txtEstado;
    @FXML private Button btnCadastrar;
    @FXML private Button btnFechar;

    @FXML private TableView<Fornecedor> tableFornecedores;
    @FXML private TableColumn<Fornecedor, Long> colId;
    @FXML private TableColumn<Fornecedor, String> colRazaoSocial;
    @FXML private TableColumn<Fornecedor, String> colCnpj;
    @FXML private TableColumn<Fornecedor, String> colTelefone;
    @FXML private TableColumn<Fornecedor, String> colEmail;
    @FXML private TableColumn<Fornecedor, String> colCidade;
    @FXML private TableColumn<Fornecedor, String> colEstado;

    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private static final String CAMINHO_CSV = "src/main/java/com/example/provan1/datacsv/fornecedores.csv";
    private final idGenerator idGenerator = new idGenerator();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fornecedorDAO.carregarFornecedores();
        configurarTabela();
    }

    private void configurarTabela() {
        if (tableFornecedores != null) {
            colId.setCellValueFactory(f -> new SimpleLongProperty(f.getValue().getID()).asObject());
            colRazaoSocial.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getRazaoSocial()));
            colCnpj.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getCnpj()));
            colTelefone.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getTelefone()));
            colEmail.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getEmail()));
            colCidade.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getCidade()));
            colEstado.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getEstado()));

            tableFornecedores.setItems(FXCollections.observableArrayList(fornecedorDAO.getFornecedores()));
        }
    }

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
            salvarCSV();

            exibirAlerta("Sucesso", "Fornecedor cadastrado com sucesso!");
            limparCampos();
            configurarTabela(); // Atualiza a tabela com o novo cadastro
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