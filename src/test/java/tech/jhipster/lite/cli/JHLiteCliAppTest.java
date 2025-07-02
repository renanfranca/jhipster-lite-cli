package tech.jhipster.lite.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.lite.TestProjects.newTestFolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@IntegrationTest
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
    void shouldShowMessagesInCorrectOrderWhenRunningVersionCommand() throws Exception {
      ProcessBuilder processBuilder = createCommandProcess("--version");
      processBuilder.redirectErrorStream(true);

      Process process = processBuilder.start();
      boolean completed = process.waitFor(30, TimeUnit.SECONDS);
      String output = captureProcessOutput(process);

      assertThat(completed).isTrue();
      System.out.println(output);
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
    }

    @Test
    void shouldShowMessagesInCorrectOrderWhenRunningListCommand() throws Exception {
      ProcessBuilder processBuilder = createCommandProcess("list");
      processBuilder.redirectErrorStream(true);

      Process process = processBuilder.start();
      boolean completed = process.waitFor(30, TimeUnit.SECONDS);
      String output = captureProcessOutput(process);

      assertThat(completed).isTrue();
      System.out.println(output);
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
    void shouldShowMessagesInCorrectOrderWhenRunningApplyInitCommandWithoutParameters() throws Exception {
      ProcessBuilder processBuilder = createCommandProcess("apply", "init", "--project-path", newTestFolder());
      processBuilder.redirectErrorStream(true);

      Process process = processBuilder.start();
      boolean completed = process.waitFor(30, TimeUnit.SECONDS);
      String output = captureProcessOutput(process);

      assertThat(completed).isTrue();
      System.out.println(output);
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
    }

    @Test
    void shouldShowMessagesInCorrectOrderWhenRunningApplyInitCommandWithRequiredParameters() throws Exception {
      ProcessBuilder processBuilder = createCommandProcess(
        "apply",
        "init",
        "--project-path",
        newTestFolder(),
        "--base-name",
        "jhipsterSampleApplication",
        "--project-name",
        "JHipster Sample Application"
      );
      processBuilder.redirectErrorStream(true);

      Process process = processBuilder.start();
      boolean completed = process.waitFor(30, TimeUnit.SECONDS);
      String output = captureProcessOutput(process);

      assertThat(completed).isTrue();
      System.out.println(output);
      assertThat(output).contains(LOADING_COMPLETE_MESSAGE);
      assertThat(output).contains(COMMAND_SUCCESS_MESSAGE);
      int loadingCompletePosition = output.indexOf(LOADING_COMPLETE_MESSAGE);
      int commandSuccessPosition = output.indexOf(COMMAND_SUCCESS_MESSAGE);
      assertThat(loadingCompletePosition)
        .withFailMessage("'%s' message should appear before '%s'".formatted(LOADING_COMPLETE_MESSAGE, COMMAND_SUCCESS_MESSAGE))
        .isLessThan(commandSuccessPosition);
      assertThat(output.trim()).endsWith(COMMAND_SUCCESS_MESSAGE);
    }
  }

  private ProcessBuilder createCommandProcess(String... command) {
    String javaPath = System.getProperty("java.home") + "/bin/java";
    String classpath = System.getProperty("java.class.path");

    List<String> processCommand = new ArrayList<>();
    processCommand.add(javaPath);
    processCommand.add("-cp");
    processCommand.add(classpath);
    processCommand.add(JHLiteCliApp.class.getName());
    processCommand.addAll(Arrays.asList(command));

    return new ProcessBuilder(processCommand);
  }

  private String captureProcessOutput(Process process) throws Exception {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
  }
}
