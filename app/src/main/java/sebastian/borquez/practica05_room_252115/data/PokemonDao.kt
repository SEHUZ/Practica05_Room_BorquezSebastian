package sebastian.borquez.practica05_room_252115.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon_table ORDER BY number ASC")
    fun getAll(): Flow<List<PokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(pokemon: PokemonEntity)

    @Delete
    suspend fun delete(pokemon: PokemonEntity)

    @Update
    suspend fun update(pokemon: PokemonEntity)


    @Query("""
        SELECT * FROM pokemon_table 
        WHERE (name LIKE '%' || :searchQuery || '%' OR type LIKE '%' || :searchQuery || '%')
        AND (:typeFilter = '' OR type LIKE '%' || :typeFilter || '%')
        AND level >= :minLevel
        ORDER BY number ASC
    """)
    fun getFilteredPokemons(
        searchQuery: String,
        typeFilter: String,
        minLevel: Int
    ): Flow<List<PokemonEntity>>
}