package id.compagnie.tawazn

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform