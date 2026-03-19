package com.example.belsidriver.di

import com.example.belsidriver.data.mock.MockAuthRepository
import com.example.belsidriver.data.mock.MockDeliveryPointRepository
import com.example.belsidriver.data.mock.MockDriverRepository
import com.example.belsidriver.data.mock.MockRouteRepository
import com.example.belsidriver.data.mock.MockShiftRepository
import com.example.belsidriver.domain.repository.AuthRepository
import com.example.belsidriver.domain.repository.DeliveryPointRepository
import com.example.belsidriver.domain.repository.DriverRepository
import com.example.belsidriver.domain.repository.RouteRepository
import com.example.belsidriver.domain.repository.ShiftRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DI Module: Mock-реализации (без бэкенда).
 * Для переключения на реальный сервер — заменить Mock* на *Impl.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: MockAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindShiftRepository(impl: MockShiftRepository): ShiftRepository

    @Binds
    @Singleton
    abstract fun bindRouteRepository(impl: MockRouteRepository): RouteRepository

    @Binds
    @Singleton
    abstract fun bindDeliveryPointRepository(impl: MockDeliveryPointRepository): DeliveryPointRepository

    @Binds
    @Singleton
    abstract fun bindDriverRepository(impl: MockDriverRepository): DriverRepository
}
