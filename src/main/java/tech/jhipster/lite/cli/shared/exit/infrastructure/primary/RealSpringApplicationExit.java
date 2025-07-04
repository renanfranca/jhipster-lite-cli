package tech.jhipster.lite.cli.shared.exit.infrastructure.primary;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import tech.jhipster.lite.cli.shared.exit.domain.SpringApplicationExit;

@Component
class RealSpringApplicationExit implements SpringApplicationExit {

  @Override
  public int exit(ApplicationContext context) {
    return SpringApplication.exit(context);
  }
}
