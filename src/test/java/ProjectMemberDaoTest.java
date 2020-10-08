import jdk.jfr.StackTrace;
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

        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("create table project_members(id int auto_increment,  name varchar(60) not null, role varchar(60))").executeUpdate();
        }
        //id bigint auto_increment,
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
        assertThat(projectMemberDao.listAll().contains(projectMemberName));
    }



    private String samplePersonName() {
        String[] options = {"John Green", "Willy Smith", "Johnny Greenwood", "Jonas Peteson"};
        Random random = new Random();
        return options[random.nextInt(options.length)];
    }

}
