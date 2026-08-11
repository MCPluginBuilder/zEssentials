plugins {
    id("io.papermc.paperweight.userdev")
}

group "NMS:V1_20_6"

dependencies {
    compileOnly(project(":API"))
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

