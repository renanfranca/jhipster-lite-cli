package tech.jhipster.lite.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.lite.TestProjects.newTestFolder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tech.jhipster.lite.cli.shared.exit.infrastructure.primary.MockExitConfiguration;

@DisplayNameGeneration(ReplaceCamelCase.class)
@SpringBootTest(classes = { JHLiteCliApp.class, MockExitConfiguration.class })
class JHLiteCliAppTest {

  private static final String LOADING_COMPLETE_MESSAGE = "JHipster Lite CLI is ready";
  private static final String COMMAND_SUCCESS_MESSAGE = "Command executed";
  private static final String COMMAND_FAILURE_MESSAGE = "Command failed";
  private static final String VERSION_INFO_PREFIX = "JHipster Lite CLI v";
  private static final String AVAILABLE_JHIPSTER_LITE_MODULES = "Available jhipster-lite modules";
  private static final String MISSING_REQUIRED_OPTIONS = "Missing required options";

  @Nested
  @DisplayName("spinner progress messages")
  class SpinnerProgressMessages {

    @Test
    void shouldShowMessagesInCorrectOrderWhenRunningVersionCommand() {
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));
      System.setErr(new PrintStream(outContent));

      JHLiteCliApp.main(new String[] { "--version" });

      String output = outContent.toString();
      assertThat(output).contains(LOADING_COMPLETE_MESSAGE);
      assertThat(output).contains(COMMAND_SUCCESS_MESSAGE);
      int loadingCompletePosition = output.indexOf(LOADING_COMPLETE_MESSAGE);
      int commandSuccessPosition = output.indexOf(COMMAND_SUCCESS_MESSAGE);
      int versionInfoPosition = output.indexOf(VERSION_INFO_PREFIX);
      assertThat(loadingCompletePosition).isPositive();
      assertThat(commandSuccessPosition).isPositive();
      assertThat(versionInfoPosition).isPositive();
      assertThat(loadingCompletePosition)
        .withFailMessage("'%s' message should appear before '%s'".formatted(LOADING_COMPLETE_MESSAGE, COMMAND_SUCCESS_MESSAGE))
        .isLessThan(commandSuccessPosition);
      assertThat(commandSuccessPosition)
        .withFailMessage("'%s' message should appear before '%s'".formatted(COMMAND_SUCCESS_MESSAGE, VERSION_INFO_PREFIX))
        .isLessThan(versionInfoPosition);

      System.setOut(System.out);
      System.setErr(System.err);
    }

    @Test
    void shouldShowMessagesInCorrectOrderWhenRunningListCommand() {
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));
      System.setErr(new PrintStream(outContent));

      JHLiteCliApp.main(new String[] { "list" });

      String output = outContent.toString();
      assertThat(output).contains(LOADING_COMPLETE_MESSAGE);
      assertThat(output).contains(COMMAND_SUCCESS_MESSAGE);
      int loadingCompletePosition = output.indexOf(LOADING_COMPLETE_MESSAGE);
      int commandSuccessPosition = output.indexOf(COMMAND_SUCCESS_MESSAGE);
      int availableJHipsterLiteModulesPosition = output.indexOf(AVAILABLE_JHIPSTER_LITE_MODULES);
      assertThat(loadingCompletePosition)
        .withFailMessage("'%s' message should appear before '%s'".formatted(LOADING_COMPLETE_MESSAGE, COMMAND_SUCCESS_MESSAGE))
        .isLessThan(commandSuccessPosition);
      assertThat(commandSuccessPosition)
        .withFailMessage("'%s' message should appear before '%s'".formatted(COMMAND_SUCCESS_MESSAGE, AVAILABLE_JHIPSTER_LITE_MODULES))
        .isLessThan(availableJHipsterLiteModulesPosition);
    }

    @Test
    void shouldShowMessagesInCorrectOrderWhenRunningApplyInitCommandWithoutParameters() {
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));
      System.setErr(new PrintStream(outContent));

      JHLiteCliApp.main(new String[] { "apply", "init", "--project-path", newTestFolder() });

      String output = outContent.toString();
      assertThat(output).contains(LOADING_COMPLETE_MESSAGE);
      assertThat(output).contains(COMMAND_FAILURE_MESSAGE);
      int loadingCompletePosition = output.indexOf(LOADING_COMPLETE_MESSAGE);
      int commandFailurePosition = output.indexOf(COMMAND_FAILURE_MESSAGE);
      int missingRequiredOptionsPosition = output.indexOf(MISSING_REQUIRED_OPTIONS);
      assertThat(loadingCompletePosition)
        .withFailMessage("'%s' message should appear before '%s'".formatted(LOADING_COMPLETE_MESSAGE, COMMAND_FAILURE_MESSAGE))
        .isLessThan(commandFailurePosition);
      assertThat(commandFailurePosition)
        .withFailMessage("'%s' message should appear before '%s'".formatted(COMMAND_FAILURE_MESSAGE, MISSING_REQUIRED_OPTIONS))
        .isLessThan(missingRequiredOptionsPosition);

      System.setOut(System.out);
      System.setErr(System.err);
    }

    @Test
    void shouldShowMessagesInCorrectOrderWhenRunningApplyInitCommandWithRequiredParameters() {
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));
      System.setErr(new PrintStream(outContent));

      JHLiteCliApp.main(
        new String[] {
          "apply",
          "init",
          "--project-path",
          newTestFolder(),
          "--base-name",
          "jhipsterSampleApplication",
          "--project-name",
          "JHipster Sample Application",
        }
      );

      String output = outContent.toString();
      assertThat(output).contains(LOADING_COMPLETE_MESSAGE);
      assertThat(output).contains(COMMAND_SUCCESS_MESSAGE);
      int loadingCompletePosition = output.indexOf(LOADING_COMPLETE_MESSAGE);
      int commandSuccessPosition = output.indexOf(COMMAND_SUCCESS_MESSAGE);
      assertThat(loadingCompletePosition)
        .withFailMessage("'%s' message should appear before '%s'".formatted(LOADING_COMPLETE_MESSAGE, COMMAND_SUCCESS_MESSAGE))
        .isLessThan(commandSuccessPosition);
      assertThat(output.trim()).endsWith(COMMAND_SUCCESS_MESSAGE);

      System.setOut(System.out);
      System.setErr(System.err);
    }
  }
}
