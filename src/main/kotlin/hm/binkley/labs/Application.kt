package hm.binkley.labs

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.SpringApplication.run
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

private val logger = getLogger(Application::class.java)!!

@EnableSwagger2
@SpringBootApplication
open class Application
    : ApplicationListener<EmbeddedServletContainerInitializedEvent> {
    override fun onApplicationEvent(
            event: EmbeddedServletContainerInitializedEvent) = logger.info(
            "Ready on port ${event.embeddedServletContainer.port}")

    @Bean
    open fun greetingRepository() = SlowGreetingRepository()

    @Bean
    open fun objectMapper() = jacksonObjectMapper()

    @Bean
    open fun api() = Docket(DocumentationType.SWAGGER_2).
            select().
            apis(RequestHandlerSelectors.any()).
            paths(PathSelectors.any()).
            build()!!
}

fun main(args: Array<String>) {
    run(Application::class.java, *args)
}
