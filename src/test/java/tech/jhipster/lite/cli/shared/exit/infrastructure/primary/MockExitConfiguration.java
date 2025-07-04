package tech.jhipster.lite.cli.shared.exit.infrastructure.primary;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tech.jhipster.lite.cli.shared.exit.domain.SpringApplicationExit;
import tech.jhipster.lite.cli.shared.exit.domain.SystemExit;

//TODO Understand why does not working with @TestConfiguration
@Configuration
public class MockExitConfiguration {

  @Bean
  @Primary
  SystemExit SystemExit() {
    return mock(SystemExit.class);
  }

  @Bean
  @Primary
  SpringApplicationExit SpringApplicationExit() {
    return mock(SpringApplicationExit.class);
  }
}
