package tech.jhipster.lite.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.lite.TestProjects.newTestFolder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

@IntegrationTest
class JHLiteCliAppApplyInitTest {

  private static final String LOADING_COMPLETE_MESSAGE = "JHipster Lite CLI is ready";
  private static final String COMMAND_SUCCESS_MESSAGE = "Command executed";
  private static final String COMMAND_FAILURE_MESSAGE = "Command failed";
  private static final String MISSING_REQUIRED_OPTIONS = "Missing required options";

  @Test
  void shouldShowMessagesInCorrectOrderWhenRunningWithoutParameters() {
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
  void shouldShowMessagesInCorrectOrderWhenRunningWithRequiredParameters() {
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
