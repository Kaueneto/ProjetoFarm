## Aplicação para gerenciar Medicamentos

Este é um sistema desktop desenvolvido em **JavaFX** que permite o gerenciamento de medicamentos e fornecedores em uma farmácia. Os dados são persistidos em arquivos CSV, garantindo facilidade e leveza na manipulação local.

Aplicação desenvolvida em JavaFX uqe permite o gerenciamento de medicamentos e fornecedores de uma farmacia, os dados sao armazenados em arquivos CSV.

## Funcionalidades

 cadastro de medicamentos
 cadastro de fornecedores
 pesquisa de medicamentos por código
Exclusão de medicamentos da tabela e do arquivo CSV
Relatórios:
  - Medicamentos controlados
  - Medicamentos com **estoque baixo (Considerado baixo menos de 5 unidades)**
  - Medicamentos **vencendo em até 30 dias**

## tecnologias utilizadas no desenvolvimento

- **java 17+**
- **javaFX**
- **FXML (SceneBuilder)**
- **java Collections / Streams**
- **persistência em CSV**
- **padrão DAO (Data Access Object) para manipulação de dados**



## 📝 Observações

- O arquivo `medicamentos.csv` deve existir na pasta `datacsv/` e conter os dados no seguinte formato:

```
Codigo;nome;descricao;principio Ativo;data Validade;quantidade Estoque;preco;controlado;razao Social Fornecedor;cnpj Fornecedor
```
assim como o arquivo fornecedor.csv:

```
id;Cnpj;razao social;num. telefone;email;cidade;estado
```

- o botão "Excluir Selecionado" remove o medicamento da tabela e atualiza o arquivo CSV automaticamente, assim como o de excluir fornecedor.

  
  ![image](https://github.com/user-attachments/assets/e67d571b-7cc1-4b7e-8224-2d2b5e73a628)


- relatórios são gerados em janelas separadas com base em filtros aplicados.

  
## Interfaces de cadastro:

![image](https://github.com/user-attachments/assets/87aefe85-7a50-4638-95f3-0386b3f4c2ea)
![image](https://github.com/user-attachments/assets/61d2086a-a9fe-4dac-805b-dcf1276cd62f)



