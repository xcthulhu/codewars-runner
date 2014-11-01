# codewars-runner

This project is both a command-line utility and server, used by [Codewars](http://www.codewars.com) to execute small sets of code within various languages, using various testing frameworks.

You can run `node run --help` to view information about which arguments are supported.

## Purpose

The purpose of this project is to provide a low level ability to run 'Kata'. It provides a mechanism for executing different configurations of code using various languages and testing frameworks.

Docker can be utilized in order to sandbox code execution. A server is provided that can accept 'run' requests and execute them within a Docker container.

## Supported Languages and Testing Frameworks

- JavaScript
	- codewars

- CoffeeScript
	- codewars

- Ruby
	- rspec
	- codewars

- Python
	- unittest
	- codewars

- Java
	- junit

- C#
	- nunit

- Haskell
	- [hspec](http://hspec.github.io)

- Clojure
	- [clojure.test](https://clojure.github.io/clojure/clojure.test-api.html)
