plugins {
	id("buildlogic.java-library-conventions")
	id("buildlogic.swt-library-conventions")
	id("buildlogic.version-conventions")
	id("buildlogic.publishing-conventions")
}

dependencies {
	implementation(libs.jackson)
}