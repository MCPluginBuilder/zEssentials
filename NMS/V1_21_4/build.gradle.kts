plugins {
    id("io.papermc.paperweight.userdev")
}

group "NMS:V1_21_4"

dependencies {
    compileOnly(project(":API"))
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

