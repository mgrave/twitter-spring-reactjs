# Usa una imagen base de Node.js
FROM node:14

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia package.json y package-lock.json al directorio de trabajo
COPY package*.json ./
RUN echo "package.json y package-lock.json copiados al directorio de trabajo"

# Instala las dependencias
RUN npm install && echo "Dependencias instaladas correctamente"

# Instala craco y ajv directamente
RUN npm install ajv@^8.0.0 && echo "ajv instalado correctamente"

# Copia el resto de los archivos de la aplicación
COPY . .
RUN echo "Archivos de la aplicación copiados"

# Expone el puerto en el que la aplicación va a correr
EXPOSE 3000

# Comando para correr la aplicación con rastreo de advertencias
CMD ["node", "--trace-deprecation", "node_modules/.bin/react-scripts", "start"]
