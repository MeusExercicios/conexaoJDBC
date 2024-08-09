package conexao.Banco;
// CONEXAO, CONSULTA E INSERÇÃO NUM BANCO DE DADOS
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // URL de conexão com o banco de dados PostgreSQL
        String url = "jdbc:postgresql://localhost:5432/bancoProjeto";
        // Nome de usuário e senha para conectar ao banco de dados
        String user = "postgres";
        String password = "admin";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Carrega o driver JDBC para PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Estabelece a conexão com o banco de dados
            connection = DriverManager.getConnection(url, user, password);

            // Cria um Statement para executar a consulta
            statement = connection.createStatement();

            // Escolha de operação
            System.out.println("Escolha uma opção:");
            System.out.println("1. Consultar dados");
            System.out.println("2. Inserir dados");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            if (choice == 1) {
                // Define a consulta SQL
                String sql = "SELECT * FROM userbancoprojeto";

                // Executa a consulta e obtém o ResultSet
                resultSet = statement.executeQuery(sql);

                // Processa o ResultSet
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nome = resultSet.getString("nome");
                    String email = resultSet.getString("email");

                    // Exibe os dados
                    System.out.println("ID: " + id + ", Nome: " + nome + ", Email: " + email);
                }
            } else if (choice == 2) {
                // Coleta os dados a serem inseridos
                System.out.print("Digite o id: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Digite o nome: ");
                String nome = scanner.nextLine();
                System.out.print("Digite o email: ");
                String email = scanner.nextLine();

                // Define o comando SQL de inserção
                String sql = "INSERT INTO userbancoprojeto (id, nome, email) VALUES (?, ?, ?)";

                // Cria o PreparedStatement
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, nome);
                preparedStatement.setString(3, email);

                // Executa o comando SQL de inserção
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Dados inseridos com sucesso! Linhas afetadas: " + rowsAffected);

                // Fecha o PreparedStatement
                preparedStatement.close();
            } else {
                System.out.println("Opção inválida.");
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver do banco não localizado!");
            ex.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Erro na conexão ou na execução do comando SQL.");
            e.printStackTrace();
        } finally {
            // Fecha o ResultSet, Statement e Connection
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Fecha o Scanner
            scanner.close();
        }
    }
}
