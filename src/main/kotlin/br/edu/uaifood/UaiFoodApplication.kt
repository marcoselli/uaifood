package br.edu.uaifood

import br.edu.uaifood.util.ScopeUtils
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.AbstractEnvironment

@SpringBootApplication
class UaiFoodApplication

fun main(args: Array<String>) {
	val profile = ScopeUtils.getProfileFromScope()
	System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile)
	runApplication<UaiFoodApplication>(*args)
}
