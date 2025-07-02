package tech.jhipster.lite.cli.shared.spinnerprogress.infrastructure.primary;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import tech.jhipster.lite.cli.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;
import tech.jhipster.lite.cli.shared.spinnerprogress.domain.SpinnerProgress;

class ConsoleSpinnerProgress implements SpinnerProgress {

  private static final String[] SPINNER_FRAMES = { "⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏" };
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final String ANSI_RED = "\u001B[31m";
  private static final String ANSI_CYAN = "\u001B[36m";
  private static final String CLEAR_LINE = "\r\033[K";

  private ScheduledExecutorService executor;
  private final AtomicBoolean running = new AtomicBoolean(false);
  private String currentMessage = "";
  private int frameIndex = 0;

  @Override
  public void show() {
    show("Processing");
  }

  @Override
  public void show(String message) {
    if (running.compareAndSet(false, true)) {
      currentMessage = message;

      renderFrameSync();

      executor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "spinner-animation");
        thread.setDaemon(true);
        return thread;
      });
      executor.scheduleAtFixedRate(this::renderFrame, 0, 80, TimeUnit.MILLISECONDS);
    } else {
      update(message);
    }
  }

  @Override
  public void update(String message) {
    currentMessage = message;

    renderFrameSync();
  }

  @Override
  public void hide() {
    stopSpinner();
  }

  private boolean stopSpinner() {
    if (running.compareAndSet(true, false)) {
      executor.shutdown();
      System.out.print(CLEAR_LINE);
      return true;
    }
    return false;
  }

  @Override
  public void success(String message) {
    displayResult(ANSI_GREEN, "✓", message);
  }

  private void displayResult(String color, String symbol, String message) {
    if (stopSpinner()) {
      System.out.println(color + symbol + ANSI_RESET + " " + message);
    }
  }

  @Override
  public void failure(String message) {
    displayResult(ANSI_RED, "✗", message);
  }

  @ExcludeFromGeneratedCodeCoverage(reason = "Rendering logic is difficult to test")
  private void renderFrameSync() {
    renderSpinner(false);
  }

  @ExcludeFromGeneratedCodeCoverage(reason = "Rendering logic is difficult to test")
  private void renderSpinner(boolean updateFrame) {
    if (running.get()) {
      if (updateFrame) {
        frameIndex = (frameIndex + 1) % SPINNER_FRAMES.length;
      }
      String frame = SPINNER_FRAMES[frameIndex];
      System.out.print(CLEAR_LINE + ANSI_CYAN + frame + ANSI_RESET + " " + currentMessage);
    }
  }

  @ExcludeFromGeneratedCodeCoverage(reason = "Rendering logic is difficult to test")
  private void renderFrame() {
    renderSpinner(true);
  }
}
