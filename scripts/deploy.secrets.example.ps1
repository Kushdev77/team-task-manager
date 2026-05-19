# Copy this file to deploy.secrets.ps1 and fill in your db4free values (file is gitignored)

$DB_NAME = "your_database_name"
$DB_USER = "your_db4free_username"
$DB_PASS = "your_db4free_password"

$SPRING_DATASOURCE_URL = "jdbc:mysql://db4free.net:3306/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$SPRING_DATASOURCE_USERNAME = $DB_USER
$SPRING_DATASOURCE_PASSWORD = $DB_PASS
$JWT_SECRET = "change-this-to-a-long-random-string-at-least-40-characters"
