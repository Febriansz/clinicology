# Clinicology - For Better Treatment

### Project Spesification
    IDE: Apache NetBeans 12.2
    Programming Language: Java
    Database: MySQL 8.0
    
### Project Dependencies (Maven)
* [JPA](https://mvnrepository.com/artifact/javax.persistence/javax.persistence-api) - Javax Persistence API for avoid writing DML in the database specific dialect of SQL.
* [MySQL JDBC Driver](https://mvnrepository.com/artifact/mysql/mysql-connector-java) - MySQL JDBC Driver for Connecting to Database
    
## Project Structure Directory
* **id.febriansz.clinicology** - Our Root Package.
* **id.febriansz.clinicology.ui** - Where we store our Frame.
* **id.febriansz.clinicology.ui.internal** - Where we store our Internal Frame.
* **id.febriansz.clinicology.data.entity** - Where we store our Internal Frame.
* **id.febriansz.clinicology.report** - Where we store our report (we use JInternalFrame and buffer it to an image because we use NetBeans 12.2, this version remove Swing Layout Extension by default, and it's too complicated for us to configure it)
* **id.febriansz.clinicology.utils** - Where we store our application utilities.
    
### User Access Default
    Username: admin
    Password: 12345
