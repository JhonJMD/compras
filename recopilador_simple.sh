#!/bin/bash

# Script simple para extraer archivos importantes de un proyecto SpringBoot
# Guarda todos los archivos en una carpeta plana y crea un archivo de rutas

# Colores para mensajes
VERDE='\033[0;32m'
AMARILLO='\033[0;33m'
ROJO='\033[0;31m'
NC='\033[0m' # Sin Color

# Directorio base (por defecto: directorio actual)
DIR_BASE="${1:-$PWD}"
# Directorio destino
FECHA=$(date +"%Y%m%d_%H%M%S")
DIR_DESTINO="springboot_archivos_${FECHA}"

# Crear directorio destino
mkdir -p "$DIR_DESTINO"

# Archivo para guardar las rutas originales
RUTAS_FILE="$DIR_DESTINO/rutas_originales.txt"
echo "# Rutas originales de los archivos" > "$RUTAS_FILE"
echo "# Formato: NOMBRE_ARCHIVO -> RUTA_ORIGINAL" >> "$RUTAS_FILE"
echo "# Fecha de extracción: $(date)" >> "$RUTAS_FILE"
echo "# Directorio base: $DIR_BASE" >> "$RUTAS_FILE"
echo "" >> "$RUTAS_FILE"

echo -e "${VERDE}Extrayendo archivos de $DIR_BASE a $DIR_DESTINO...${NC}"

# Contador de archivos
CONTADOR=0

# Función para extraer archivos relevantes
extraer_archivos() {
    local patron="$1"
    local descripcion="$2"
    
    echo -e "${VERDE}Buscando $descripcion...${NC}"
    
    find "$DIR_BASE" -type f -name "$patron" \
    -not -path "*/target/*" \
    -not -path "*/build/*" \
    -not -path "*/.git/*" \
    -not -path "*/.idea/*" \
    -not -path "*/.gradle/*" \
    -not -path "*/out/*" \
    -not -path "*/node_modules/*" \
    -not -path "*/bin/*" \
    -print0 | while IFS= read -r -d $'\0' archivo; do
        # Obtener solo el nombre del archivo (sin ruta)
        nombre_base=$(basename "$archivo")
        
        # Si ya existe un archivo con el mismo nombre, añadir un sufijo
        if [ -f "$DIR_DESTINO/$nombre_base" ]; then
            # Extraer el directorio padre para usarlo como sufijo
            dir_padre=$(basename "$(dirname "$archivo")")
            nombre_base="${dir_padre}_${nombre_base}"
            
            # Si sigue existiendo, añadir un contador
            if [ -f "$DIR_DESTINO/$nombre_base" ]; then
                nombre_base="$(echo $nombre_base | sed 's/\.[^.]*$//')_${CONTADOR}.${nombre_base##*.}"
            fi
        fi
        
        # Copiar el archivo
        cp "$archivo" "$DIR_DESTINO/$nombre_base"
        
        # Registrar la ruta original
        echo "$nombre_base -> $archivo" >> "$RUTAS_FILE"
        
        # Incrementar el contador
        CONTADOR=$((CONTADOR + 1))
    done
}

# Extraer archivos Java
extraer_archivos "*.java" "archivos Java"

# Extraer archivos de configuración
extraer_archivos "*.properties" "archivos properties"
extraer_archivos "*.yml" "archivos YAML"
extraer_archivos "*.yaml" "archivos YAML"
extraer_archivos "*.xml" "archivos XML"
extraer_archivos "*.json" "archivos JSON"

# Extraer archivos de build
extraer_archivos "pom.xml" "archivos Maven"
extraer_archivos "*.gradle" "archivos Gradle"
extraer_archivos "build.gradle" "archivo build.gradle"
extraer_archivos "settings.gradle" "archivo settings.gradle"

# Extraer archivos SQL
extraer_archivos "*.sql" "archivos SQL"

# Extraer archivos de documentación
extraer_archivos "*.md" "archivos Markdown"

# Extraer archivos Docker
extraer_archivos "Dockerfile" "Dockerfile"
extraer_archivos "docker-compose*" "docker-compose"

# Crear un archivo de información adicional
echo "# Información sobre el proyecto" > "$DIR_DESTINO/info.txt"
echo "Fecha de extracción: $(date)" >> "$DIR_DESTINO/info.txt"
echo "Directorio base: $DIR_BASE" >> "$DIR_DESTINO/info.txt"
echo "Total de archivos: $CONTADOR" >> "$DIR_DESTINO/info.txt"

# Intentar determinar información adicional sobre el proyecto
if [ -f "$DIR_BASE/pom.xml" ]; then
    echo "Sistema de build: Maven" >> "$DIR_DESTINO/info.txt"
    # Intentar extraer el groupId y artifactId
    if command -v grep &> /dev/null && command -v sed &> /dev/null; then
        GROUP_ID=$(grep -m 1 "<groupId>" "$DIR_BASE/pom.xml" | sed 's/.*<groupId>\(.*\)<\/groupId>.*/\1/')
        ARTIFACT_ID=$(grep -m 1 "<artifactId>" "$DIR_BASE/pom.xml" | sed 's/.*<artifactId>\(.*\)<\/artifactId>.*/\1/')
        echo "GroupId: $GROUP_ID" >> "$DIR_DESTINO/info.txt"
        echo "ArtifactId: $ARTIFACT_ID" >> "$DIR_DESTINO/info.txt"
    fi
elif [ -f "$DIR_BASE/build.gradle" ]; then
    echo "Sistema de build: Gradle" >> "$DIR_DESTINO/info.txt"
fi

# Comprimir todos los archivos
ZIP_ARCHIVO="${DIR_DESTINO}.zip"
echo -e "${VERDE}Comprimiendo archivos en $ZIP_ARCHIVO...${NC}"
zip -r "$ZIP_ARCHIVO" "$DIR_DESTINO" > /dev/null

# Resumen
echo ""
echo -e "${VERDE}¡Proceso completado!${NC}"
echo "Se han extraído $CONTADOR archivos"
echo "Carpeta de archivos: $DIR_DESTINO"
echo "Archivo ZIP: $ZIP_ARCHIVO"
echo "Archivo con rutas originales: $RUTAS_FILE"