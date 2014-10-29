IMAGES=alt dotnet func jvm node python ruby systems
ALL_IMAGES=${IMAGES} base

.PHONY: ${ALL_IMAGES} clean docker_rm docker_rmi


all: ${IMAGES}

base:
	cp $@.docker ../Dockerfile
	cd .. ; docker build -t codewars/runner-$@ .

${IMAGES}: base
	cp $@.docker ../Dockerfile
	cd .. ; docker build -t codewars/runner-$@ .
	make docker_rmi_temporary

# Kill temporary built images
docker_rmi_temporary:
	[ ! -n "$(shell docker images --no-trunc | grep none | sed -e 's/\s\s*/\t/g' | cut -f3)" ] || docker images --no-trunc | grep none | sed -e 's/\s\s*/\t/g' | cut -f3 | xargs -n 1 docker rmi -f

# Kill all of the in-flight and exited docker containers
docker_rm:
	[ ! -n "$(shell docker ps -a -q)" ] || echo $(shell docker ps -a -q) | xargs -n 1 docker rm -f

# Kill all docker images
docker_rmi: docker_rm
	[ ! -n "$(shell docker images -q)" ] || docker images -q | xargs -n 1 docker rmi -f 

clean: docker_rmi
