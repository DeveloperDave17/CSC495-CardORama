FROM node:19 as build

WORKDIR /app

COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . ./
RUN npm run build

FROM node:19-alpine3.16

WORKDIR /app
COPY --from=build /app .

ARG FRONTEND_PORT

ENV HOST=0.0.0.0
EXPOSE $FRONTEND_PORT
RUN echo "$FRONTEND_PORT"
ENV ENV_FRONTEND_PORT $FRONTEND_PORT
CMD "npm" "run" "preview" "--" "--host" "0.0.0.0" "--port" $ENV_FRONTEND_PORT
