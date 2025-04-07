package com.example.provan1.controller;

import com.example.provan1.dao.RelatorioDAO;
import com.example.provan1.model.Medicamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;

import java.util.List;

public class RelatorioController {

    @FXML
    private TableView<Medicamento> tableRelatorio;
    @FXML
    private TableColumn<Medicamento, String> colCodigo;
    @FXML
    private TableColumn<Medicamento, String> colNome;
    @FXML
    private TableColumn<Medicamento, String> colDescricao;
    @FXML
    private TableColumn<Medicamento, String> colValidade;
    @FXML
    private TableColumn<Medicamento, Integer> colEstoque;
    @FXML
    private TableColumn<Medicamento, String> colControlado;
    @FXML
    private TableColumn<Medicamento, String> colPrincipio;
    @FXML
    private TableColumn<Medicamento, String> colPreco;
    @FXML
    private TableColumn<Medicamento, String> colCnpj;
    @FXML
    private TableColumn<Medicamento, String> colFornecedor;
    @FXML
    private Button btnfecharelatorio;





    private final RelatorioDAO relatorioDAO = new RelatorioDAO();

    public void carregarDadosFiltrados(String tipo) {
        List<Medicamento> dados = switch (tipo) {
            case "controlado" -> relatorioDAO.listarControlados();
            case "estoque_baixo" -> relatorioDAO.listarEstoqueBaixo(10);
            case "vencendo_30_dias" -> relatorioDAO.listarVencendoProximos30Dias();

            default -> List.of();

        };

        ObservableList<Medicamento> observableList = FXCollections.observableArrayList(dados);
        tableRelatorio.setItems(observableList);

        configurarColunas();

    }

    private void configurarColunas() {
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colDescricao.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescricao()));
        colValidade.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDataValidade().toString()));
        colEstoque.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getQuantidadeEstoque()).asObject());
        colControlado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isControlado() ? "Sim" : "Não"));
        colPrincipio.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPrincipioAtivo()));
        colPreco.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPreco().toString()));

        colCnpj.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFornecedor().getCnpj()));
        colFornecedor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFornecedor().getRazaoSocial()));
    }
    @FXML
    private void sairelatorio() {
        Stage stage = (Stage) btnfecharelatorio.getScene().getWindow();
        stage.close();
    }
}