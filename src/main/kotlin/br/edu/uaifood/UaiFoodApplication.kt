package br.edu.uaifood

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UaiFoodApplication

fun main(args: Array<String>) {
	runApplication<UaiFoodApplication>(*args)
}
