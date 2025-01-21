package br.edu.uaifood

import br.edu.uaifood.util.ScopeUtils
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.AbstractEnvironment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@OpenAPIDefinition(
	info = Info(
		title = "UaiFood API",
		version = "1.0.0",
		description = "API documentation for UaiFood project"
	)
)

class UaiFoodApplication

fun main(args: Array<String>) {
	val profile = ScopeUtils.getProfileFromScope()
	System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile)
	runApplication<UaiFoodApplication>(*args)
}
