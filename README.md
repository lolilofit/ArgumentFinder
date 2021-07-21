# Argument Finder

## System could load texts and search for user's arguments with schemes based on first order logic and atomic diagrams. 
### Supported text languages: russian.
Программа позволяет: извлекать аргументацию из текстов естественного языка, преобразовать утверждения к специальной форме – ситуациям, сравнивать ситуации, находить аргументацию для утверждений естественного языка. Тексты для извлечения аргументации могут быть предоставлены пользователем или скачаны в автоматическом режиме.

### Requirments:
1. Windows 7+
2. Python 3.7+
    - Feedparser library
    - Beautifulsoup4 library
    - Wiki-ru-wordnet library
3. Java 11+
4. PostgreSQL 13.1+   

### Before launch:
Servers jar files, client jar, sql init files are located in "diplom_dist/" folder. 
1. Create tables with "create_tables.sql"
2. Start server for ontology interaction:
```
python ./wordnet.py
```
3. Start main server:
```
java -Dfile.encoding=UTF-8 -jar path_to_dir/server-1.0-SNAPSHOT.jar --spring.config.location=path_to_dir/application.properties.
```
4. Start client:
```
java --module-path "path_to_dir\openjfx-11.0.2_windows-x64_bin-sdk/javafx-sdk-11.0.2/lib//" --add-modules javafx.controls,javafx.fxml -Dfile.encoding=UTF-8 -jar  client.jar.
```
