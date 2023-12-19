# Dockerfile

# Use an official Node.js runtime as a parent image

FROM node:18-bullseye-slim

# Set the working directory to /app

WORKDIR /app

# Copy package.json and package-lock.json to the working directory

COPY package\*.json ./

# Install the project dependencies

RUN npm install

# Copy the content of the local src directory to the working directory

COPY . .

# Make port 3000 available to the world outside this container

EXPOSE 9000

ENV DB_NAME=capstone
ENV DB_PORT=3306
ENV DB_HOST=34.16.40.205
ENV DB_USER=root
ENV DB_PASS=password123456789
ENV SECRET_KEY=Password123

# Run the application

CMD ["npm", "start"]