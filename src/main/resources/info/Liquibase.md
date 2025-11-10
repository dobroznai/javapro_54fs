changeSet - маленькая миграция
changeLog - набор миграций

mvn liquibase:history
mvn liquibase:status
mvn liquibase:validate

mvn liquibase:update
mvn liquibase:rollback -Dliquibase.rollbackCount=1