# Development server with Vite
FROM node:20-alpine

WORKDIR /app

# Copy package files and install
COPY package.json package-lock.json* ./
RUN npm install

# Copy source
COPY . .

# Set the backend URL for Vite
ARG VITE_BACKEND_URL=http://localhost:8080/api
ENV VITE_BACKEND_URL=${VITE_BACKEND_URL}

# Create .env file for Vite
RUN echo "VITE_BACKEND_URL=${VITE_BACKEND_URL}" > .env

EXPOSE 5173

CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]



