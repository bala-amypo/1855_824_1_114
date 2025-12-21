# ===============================
# Server Configuration
# ===============================
server.port=8080

# ===============================
# MySQL Database Configuration
# ===============================
spring.datasource.url=jdbc:mysql://localhost:3306/digital_queue_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password_here
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ===============================
# JPA / Hibernate Configuration
# ===============================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# ===============================
# JWT Configuration (Optional)
# ===============================
# Token values are hardcoded in JwtTokenProvider as per test constraints

# ===============================
# Swagger / OpenAPI Configuration
# ===============================
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs

# ===============================
# Logging
# ===============================
logging.level.org.springframework.security=INFO
logging.level.org.hibernate.SQL=DEBUG
