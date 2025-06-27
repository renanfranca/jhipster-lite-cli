package tech.jhipster.lite.cli;

import lukfor.progress.Components;
import lukfor.progress.TaskService;
import lukfor.progress.tasks.ITaskRunnable;
import lukfor.progress.tasks.monitors.ITaskMonitor;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import tech.jhipster.lite.JHLiteApp;
import tech.jhipster.lite.cli.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;

@SpringBootApplication(scanBasePackageClasses = { JHLiteApp.class, JHLiteCliApp.class })
@ExcludeFromGeneratedCodeCoverage(reason = "Not testing logs")
public class JHLiteCliApp {

  public static void main(String[] args) {
    TaskService.monitor(Components.SPINNER, Components.TASK_NAME).run(buildStartupTask(args));
  }

  private static ITaskRunnable buildStartupTask(String[] args) {
    return monitor -> {
      monitor.begin("Loading JHipster Lite CLI");

      ConfigurableApplicationContext context = buildApplicationContext(args, monitor);

      int exitCode = SpringApplication.exit(context);
      System.exit(exitCode);
    };
  }

  private static ConfigurableApplicationContext buildApplicationContext(String[] args, ITaskMonitor monitor) {
    return new SpringApplicationBuilder(JHLiteCliApp.class)
      .bannerMode(Banner.Mode.OFF)
      .web(WebApplicationType.NONE)
      .lazyInitialization(true)
      .listeners(event -> handleApplicationEvent(event, monitor))
      .run(args);
  }

  private static void handleApplicationEvent(Object event, ITaskMonitor monitor) {
    if (event instanceof ApplicationStartedEvent) {
      monitor.update("JHipster Lite CLI is ready");
      monitor.done();
    }
  }
}
