package tech.jhipster.lite.cli.command.infrastructure.primary;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import lukfor.progress.Components;
import lukfor.progress.TaskService;
import lukfor.progress.tasks.ITaskRunnable;
import lukfor.progress.tasks.monitors.ITaskMonitor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import tech.jhipster.lite.cli.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;

@Component
class JHLiteCommandRunner implements CommandLineRunner, ExitCodeGenerator {

  private final JHLiteCommandsFactory command;
  private int exitCode;

  public JHLiteCommandRunner(JHLiteCommandsFactory command) {
    this.command = command;
  }

  @Override
  @ExcludeFromGeneratedCodeCoverage(reason = "Do not need to test when using picocli framework")
  public void run(String... args) {
    TaskService.monitor(Components.SPINNER, Components.TASK_NAME).run(executeCommandTask(args));
  }

  private ITaskRunnable executeCommandTask(String... args) {
    return monitor -> {
      monitor.begin("Running command");

      exitCode = captureOutputAndExecute(monitor, args);
    };
  }

  private int captureOutputAndExecute(ITaskMonitor monitor, String... args) {
    PrintStream originalOut = System.out;
    PrintStream originalErr = System.err;
    ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
    ByteArrayOutputStream errCapture = new ByteArrayOutputStream();

    try {
      System.setOut(new PrintStream(outputCapture));
      System.setErr(new PrintStream(errCapture));

      return executeCommand(args);
    } finally {
      monitor.update("Command executed");
      monitor.done();

      System.setOut(originalOut);
      System.setErr(originalErr);
      System.out.print(outputCapture);
      System.err.print(errCapture);
    }
  }

  private int executeCommand(String... args) {
    return new CommandLine(command.buildCommandSpec()).execute(args);
  }

  @Override
  @ExcludeFromGeneratedCodeCoverage(reason = "Don not need to test when using picocli framework")
  public int getExitCode() {
    return exitCode;
  }
}
