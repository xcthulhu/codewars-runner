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
  {:type "status"
   :status :ready
   :message message})

(defn- server
  "ZMQ request server; should only be one instance running or ZMQ throws a fit"
  []
  (with-open
      [socket (doto (zmq/socket (zmq/zcontext) :rep) (zmq/connect addr))]
    (loop [message (-> (zmq/receive socket) String. (json/parse-string true))]
      (if (map? message)

        (let [result (run message)]
          (-> result (assoc :type "execution response") json/generate-string (->> (zmq/send-str socket)))
          result)

        (do
          (-> (status message) json/generate-string (->> (zmq/send-str socket)))
          (recur (-> (zmq/receive socket) String. (json/parse-string true))))))))

;; TODO: This shouldn't be necessary, but ZMQ hangs without it
(def ^:private zmq-daemon (atom (future-call server)))

(defn listen
  "Listen for jobs off of a ZMQ connection, runs jobs and replies"
  []
  (when (future-done? @zmq-daemon)
    (swap! zmq-daemon (constantly (future-call server))))
  @@zmq-daemon)
