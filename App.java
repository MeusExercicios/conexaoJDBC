package conexao.Banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/bancoProjeto";
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
            System.out.println("3. Atualizar dados");
            System.out.println("4. Deletar dados");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            if (choice == 1) {
                String sql = "SELECT * FROM userbancoprojeto";
                resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nome = resultSet.getString("nome");
                    String email = resultSet.getString("email");

                    System.out.println("ID: " + id + ", Nome: " + nome + ", Email: " + email);
                }
            } else if (choice == 2) {
                System.out.print("Digite o id: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Digite o nome: ");
                String nome = scanner.nextLine();
                System.out.print("Digite o email: ");
                String email = scanner.nextLine();

                String sql = "INSERT INTO userbancoprojeto (id, nome, email) VALUES (?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, nome);
                preparedStatement.setString(3, email);

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Dados inseridos com sucesso! Linhas afetadas: " + rowsAffected);

                preparedStatement.close();
            } else if (choice == 3) {
                System.out.println("Qual id deseja atualizar ?");
                int atualizarId = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Digite o novo nome: ");
                String novoNome = scanner.nextLine();

                System.out.print("Digite o novo email: ");
                String novoEmail = scanner.nextLine();

                String sql = "UPDATE userbancoprojeto SET nome = ?, email = ? WHERE id = ?";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, novoNome);
                preparedStatement.setString(2, novoEmail);
                preparedStatement.setInt(3, atualizarId);

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Atualizado com sucesso! Linhas afetadas: " + rowsAffected);

                preparedStatement.close();
            } else if (choice == 4) {
                System.out.println("Qual o id do registro que deseja deletar ?");
                int deletarRegistroId = scanner.nextInt();
                scanner.nextLine();

                String sql = "DELETE FROM userbancoprojeto WHERE id = ?";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setInt(1,deletarRegistroId);

                int rowsAffected = preparedStatement.executeUpdate();

                System.out.println("Deletado com sucesso! Linhas afetadas: " + rowsAffected);

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
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            scanner.close();
        }
    }
}
