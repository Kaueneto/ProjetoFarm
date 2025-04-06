package com.example.provan1.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathFXML {

    public static String pathBase() {
        return Paths.get("src/main/resources/com/example/provan1/Medicamento.fxml").toAbsolutePath().toString();
    }
    public final Path pathMedicamentos = Paths.get("src/main/java/com/example/provan1/datacsv/medicamentos.csv");


}