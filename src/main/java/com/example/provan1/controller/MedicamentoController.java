package com.example.provan1.controller;

import com.example.provan1.dao.FornecedorDAO;
import com.example.provan1.dao.MedicamentoDAO;
import com.example.provan1.model.Fornecedor;
import com.example.provan1.model.Medicamento;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MedicamentoController implements Initializable {

    @FXML
    private Button btnCadastrar;
    @FXML
    private TextField txtcodigo;
    @FXML
    private TextField txtNomemedicamento;
    @FXML
    private DatePicker txtDataValidade;
    @FXML
    private TextField txtDescricao;
    @FXML
    private TextField txtPriAtivo;
    @FXML
    private TextField txtPreco;
    @FXML
    private Spinner<Integer> SpinerQtdeEstoque;
    @FXML
    private CheckBox checkControlado;
    @FXML
    private ChoiceBox<String> choicefornecedor;
    @FXML
    private TextField txtCodForn; // novo campo para inserir o ID do fornecedor     e buscar

    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private final MedicamentoDAO medicamentoDAO = new MedicamentoDAO(fornecedorDAO);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fornecedorDAO.carregarFornecedores();
        medicamentoDAO.carregarMedicamentos();

        choicefornecedor.getItems().addAll(
                fornecedorDAO.getFornecedores().stream()
                        .map(Fornecedor::getRazaoSocial)
                        .toList()
        );

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999, 0);
        SpinerQtdeEstoque.setValueFactory(valueFactory);
    }

    @FXML
    private void btnCadastrarMedicamento() {
        try {
            String codigo = txtcodigo.getText().trim();
            if (!codigo.matches("[a-zA-Z0-9]{7}")) {
                throw new IllegalArgumentException("O código deve ter exatamente 7 caracteres alfanuméricos (letras ou números)");
            }

            String nome = txtNomemedicamento.getText();
            String descricao = txtDescricao.getText();
            String principioAtivo = txtPriAtivo.getText();
            LocalDate validade = txtDataValidade.getValue();
            int quantidade = SpinerQtdeEstoque.getValue();
            BigDecimal preco = new BigDecimal(txtPreco.getText());
            boolean controlado = checkControlado.isSelected();
            String nomeFornecedor = choicefornecedor.getValue();

            Fornecedor fornecedor = fornecedorDAO.buscarPorRazaoSocial(nomeFornecedor)
                    .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

            Medicamento novoMedicamento = new Medicamento(
                    codigo, nome, descricao, principioAtivo, validade, quantidade, preco, controlado, fornecedor
            );

            medicamentoDAO.adicionar(novoMedicamento);
            medicamentoDAO.salvarMedicamentos();

            System.out.println("Medicamento cadastrado com sucesso!");
            limparCampos();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao cadastrar medicamento:\n" + e.getMessage());
        }
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    @FXML
    private void verificarFornecedorPorId() {
        String texto = txtCodForn.getText().trim();

        if (!texto.isEmpty() && texto.matches("\\d+")) {
            long id = Long.parseLong(texto);

            fornecedorDAO.getFornecedores().stream()
                    .filter(f -> f.getID() == id)
                    .findFirst()
                    .ifPresentOrElse(fornecedor -> {
                        choicefornecedor.getItems().setAll(fornecedor.getRazaoSocial());
                        choicefornecedor.setValue(fornecedor.getRazaoSocial());
                    }, () -> {
                        //choicefornecedor.getItems().setAll("");
                        choicefornecedor.setValue("Fornecedor não encontrado");
                    });
        } else {
            choicefornecedor.getItems().clear();
        }
    }

    private void limparCampos() {
        txtcodigo.clear();
        txtNomemedicamento.clear();
        txtDescricao.clear();
        txtPriAtivo.clear();
        txtDataValidade.setValue(null);
        SpinerQtdeEstoque.getValueFactory().setValue(0);
        checkControlado.setSelected(false);
        txtCodForn.clear();
        choicefornecedor.setValue(null);
    }

    public Button getBtnCadastrar() {
        return btnCadastrar;
    }

    public void setBtnCadastrar(Button btnCadastrar) {
        this.btnCadastrar = btnCadastrar;
    }
}
