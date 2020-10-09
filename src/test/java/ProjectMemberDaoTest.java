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
    private SampleData sd = new SampleData();

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

        String projectMemberName = sd.samplePersonName();
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

        String projectMemberName = sd.samplePersonName();
        String projectMemberRole = sd.sampleRole();

        projectMemberDao.insert(projectMemberName, projectMemberRole);
        assertThat(projectMemberDao.listNames()).contains(projectMemberName);
        assertThat(projectMemberDao.listRoles()).contains(projectMemberRole);

        projectMemberDao.printAll();
    }

    @Test
    void shouldRetrieveSingleMember() throws SQLException {
        JdbcDataSource ds = createTestDataSource();
        projectMemberDao = new ProjectMemberDao(ds);

        ProjectMember projectMember = sd.sampleProjectMember();
        projectMemberDao.insert(projectMember);

        projectMemberDao.printAll();

        assertThat(projectMemberDao.retrieve(projectMember.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(projectMember);
    }
}
