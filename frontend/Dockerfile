# Stage 1: Build frontend
FROM node:20-alpine AS build

WORKDIR /app

# Copy package files and install
COPY package.json package-lock.json* ./

RUN npm install

# Copy source and environment
COPY . .

COPY .env .env

# Build production assets
RUN npm run build

# Stage 2: Development server
FROM node:20-alpine

WORKDIR /app

# Copy package files and install
COPY package.json package-lock.json* ./
RUN npm install

# Copy source code
COPY . .

EXPOSE 5173

CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
