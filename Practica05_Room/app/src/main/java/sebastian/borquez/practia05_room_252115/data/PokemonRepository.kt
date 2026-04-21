package sebastian.borquez.practia05_room_252115.data

class PokemonRepository(private val pokemonDao: PokemonDao) {
    val allPokemons = pokemonDao.getAll()

    suspend fun add(pokemon: PokemonEntity) {
        pokemonDao.add(pokemon)
    }
}
