package br.edu.uaifood.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class CustomerNotFoundException(reason: String) : ResponseStatusException(HttpStatus.NOT_FOUND, reason)