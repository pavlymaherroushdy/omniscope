package com.example.data

import com.example.model.SubTopic

object SpaceTopics {

    // 13 Topics Inside Solar System
    val mercuryTopic = SpaceInsideTopics.mercuryTopic
    val venusTopic = SpaceInsideTopics.venusTopic
    val earthTopic = SpaceInsideTopics.earthTopic
    val marsTopic = SpaceInsideTopics.marsTopic
    val asteroidBeltTopic = SpaceInsideTopics.asteroidBeltTopic
    val jupiterTopic = SpaceInsideTopics.jupiterTopic
    val saturnTopic = SpaceInsideTopics.saturnTopic
    val uranusTopic = SpaceInsideTopics.uranusTopic
    val neptuneTopic = SpaceInsideTopics.neptuneTopic
    val plutoTopic = SpaceInsideTopics.plutoTopic
    val dwarfPlanetsTopic = SpaceInsideTopics.dwarfPlanetsTopic
    val kuiperBeltTopic = SpaceInsideTopics.kuiperBeltTopic
    val oortCloudTopic = SpaceInsideTopics.oortCloudTopic

    // 5 Topics Outside Solar System
    val galaxiesTopic = SpaceOutsideTopics.galaxiesTopic
    val blackHolesTopic = SpaceOutsideTopics.blackHolesTopic
    val starsNebulaeTopic = SpaceOutsideTopics.starsNebulaeTopic
    val exoplanetsTopic = SpaceOutsideTopics.exoplanetsTopic
    val cosmicPhenomenaTopic = SpaceOutsideTopics.cosmicPhenomenaTopic

    // Backward compatibility aliases
    val solarSystemTopic = mercuryTopic
    val starsBlackHolesTopic = blackHolesTopic
    val rocketsTopic = starsNebulaeTopic
    val galaxiesUniverseTopic = galaxiesTopic

    val allSpaceTopics: List<SubTopic> = SpaceInsideTopics.allInsideTopics + SpaceOutsideTopics.allOutsideTopics
}
