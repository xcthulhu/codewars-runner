IMAGES=alt dotnet func jvm node python ruby systems
ALL_IMAGES=${IMAGES} base

.PHONY: ${ALL_IMAGES} clean docker_rm docker_rmi

all: ${IMAGES}

base:
	cp $@.docker ../Dockerfile
	cd .. ; docker build -t codewars/$@ .

${IMAGES}: base
	cp $@.docker ../Dockerfile
	cd .. ; docker build -t codewars/$@-runner .

# Remove docker processes that have exited cleanly
docker_rm_exited:
	[ ! -n "$(shell docker ps -a | grep Exit | cut -d ' ' -f 1)" ] || echo $(shell docker ps -a | grep Exit | cut -d ' ' -f 1) | xargs -n 1 docker rm -f

# Kill temporary built images might fail if not exited cleanly
docker_rmi_temporary:
	[ ! -n "$(shell docker images --no-trunc | grep none | sed -e 's/\s\s*/\t/g' | cut -f3)" ] || docker images --no-trunc | grep none | sed -e 's/\s\s*/\t/g' | cut -f3 | xargs -n 1 docker rmi -f

# Kill all of the in-flight and exited docker containers
docker_rm:
	[ ! -n "$(shell docker ps -a -q)" ] || echo $(shell docker ps -a -q) | xargs -n 1 docker rm -f

# Kill all docker images
docker_rmi: docker_rm
	[ ! -n "$(shell docker images -q)" ] || docker images -q | xargs -n 1 docker rmi -f 

clean: docker_rm_exited docker_rmi_temporary
	for i in $(shell for i in codewars/base $(patsubst %, codewars/%-runner, $(IMAGES)) ; do docker images | grep $$i | sed -e 's/\s\s*/\t/g' | cut -f1; done) ; do docker rmi -f $$i ; done

deep-clean: docker_rmi
