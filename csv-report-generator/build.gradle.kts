plugins {
    kotlin("jvm") version "1.8.0"
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.5"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf") // Для HTML-шаблонов
    implementation("com.opencsv:opencsv:5.8") // Для работы с CSV
    implementation("org.apache.poi:poi-ooxml:5.2.3") // Для работы с Excel
    implementation("com.itextpdf:itext7-core:7.2.5") // Для работы с PDF
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("com.itextpdf:itextpdf:5.5.13.3")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // Для работы с транзакциями
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("com.microsoft.sqlserver:mssql-jdbc:12.4.2.jre11")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
