package tech.jhipster.lite.cli.command.infrastructure.primary;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import picocli.CommandLine;

@ExtendWith({ SpringExtension.class, OutputCaptureExtension.class })
@ContextConfiguration(classes = CommandContextTestConfig.class)
class JHLiteCommandTest {

  @Autowired
  private JHLiteCommand jhliteCommand;

  @Autowired
  private CommandLine.IFactory factory;

  @Test
  void shouldShowHelpMessageWhenNoCommand(CapturedOutput output) {
    String[] args = {};
    CommandLine cmd = new CommandLine(jhliteCommand, factory);

    int exitCode = cmd.execute(args);

    assertThat(exitCode).isEqualTo(2);
    assertThat(output.toString()).contains(
      """
      JHLite CLI Application
        -h, --help      Show this help message and exit.
        -V, --version   Print version information and exit.

      Commands:
      """
    );
  }

  @Test
  void shouldListModules(CapturedOutput output) {
    String[] args = { "list" };
    CommandLine cmd = new CommandLine(jhliteCommand);

    int exitCode = cmd.execute(args);

    assertThat(exitCode).isZero();
    assertThat(output.toString()).contains("Listing all jhipster-lite modules");
    assertThat(output.toString()).contains("init");
    assertThat(output.toString()).contains("prettier");
  }
}
