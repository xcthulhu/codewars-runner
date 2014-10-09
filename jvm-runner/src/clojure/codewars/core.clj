(ns codewars.core
  (:require [cheshire.core :as json]
            [codewars.runners :refer [run]]
            [com.keminglabs.zmq-async.core :refer [register-socket!]]
            [clojure.core.async :refer [>! <! go chan sliding-buffer close!]]
            [cheshire.core :as json]
            [codewars.runners.groovy]
            [codewars.runners.clojure]
            [codewars.runners.java])
  (:gen-class))

(def ^:private zmq-addr "Address of the ZMQ socket"
  "inproc://zmq-intercom")

(defn- listen
  "Listen for jobs off of a ZMQ connection, runs jobs and replies"
  []
  (let [data-in (chan)
        data-out (chan)]
    (register-socket! {:in data-in
                       :out data-out
                       :socket-type :rep
                       :configurator (fn [socket] (.bind socket zmq-addr))})
    (loop []
      (->> (<! data-out)
           json/parse-string
           run
           json/generate-string
           (>! data-in))
      (recur))))

(defn -main
  "Listens to *in* for a JSON message, parses it and calls the appropriate runner"
  [& _]
  (let [input (json/parse-stream *in* true)]
    (run input)))
