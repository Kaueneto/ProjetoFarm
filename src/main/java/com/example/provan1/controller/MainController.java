package com.example.provan1.controller;

import com.example.provan1.model.Fornecedor;
import com.example.provan1.model.Medicamento;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableColumn<Medicamento, String> colCodigo;
    @FXML
    private TableColumn<Medicamento, String> colNome;
    @FXML
    private TableColumn<Medicamento, String> colDescricao;
    @FXML
    private TableColumn<Medicamento, String> colPrincipio;
    @FXML
    private TableColumn<Medicamento, String> colValidade;
    @FXML
    private TableColumn<Medicamento, Integer> colEstoque;
    @FXML
    private TableColumn<Medicamento, String> colPreco;
    @FXML
    private TableColumn<Medicamento, String> colControlado;
    @FXML
    private TableColumn<Medicamento, String> colFornecedor;
    @FXML
    private TableColumn<Medicamento, String> colCnpj;

    @FXML
    private MenuItem menuitemCadastrarMed;
    @FXML
    private MenuItem menuItemCadastrarforn;
    @FXML
    private TableView<Medicamento> tableMedicamentos;
    @FXML
    private TextField txtPesquisaCodigo;
    @FXML
    private Button btnExcluirSelecionado;
    @FXML
    private Button pesquisarmed;

    private ObservableList<Medicamento> listaMedicamentos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCodigo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCodigo()));
        colNome.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
        colDescricao.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescricao()));
        colPrincipio.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPrincipioAtivo()));
        colValidade.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDataValidade().toString()));
        colEstoque.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getQuantidadeEstoque()).asObject());
        colPreco.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPreco().toString()));
        colControlado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isControlado() ? "Sim" : "Não"));
        colFornecedor.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFornecedor().getRazaoSocial()));
        colCnpj.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFornecedor().getCnpj()));

        tableMedicamentos.setItems(listaMedicamentos);
        carregarDadosDoCSV();

        btnExcluirSelecionado.setOnAction(e -> excluirSelecionado());
    }

    @FXML
    private void Abrircadastromedicamento() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/provan1/MedicamentoView.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Medicamento");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao abrir a tela de cadastro de medicamento.");
        }
    }

    @FXML
    private void AbrirCadastroForn() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/provan1/fornecedor.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Cadastro de fornecedores");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao abrir a tela de cadastro de fornecedores.");
        }
    }

    private void carregarDadosDoCSV() {
        listaMedicamentos.clear();

        String caminho = Paths.get("src", "main", "java", "com", "example", "provan1", "datacsv", "medicamentos.csv").toString();

        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] partes = linha.split(";");
                if (partes.length != 10) continue;

                Fornecedor fornecedor = new Fornecedor(partes[9], partes[8]);

                Medicamento m = new Medicamento(
                        partes[0],
                        partes[1],
                        partes[2],
                        partes[3],
                        LocalDate.parse(partes[4]),
                        Integer.parseInt(partes[5]),
                        new BigDecimal(partes[6]),
                        Boolean.parseBoolean(partes[7]),
                        fornecedor
                );

                listaMedicamentos.add(m);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao ler medicamentos do arquivo CSV.");
        }
    }

    @FXML
    private void pesquisarMedicamentos() {
        String codigoBusca = txtPesquisaCodigo.getText().trim();

        if (codigoBusca.isEmpty()) {
            carregarDadosDoCSV();
        } else {
            ObservableList<Medicamento> filtrados = FXCollections.observableArrayList();
            for (Medicamento m : listaMedicamentos) {
                if (m.getCodigo().equalsIgnoreCase(codigoBusca)) {
                    filtrados.add(m);
                }
            }
            tableMedicamentos.setItems(filtrados);
        }
    }

    private void excluirSelecionado() {
        Medicamento selecionado = tableMedicamentos.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            listaMedicamentos.remove(selecionado);
            salvarListaNoCSV();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sucesso");
            alerta.setHeaderText(null);
            alerta.setContentText("Medicamento excluído com sucesso!");
            alerta.showAndWait();
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Nenhuma seleção");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecione um medicamento para excluir.");
            alerta.showAndWait();
        }
    }

    private void salvarListaNoCSV() {
        String caminho = Paths.get("src", "main", "java", "com", "example", "provan1", "datacsv", "medicamentos.csv").toString();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
            for (Medicamento m : listaMedicamentos) {
                writer.write(String.join(";",
                        m.getCodigo(),
                        m.getNome(),
                        m.getDescricao(),
                        m.getPrincipioAtivo(),
                        m.getDataValidade().toString(),
                        String.valueOf(m.getQuantidadeEstoque()),
                        m.getPreco().toString(),
                        String.valueOf(m.isControlado()),
                        m.getFornecedor().getRazaoSocial(),
                        m.getFornecedor().getCnpj()
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar medicamentos no arquivo CSV.");
        }
    }

    @FXML
    private void abrirRelatorioControlados() {
        abrirRelatorioComFiltro("controlado");
    }

    @FXML
    private void abrirRelatorioEstoqueBaixo() {
        abrirRelatorioComFiltro("estoque_baixo");
    }
    @FXML
    private void abrirRelatorioVencendo30Dias() {
        abrirRelatorioComFiltro("vencendo_30_dias");
    }
    private void abrirRelatorioComFiltro(String tipoRelatorio) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/provan1/Relatorio.fxml"));
            Parent root = loader.load();

            RelatorioController controller = loader.getController();
            controller.carregarDadosFiltrados(tipoRelatorio);

            Stage stage = new Stage();
            stage.setTitle("Relatório - " + tipoRelatorio);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}