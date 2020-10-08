import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProjectMemberDao {

    public List<ProjectMember> projectMemberList;
    private DataSource dataSource;

    public ProjectMemberDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[] args) throws SQLException {

        // Initialize PGSimpleDataSource
        PGSimpleDataSource pgDataSource = new PGSimpleDataSource();
        pgDataSource.setUrl("jdbc:postgresql://localhost:5432/project");
        pgDataSource.setUser("oppgavesett08");
        pgDataSource.setPassword("nw3fGmA9nKgbwtGwpj");

        // Prompt value
        System.out.println("Enter value to insert:");
        Scanner scanner = new Scanner(System.in);
        String projectMemberName = scanner.nextLine();

        // Insert into value DB
        try (Connection connection = pgDataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into project_members (name) " + "values (?)")) {
                statement.setString(1, projectMemberName);
                statement.executeUpdate();
            }
        }

        //Print all values
        try (Connection connection = pgDataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from project_members")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<ProjectMember> projectMemberList = new ArrayList<>();

                    while(resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String role = resultSet.getString("role");

                        System.out.println("#" + id + ": " + name);
                    }
                }
            }
        }

    }

    public List<ProjectMember> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from project_members")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<ProjectMember> projectMemberList = new ArrayList<>();

                    while(resultSet.next()) {
                        ProjectMember projectMember = new ProjectMember();
                        projectMember.setId(resultSet.getInt("id"));
                        projectMember.setName(resultSet.getString("name"));
                        projectMember.setRole(resultSet.getString("role"));

                        projectMemberList.add(projectMember);
                    }

                    return projectMemberList;
                }
            }
        }
    }

    public void insert(String projectMemberName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into project_members (name) " + "values (?)")) {
                statement.setString(1, projectMemberName);
                statement.executeUpdate();
            }
        }
    }
}
