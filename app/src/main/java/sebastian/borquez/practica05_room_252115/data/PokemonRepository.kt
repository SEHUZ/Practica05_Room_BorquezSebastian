package sebastian.borquez.practica05_room_252115.data

import kotlinx.coroutines.flow.Flow

class PokemonRepository(private val pokemonDao: PokemonDao) {
    val allPokemons = pokemonDao.getAll()

    suspend fun add(pokemon: PokemonEntity) {
        pokemonDao.add(pokemon)
    }

    suspend fun delete(pokemon: PokemonEntity) {
        pokemonDao.delete(pokemon)
    }

    suspend fun update(pokemon: PokemonEntity) {
        pokemonDao.update(pokemon)
    }

    fun getFilteredPokemons(searchQuery: String, typeFilter: String, minLevel: Int): Flow<List<PokemonEntity>> {
        return pokemonDao.getFilteredPokemons(searchQuery, typeFilter, minLevel)
    }
}
