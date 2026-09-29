# AerolineaVS
Proyecto Aerolínea con Visual Studio Code + Maven + GitHub.

## Requisitos implementados
- CLI en Java para recomendar la tarifa más adecuada según reglas de negocio.
- Gestión con Maven.
- Tests con JUnit 5 y ejecución con Surefire.
- Cobertura con JaCoCo.
- Javadoc en código y generación vía Maven.

## Ejecutar tests
```bash
mvn test
```

## Generar informes de testing
```bash
mvn site:site
```

## Generar Javadoc
```bash
mvn javadoc:javadoc
```

## Ejecutar la CLI
```bash
mvn exec:java -Dexec.mainClass=com.aerolineavs.tarifas.App
```

## Ejecutar la interfaz gráfica
```bash
mvn -Pgui exec:java
```

## Suposiciones para evitar ambigüedades
- En la tarifa de estudiante, "al menos una vez al mes durante el curso" se interpreta como **9 viajes/año**.
- La frecuencia anual informada se usa como número de viajes relevantes para cada regla.
- Si no se cumple ninguna regla exacta, se devuelve "Sin tarifa aplicable".
