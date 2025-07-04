package tech.jhipster.lite.cli;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@DisplayNameGeneration(ReplaceCamelCase.class)
@SpringBootTest(classes = { JHLiteCliApp.class }, useMainMethod = SpringBootTest.UseMainMethod.ALWAYS, args = { "--version" })
@ExtendWith(OutputCaptureExtension.class)
class JHLiteCliAppVersionTest {

  private static final String LOADING_COMPLETE_MESSAGE = "JHipster Lite CLI is ready";
  private static final String COMMAND_SUCCESS_MESSAGE = "Command executed";
  private static final String VERSION_INFO_PREFIX = "JHipster Lite CLI v";

  @Test
  void shouldShowMessagesInCorrectOrder(CapturedOutput output) {
    assertThat(output).contains(LOADING_COMPLETE_MESSAGE);
    assertThat(output).contains(COMMAND_SUCCESS_MESSAGE);
    int loadingCompletePosition = output.toString().indexOf(LOADING_COMPLETE_MESSAGE);
    int commandSuccessPosition = output.toString().indexOf(COMMAND_SUCCESS_MESSAGE);
    int versionInfoPosition = output.toString().indexOf(VERSION_INFO_PREFIX);
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
}
