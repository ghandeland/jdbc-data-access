import jdk.jfr.StackTrace;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectMemberDaoTest {
    private ProjectMemberDao projectMemberDao;

    public ProjectMemberDaoTest() throws SQLException { }

    private JdbcDataSource createTestDataSource() throws SQLException {

        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    @Test
    void shouldListSavedMembers() throws SQLException {
        JdbcDataSource ds = createTestDataSource();
        projectMemberDao = new ProjectMemberDao(ds);

        String projectMemberName = samplePersonName();
        System.out.println(projectMemberName);

        projectMemberDao.insert(projectMemberName);
        List<ProjectMember> pmList = projectMemberDao.listAll();
        assertThat(projectMemberDao.listNames()).contains(projectMemberName);
        projectMemberDao.printAll();
    }

    @Test
    void shouldListSavedNameAndRole() throws SQLException {
        JdbcDataSource ds = createTestDataSource();
        projectMemberDao = new ProjectMemberDao(ds);

        String projectMemberName = samplePersonName();
        String projectMemberRole = sampleRole();

        projectMemberDao.insert(projectMemberName, projectMemberRole);
        assertThat(projectMemberDao.listNames()).contains(projectMemberName);
        assertThat(projectMemberDao.listRoles()).contains(projectMemberRole);

        projectMemberDao.printAll();
    }

    @Test
    void shouldRetrieveSingleMember() throws SQLException {
        JdbcDataSource ds = createTestDataSource();
        projectMemberDao = new ProjectMemberDao(ds);

        ProjectMember projectMember = sampleProjectMember();
        projectMemberDao.insert(projectMember);

        projectMemberDao.printAll();

        assertThat(projectMemberDao.retrieve(projectMember.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(projectMember);
    }

    private ProjectMember sampleProjectMember() {
        return new ProjectMember(samplePersonName(), sampleRole());
    }

    private String samplePersonName() {
        String[] options = {"John Green", "Willy Smith", "Johnny Greenwood", "Jonas Peteson", "Gavin G.", "Taquisha H.", "Lasse S."};
        Random random = new Random();
        return options[random.nextInt(options.length)];
    }

    private String sampleRole() {
        String[] options = {"Architect", "Designer", "Manager", "HR", "UX", "Developer", "PR", "Engineering"};
        Random random = new Random();
        return options[random.nextInt(options.length)];
    }

}
