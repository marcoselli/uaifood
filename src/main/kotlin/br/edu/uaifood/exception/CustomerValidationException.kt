package br.edu.uaifood.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class CustomerValidationException(reason: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, reason)