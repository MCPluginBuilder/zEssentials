plugins {
    id("io.papermc.paperweight.userdev")
}

group "NMS:V26_2"

dependencies {
    compileOnly(project(":API"))
    compileOnly("net.kyori:adventure-api:4.26.1")
    paperweight.paperDevBundle("26.2.build.+")
}

// Minecraft 26.x is Mojang-mapped only (no Spigot reobfuscation since 26.1), so the default
// production artifact is already Mojang-mapped and no reobfArtifactConfiguration is required.

java {
    // Minecraft 26.x targets Java 25.
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}
