plugins {
	id("fabric-loom") version("0.11-SNAPSHOT")
}

val javaVersion: String by project

val minecraftDependency: String by project
val minecraftVersion: String by project
val loaderVersion: String by project
val yarnMappings: String by project

val fabricVersion: String by project
val voicechatDependency: String by project
val voicechatApiVersion: String by project
val voicechatVersion: String by project
val phonosDependency: String by project
val phonosVersion: String by project
val trinketsDependency: String by project
val trinketsVersion: String by project

val modVersion: String by project
val modId: String by project
val mavenGroup: String by project

base.archivesBaseName = modId
version = modVersion
group = mavenGroup

repositories {
	mavenCentral()
	maven {
		name = "henkelmax.public"
		url = uri("https://maven.maxhenkel.de/repository/public")
		content {
			includeGroup("de.maxhenkel.voicechat")
		}
	}
	maven {
		name = "Modrinth"
		url = uri("https://api.modrinth.com/maven")
		content {
			includeGroup("maven.modrinth")
		}
	}
	maven {
		name = "TerraformersMC"
		url = uri("https://maven.terraformersmc.com/")
	}
	maven {
		name = "Ladysnake Libs"
		url = uri("https://maven.ladysnake.org/releases")
	}
	mavenLocal()
}

dependencies {
	minecraft("com.mojang:minecraft:$minecraftVersion")
	mappings("net.fabricmc:yarn:$yarnMappings:v2")
	modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
	
	modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
	
	// API/Lib
	modImplementation("io.github.foundationgames:Phonos:$phonosVersion") {
		exclude("com.terraformersmc", "modmenu")
	} // uses maven local for now
	modImplementation("dev.emi:trinkets:$trinketsVersion")
	implementation("de.maxhenkel.voicechat:voicechat-api:$voicechatApiVersion")
	
	implementation("com.google.code.findbugs:jsr305:3.0.2")
	
	modRuntimeOnly("maven.modrinth:simple-voice-chat:fabric-$voicechatVersion")
}

tasks.processResources {
	inputs.property("version", version)
	
	filesMatching("fabric.mod.json") {
		expand("version" to modVersion,
			"javaVersion" to javaVersion.toInt(),
			"minecraftDependency" to minecraftDependency,
			"loaderVersion" to loaderVersion,
			"fabricVersion" to fabricVersion,
			"voicechatDependency" to voicechatDependency,
			"phonosDependency" to phonosDependency,
			"trinketsDependency" to trinketsDependency,
		)
	}
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
	options.release.set(javaVersion.toInt())
}

java {
	sourceCompatibility = JavaVersion.valueOf("VERSION_$javaVersion")
	targetCompatibility = JavaVersion.valueOf("VERSION_$javaVersion")
	
	withSourcesJar()
}

tasks.withType<AbstractArchiveTask> {
	from("LICENSE") {
		rename { "${it}_$modId" }
	}
}
