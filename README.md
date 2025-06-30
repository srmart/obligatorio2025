Este programa cumple con las funciones requeridas para realizar las consultas requeridas por UMovie.
Mover los csv con la metadata a la raíz del proyecto para el correcto funcionamiento del mismo.
Se utilizaron dependencias para lombok, openCSV y org.json:
    </dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.38</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.opencsv</groupId>
            <artifactId>opencsv</artifactId>
            <version>5.11</version>
        </dependency>
        <dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>
            <version>20231013</version>
        </dependency>
    </dependencies>
