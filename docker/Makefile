IMAGES=alt dotnet func jvm node python ruby systems
VERSION=$(shell cd .. ; node run -v)

.PHONY:${IMAGES} base

all: ${IMAGES}

base: 
	cp $@.docker ../Dockerfile
	cd .. ; docker build -t codewars/runner-$@:${VERSION} .

${IMAGES}: base
	cp $@.docker ../Dockerfile
	cd .. ; docker build -t codewars/runner-$@:${VERSION} .
