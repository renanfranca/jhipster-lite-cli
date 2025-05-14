package tech.jhipster.lite.cli.command.infrastructure.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.lite.TestProjects.newTestFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import picocli.CommandLine;
import tech.jhipster.lite.cli.IntegrationTest;
import tech.jhipster.lite.module.application.JHipsterModulesApplicationService;
import tech.jhipster.lite.module.infrastructure.secondary.git.GitTestUtil;
import tech.jhipster.lite.project.application.ProjectsApplicationService;
import tech.jhipster.lite.project.domain.ProjectPath;
import tech.jhipster.lite.project.domain.history.ProjectHistory;

@ExtendWith(OutputCaptureExtension.class)
@IntegrationTest
class JHLiteCommandTest {

  private static final String PACKAGE_NAME = "packageName";
  private static final String PROJECT_NAME = "projectName";
  private static final String BASE_NAME = "baseName";
  private static final String INDENT_SIZE = "indentSize";

  @Autowired
  private ProjectsApplicationService projects;

  @Autowired
  private JHipsterModulesApplicationService modules;

  @Test
  void shouldShowHelpMessageWhenNoCommand(CapturedOutput output) {
    String[] args = {};
    CommandLine cmd = commandLine();

    int exitCode = cmd.execute(args);

    assertThat(exitCode).isEqualTo(2);
    assertThat(output.toString()).contains(
      """
      JHipster Lite CLI
        -h, --help      Show this help message and exit.
        -V, --version   Print version information and exit.

      Commands:
      """
    );
  }

  @Nested
  @DisplayName("list")
  class ListModules {

    @Test
    void shouldListModules(CapturedOutput output) {
      String[] args = { "list" };
      CommandLine cmd = commandLine();

      int exitCode = cmd.execute(args);

      assertThat(exitCode).isZero();
      assertThat(output.toString()).contains("Available jhipster-lite modules");
      assertThat(output.toString()).contains("init").contains("Init project");
      assertThat(output.toString()).contains("prettier").contains("Format project with prettier");
    }
  }

  @Nested
  @DisplayName("apply")
  class ApplyModule {

    @Test
    void shouldNotApplyWithoutModuleSlug(CapturedOutput output) throws IOException {
      Path projectPath = setupProjectTestFolder();
      String[] args = { "apply", "--project-path", projectPath.toString() };
      CommandLine cmd = commandLine();

      int exitCode = cmd.execute(args);

      assertThat(exitCode).isEqualTo(2);
      assertThat(output.toString()).contains("Missing required parameter: 'MODULE_SLUG'");
    }

    @Test
    void shouldApplyInitModuleWithDefaultOptions() throws IOException {
      Path projectPath = setupProjectTestFolder();
      String[] args = { "apply", "init", "--project-path", projectPath.toString() };
      CommandLine cmd = commandLine();

      int exitCode = cmd.execute(args);

      assertThat(exitCode).isZero();
      assertThat(GitTestUtil.getCommits(projectPath)).contains("Apply module: init");
      assertThat(projectPropertyValue(projectPath, PACKAGE_NAME)).isEqualTo("com.mycompany.myapp");
      assertThat(projectPropertyValue(projectPath, PROJECT_NAME)).isEqualTo("JHipster Sample Application");
      assertThat(projectPropertyValue(projectPath, BASE_NAME)).isEqualTo("jhipsterSampleApplication");
      assertThat(projectPropertyValue(projectPath, INDENT_SIZE)).isEqualTo(2);
    }

    private Object projectPropertyValue(Path projectPath, String propertyKey) {
      ProjectHistory history = projects.getHistory(new ProjectPath(projectPath.toString()));
      return history.latestProperties().parameters().getOrDefault(propertyKey, null);
    }

    @Test
    void shouldApplyInitModuleWithCommit() throws IOException {
      Path projectPath = setupProjectTestFolder();
      String[] args = { "apply", "init", "--project-path", projectPath.toString(), "--commit" };
      CommandLine cmd = commandLine();

      int exitCode = cmd.execute(args);

      assertThat(exitCode).isZero();
      assertThat(GitTestUtil.getCommits(projectPath)).contains("Apply module: init");
    }

    @Test
    void shouldApplyInitModuleWithoutCommit() throws IOException {
      Path projectPath = setupProjectTestFolder();
      String[] args = { "apply", "init", "--project-path", projectPath.toString(), "--no-commit" };
      CommandLine cmd = commandLine();

      int exitCode = cmd.execute(args);

      assertThat(exitCode).isZero();
      assertThat(GitTestUtil.getCommits(projectPath)).isEmpty();
    }

    @Test
    void shouldApplyInitModuleWithPackageName() throws IOException {
      Path projectPath = setupProjectTestFolder();
      String[] args = { "apply", "init", "--project-path", projectPath.toString(), "--package-name", "com.newcompany.newapp" };
      CommandLine cmd = commandLine();

      int exitCode = cmd.execute(args);

      assertThat(exitCode).isZero();
      assertThat(projectPropertyValue(projectPath, PACKAGE_NAME)).isEqualTo("com.newcompany.newapp");
    }

    @Test
    void shouldApplyInitModuleWithProjectName() throws IOException {
      Path projectPath = setupProjectTestFolder();
      String[] args = { "apply", "init", "--project-path", projectPath.toString(), "--project-name", "My New App" };
      CommandLine cmd = commandLine();

      int exitCode = cmd.execute(args);

      assertThat(exitCode).isZero();
      assertThat(projectPropertyValue(projectPath, PROJECT_NAME)).isEqualTo("My New App");
    }

    @Test
    void shouldApplyInitModuleWithBaseName() throws IOException {
      Path projectPath = setupProjectTestFolder();
      String[] args = { "apply", "init", "--project-path", projectPath.toString(), "--base-name", "myNewApp" };
      CommandLine cmd = commandLine();

      int exitCode = cmd.execute(args);

      assertThat(exitCode).isZero();
      assertThat(projectPropertyValue(projectPath, BASE_NAME)).isEqualTo("myNewApp");
    }

    @Test
    void shouldNotApplyModuleWithInvalidBaseName() throws IOException {
      Path projectPath = setupProjectTestFolder();
      String[] args = { "apply", "init", "--project-path", projectPath.toString(), "--base-name", "my.New@pp" };
      CommandLine cmd = commandLine();

      int exitCode = cmd.execute(args);

      assertThat(exitCode).isEqualTo(1);
    }

    @Test
    void shouldApplyInitModuleWithIndentation() throws IOException {
      Path projectPath = setupProjectTestFolder();
      String[] args = { "apply", "init", "--project-path", projectPath.toString(), "--indentation", "4" };
      CommandLine cmd = commandLine();

      int exitCode = cmd.execute(args);

      assertThat(exitCode).isZero();
      assertThat(projectPropertyValue(projectPath, INDENT_SIZE)).isEqualTo(4);
    }

    private static Path setupProjectTestFolder() throws IOException {
      String projectFolder = newTestFolder();
      Path projectPath = Path.of(projectFolder);
      Files.createDirectories(projectPath);
      loadGitConfig(projectPath);

      return projectPath;
    }

    private static void loadGitConfig(Path project) {
      GitTestUtil.execute(project, "init");
      GitTestUtil.execute(project, "config", "init.defaultBranch", "main");
      GitTestUtil.execute(project, "config", "user.email", "\"test@jhipster.com\"");
      GitTestUtil.execute(project, "config", "user.name", "\"Test\"");
    }
  }

  private CommandLine commandLine() {
    ListModulesCommand listModulesCommand = new ListModulesCommand(modules);
    ApplyModuleCommand applyModuleCommand = new ApplyModuleCommand(modules);

    JHLiteCommand jhliteCommand = new JHLiteCommand(listModulesCommand, applyModuleCommand);

    return new CommandLine(jhliteCommand.buildCommandSpec());
  }
}
