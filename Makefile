SHELL := /bin/sh

MVNW := ./mvnw
KEY_DIR := src/main/resources/certs
PRIVATE_KEY := $(KEY_DIR)/private.pem
PUBLIC_KEY := $(KEY_DIR)/public.pem
OPENSSL ?= openssl

.PHONY: help keys rotate-keys run build test clean

help:
	@printf '%s\n' \
		'make keys         Generate JWT keys only when missing' \
		'make rotate-keys  Replace the current JWT key pair' \
		'make run          Run the Spring Boot application' \
		'make build        Build the application without tests' \
		'make test         Run the test suite' \
		'make clean        Remove Maven build output'

keys:
	@mkdir -p "$(KEY_DIR)"
	@if [ ! -f "$(PRIVATE_KEY)" ] || [ ! -f "$(PUBLIC_KEY)" ]; then \
		$(MAKE) rotate-keys; \
	else \
		printf '%s\n' 'JWT key pair already exists.'; \
	fi

rotate-keys:
	@command -v "$(OPENSSL)" >/dev/null 2>&1 || { printf '%s\n' 'OpenSSL is required.' >&2; exit 1; }
	@mkdir -p "$(KEY_DIR)"
	@$(OPENSSL) genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out "$(PRIVATE_KEY)"
	@$(OPENSSL) pkey -in "$(PRIVATE_KEY)" -pubout -out "$(PUBLIC_KEY)"
	@chmod 600 "$(PRIVATE_KEY)"
	@printf '%s\n' 'JWT key pair rotated. Existing access and refresh JWTs are now invalid.'

run: keys
	@$(MVNW) spring-boot:run

build: keys
	@$(MVNW) -DskipTests package

test: keys
	@$(MVNW) test

clean:
	@$(MVNW) clean