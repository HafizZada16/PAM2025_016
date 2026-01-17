package com.example.cinetrack_ucp.data.local

import androidx.room.*
import com.example.cinetrack_ucp.data.BookingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("SELECT * FROM bookings ORDER BY id DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Delete
    suspend fun deleteBooking(booking: BookingEntity)
}