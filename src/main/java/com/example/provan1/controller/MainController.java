package com.example.provan1.controller;

import com.example.provan1.model.Fornecedor;
import com.example.provan1.model.Medicamento;
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
    private MenuItem menuitemCadastrarMed;

    @FXML
    private MenuItem menuItemCadastrarforn;

    @FXML
    private TableView<Medicamento> tableMedicamentos;

    @FXML
    private TextField txtPesquisaCodigo;

    @FXML
    private Button btnExcluirSelecionado;

    private ObservableList<Medicamento> listaMedicamentos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabela();
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

    private void configurarTabela() {
        tableMedicamentos.setItems(listaMedicamentos);
    }

    private void carregarDadosDoCSV() {
        listaMedicamentos.clear();

        String caminho = Paths.get(System.getProperty("user.dir"), "datacsv", "medicamentos.csv").toString();

        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] partes = linha.split(";");
                if (partes.length != 10) continue;

                Fornecedor fornecedor = new Fornecedor(partes[9], partes[8]);

                Medicamento m = new Medicamento(
                        partes[0], // código
                        partes[1], // nome
                        partes[2], // descrição
                        partes[3], // princípio ativo
                        LocalDate.parse(partes[4]), // validade
                        Integer.parseInt(partes[5]), // estoque
                        new BigDecimal(partes[6]), // preço
                        Boolean.parseBoolean(partes[7]), // controlado
                        fornecedor // ← agora passando um Fornecedor mesmo
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
            carregarDadosDoCSV(); // Mostra todos
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
        }
    }

    private void salvarListaNoCSV() {
        String caminho = Paths.get(System.getProperty("user.dir"), "datacsv", "medicamentos.csv").toString();

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
                        m.getFornecedor().getCnpj()
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar medicamentos no arquivo CSV.");
        }
    }
}
