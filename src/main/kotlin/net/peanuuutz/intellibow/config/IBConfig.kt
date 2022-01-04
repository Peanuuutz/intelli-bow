package net.peanuuutz.intellibow.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.fabricmc.loader.api.FabricLoader
import net.peanuuutz.intellibow.IntelliBow
import net.peanuuutz.tomlkt.Comment
import net.peanuuutz.tomlkt.Toml
import java.nio.file.Files

@Serializable
data class IBConfig(
    @SerialName("Recursive Bow")
    val recursiveBowAttribute: IntelliBowAttribute = IntelliBowAttribute(
        baseSpeedModifier = -0.2f,
        baseDamageModifier = 2.0,
        moduleCapacity = 2
    ),
    @SerialName("Composite Bow")
    val compositeBowAttribute: IntelliBowAttribute = IntelliBowAttribute(
        baseSpeedModifier = 0.0f,
        baseDamageModifier = 0.5,
        moduleCapacity = 3
    ),
    @SerialName("Modules")
    val moduleAttributes: ModuleAttributes = ModuleAttributes(
        pullingDeviceSpeedModifier = 0.1f,
        trajectorySimulatorRange = 64
    )
) {
    @Serializable
    data class IntelliBowAttribute(
        @Comment("Literally pulling speed. Float type, bigger than -1.")
        var baseSpeedModifier: Float,
        @Comment("Extra damage (by health point) for each arrow shot by this bow. Double type.")
        var baseDamageModifier: Double,
        @Comment("Literally how many modules can this bow carries. Int type.")
        var moduleCapacity: Int
    ) : ConfigEntryValidator {
        override fun validate() {
            require(baseSpeedModifier > -1.0f)
            require(moduleCapacity >= 0)
        }
    }

    @Serializable
    data class ModuleAttributes(
        @Comment("Literally pulling speed, but for pulling device. Float type.")
        var pullingDeviceSpeedModifier: Float,
        @Comment("(You can't see too far!) Int type.")
        var trajectorySimulatorRange: Int
    ) : ConfigEntryValidator {
        override fun validate() {
            require(pullingDeviceSpeedModifier > 0.0f)
            require(trajectorySimulatorRange > 0)
        }
    }

    interface ConfigEntryValidator {
        fun validate()
    }
}

object IBConfigProvider {
    private val configPath = FabricLoader.getInstance().configDir.resolve("${IntelliBow.MOD_ID}.toml")

    fun load(): IBConfig = if (Files.exists(configPath)) {
        try {
            val content = configPath.toFile().readText()
            Toml.decodeFromString(IBConfig.serializer(), content).apply {
                recursiveBowAttribute.validate()
                compositeBowAttribute.validate()
                moduleAttributes.validate()
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
            save(true)
        }
    } else {
        save(false)
    }

    private fun save(alreadyExists: Boolean): IBConfig {
        val config = IBConfig()
        if (alreadyExists) {
            Files.delete(configPath)
        }
        Files.createFile(configPath)
        val content = Toml.encodeToString(IBConfig.serializer(), config)
        configPath.toFile().writeText(content)
        return config
    }
}