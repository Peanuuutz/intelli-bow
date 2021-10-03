package net.peanuuutz.intellibow.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.fabricmc.loader.api.FabricLoader
import net.mamoe.yamlkt.Comment
import net.mamoe.yamlkt.Yaml
import net.peanuuutz.intellibow.IntelliBow
import java.nio.file.Files

@Serializable
data class IBConfig(
    @SerialName("Recursive Bow")
    val recursiveBowAttribute: IntelliBowAttribute = IntelliBowAttribute(
        baseSpeedModifier = -0.1f,
        baseDamageModifier = 1.0,
        moduleCapacity = 2
    ),
    @SerialName("Composite Bow")
    val compositeBowAttribute: IntelliBowAttribute = IntelliBowAttribute(
        baseSpeedModifier = 0.1f,
        baseDamageModifier = 0.0,
        moduleCapacity = 3
    ),
    @SerialName("Modules")
    val moduleAttributes: ModuleAttributes = ModuleAttributes(
        pullingDeviceSpeedModifier = 0.1f
    )
) {
    @Serializable
    data class IntelliBowAttribute(
        @Comment("Literally pulling speed. Float type, bigger than -1.0.")
        var baseSpeedModifier: Float,
        @Comment("Extra damage (by health point) for each arrow shot by this bow. Double type.")
        var baseDamageModifier: Double,
        @Comment("Literally how many modules can this bow carries. Int type.")
        var moduleCapacity: Int
    ) : ConfigEntryValidator {
        override fun validate() {
            require(baseSpeedModifier > -1.0f)
            require(baseDamageModifier > 0.0)
            require(moduleCapacity > 0)
        }
    }

    @Serializable
    data class ModuleAttributes(
        @Comment("Literally pulling speed, but for pulling device. Float type.")
        var pullingDeviceSpeedModifier: Float
    ) : ConfigEntryValidator {
        override fun validate() {
            require(pullingDeviceSpeedModifier > 0)
        }
    }

    interface ConfigEntryValidator {
        fun validate()
    }
}

object IBConfigProvider {
    private val configPath = FabricLoader.getInstance().configDir.resolve("${IntelliBow.MOD_ID}.yaml")

    fun load(): IBConfig = if (Files.exists(configPath)) {
        try {
            val content = Files.newBufferedReader(configPath).use { it.readText() }
            Yaml.decodeFromString(IBConfig.serializer(), content).apply {
                recursiveBowAttribute.validate()
                compositeBowAttribute.validate()
                moduleAttributes.validate()
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
            save()
        }
    } else {
        save()
    }

    private fun save(): IBConfig {
        val config = IBConfig()
        if (Files.exists(configPath)) {
            Files.createFile(configPath)
        }
        val content = Yaml.encodeToString(config)
        configPath.toFile().printWriter().use {
            it.write(content)
        }
        return config
    }
}