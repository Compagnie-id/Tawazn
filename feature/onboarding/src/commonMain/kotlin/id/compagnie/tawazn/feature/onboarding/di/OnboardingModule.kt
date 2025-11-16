package id.compagnie.tawazn.feature.onboarding.di

import id.compagnie.tawazn.feature.onboarding.OnboardingScreenModel
import org.koin.dsl.module

val onboardingModule = module {
    factory { OnboardingScreenModel() }
}
