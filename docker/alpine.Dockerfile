FROM eclipse-temurin:17.0.12_7-jdk-alpine

LABEL maintainer="ySenih@erpya.com; EdwinBetanc0urt@outlook.com;" \
	description="ADempiere gRPC All In One Server used as ADempiere adempiere-grpc-server"

# Init ENV with default values
ENV \
	SERVER_PORT="50059" \
	SERVER_LOG_LEVEL="WARNING" \
	DB_HOST="localhost" \
	DB_PORT="5432" \
	DB_NAME="adempiere" \
	DB_USER="adempiere" \
	DB_PASSWORD="adempiere" \
	DB_TYPE="PostgreSQL" \
	IDLE_TIMEOUT="300" \
	MINIMUM_IDLE="1" \
	MAXIMUM_POOL_SIZE="10" \
	CONNECTION_TIMEOUT="5000" \
	MAXIMUM_LIFETIME="6000" \
	KEEPALIVE_TIME="360000" \
	CONNECTION_TEST_QUERY="\"SELECT 1\"" \
	JAVA_OPTIONS="\"-Xms64M\" \"-Xmx1512M\"" \
	SYSTEM_LOGO_URL="" \
	TZ="America/Caracas"

EXPOSE ${SERVER_PORT}


# Add operative system dependencies
RUN rm -rf /tmp/* && \
	apk update && \
	apk add --no-cache \
		tzdata \
		bash \
		fontconfig \
		ttf-dejavu \
		msttcorefonts-installer && \
	fc-cache -f && \
	echo "Set Timezone..." && \
	echo $TZ > /etc/timezone


WORKDIR /opt/apps/server

# Copy src files
COPY docker/adempiere-grpc-server /opt/apps/server
COPY docker/env.yaml /opt/apps/server/env.yaml
COPY docker/start.sh /opt/apps/server/start.sh


# Add adempiere as user
RUN addgroup adempiere && \
	adduser --disabled-password --gecos "" --ingroup adempiere --no-create-home adempiere && \
	chown -R adempiere /opt/apps/server/ && \
	chmod +x start.sh

USER adempiere

# Start app
ENTRYPOINT ["sh" , "start.sh"]
