(ns codewars.zmq
  (:require [cheshire.core :as json]
            [codewars.runners :refer [run]]
            [zeromq.zmq :as zmq]
            [clojure.pprint :refer [pprint]]
            [environ.core :refer [env]]
            [cheshire.core :as json]
            [codewars.runners.groovy]
            [codewars.runners.clojure]
            [codewars.runners.java]))

(def addr
  "Address of the ZMQ socket"
  (env :zmq-socket))

(defn status [message]
   {:type "JVM status"
    :status :read
    :message message})

(defn server
  "Listen for jobs off of a ZMQ connection, runs jobs and replies"
  []
  (with-open
      [socket (doto (zmq/socket (zmq/zcontext) :rep)
                (zmq/bind addr))]
    (println "Listening to" addr)
    (loop [message (json/parse-string (String. (zmq/receive socket)))]
      (if (map? message)
        (zmq/send-str socket (json/generate-string (run message)))
        (do (zmq/send-str socket (json/generate-string (status message)))
            (recur (json/parse-string (String. (zmq/receive socket))))))))
  :done)
