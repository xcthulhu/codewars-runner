(defproject jvm-runner "0.1.1"
  :description "JVM Runner for codewars"
  :url "http://www.codewars.com/"
  :javac-target "1.8"
  :dependencies
  [[org.clojure/clojure "1.6.0"]
   [cheshire "5.3.1"] ; JSON
   [korma "0.3.0"]    ; ORM
   [com.h2database/h2 "1.3.170"] ; In Memory Database
   [org.xerial/sqlite-jdbc "3.7.15-M1"] ; SQLite
   [com.novemberain/monger "2.0.0"] ; Monger
   [com.taoensso/carmine "2.7.0" :exclusions [org.clojure/clojure]] ; Carmine
   [org.clojure/core.async "0.1.346.0-17112a-alpha"] ; Go-Routines
   [org.clojure/test.check "0.5.9"] ; Generative Testing
   [prismatic/schema "0.2.6"] ; Schema Verification
   [instaparse "1.3.4"] ; BNF Parsers
   [org.clojure/core.logic "0.8.8"] ; Logic Programming
   [org.clojure/core.match "0.2.1"] ; Pattern Matching
   [prismatic/plumbing "0.3.3"] ; Random Awesomeness
   [clj-http "1.0.0"] ; HTTP Client
   [enlive "1.1.5"] ; HTML templating
   [hiccup "1.0.5"] ; Clojure HTML DSL
   [junit/junit "4.11"] ; JUnit
   [org.codehaus.groovy/groovy-all "2.3.6"] ; Groovy
   [environ "0.5.0"] ; Environment Variables
   [org.zeromq/jeromq "0.3.3"] ; ZMQ
   [org.zeromq/cljzmq "0.1.4" :exclusions [org.zeromq/jzmq]]
   ]
  :ring {:handler codewars.rest.handler/app}
  :plugins [[lein-environ "0.5.0"] [lein-ring "0.8.12"]]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :env {:timeout "10000"
        :zmq-socket "tcp://127.0.0.1:12349"}
  :main codewars.core)
