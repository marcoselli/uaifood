package br.edu.uaifood.util

class ScopeUtils {

    companion object {
        const val ENV_SCOPE = "SCOPE"
        const val PROD_SCOPE = "production"

        fun getProfileFromScope(): String {
            val scope = System.getenv(ENV_SCOPE)
            return when(scope) {
                PROD_SCOPE -> "prod"
                else -> " local"
            }
        }
    }
}