(ns codewars.util
  (:require [clojure.java.io :as io]
            [environ.core :refer [env]])
  (:import [java.io StringWriter PrintWriter PrintStream ByteArrayOutputStream]
           [java.util.concurrent TimeoutException ExecutionException TimeUnit FutureTask]))

(defn slurp-bytes
  "Slurp the bytes from a slurpable thing"
  [x]
  (with-open [out (ByteArrayOutputStream.)]
    (io/copy (io/input-stream x) out)
    (.toByteArray out)))

(def timeout "Timeout in milliseconds"
  ((fnil #(Integer/parseInt %) "10000") (env :timeout)))

;; Adapted from https://github.com/Raynes/clojail/blob/master/src/clojail/core.clj#L24
(defn thunk-timeout
  "Takes a function and an amount of time to wait for the function to finish executing."
  [thunk ms]
  (let [task (FutureTask. thunk)
        thr (Thread. task)]
    (try
      (.start thr)
      (.get task ms TimeUnit/MILLISECONDS)
      (catch TimeoutException e
        (.cancel task true)
        (.stop thr)
        (throw (TimeoutException. (str "Timeout: " ms " msecs"))))
      (catch ExecutionException e
        (.cancel task true)
        (.stop thr)
        (throw (.getCause e)))
      (catch Exception e
        (.cancel task true)
        (.stop thr)
        (throw e)))))

(defmacro with-timeout
  "Run a body of code for a given timeout"
  [ms & body]
    `(thunk-timeout (fn [] ~@body) ~ms))

(defmacro catch-and-wrap
  "Run a command, catch if it throws and write an error"
  [& body]
  `(try
     ~@body
     (catch Exception e#
       (let [sw# (StringWriter.)]
         (.printStackTrace e# (PrintWriter. sw#))
         {:stderr
          (-> (str sw#)
              (clojure.string/replace "\n" "<:LF:>")
              (->> (str "<ERROR::>" (.getMessage e#) "<:LF:>")))
          :stdout ""
          :result nil}))))

(defmacro wrap-result
  "Run a command, catch its stdout and stderr, and add them as fields in a hash-map containing the results"
  [& body]
  `(let [original-java-out# (PrintStream. System/out)
         original-java-err# (PrintStream. System/err)
         ;; TODO: Wish this wasn't so complicated
         clj-out# (StringWriter.)
         clj-err# (StringWriter.)
         out# (ByteArrayOutputStream.)
         err# (ByteArrayOutputStream.)]
     (with-redefs
       [*out* clj-out#
        *err* clj-err#]
       (-> out# PrintStream. System/setOut)
       (-> err# PrintStream. System/setErr)
       (try
         (catch-and-wrap
          (let [result# (do ~@body)]
            (flush)
            {:stdout (str clj-out# out#)
             :stderr (str clj-err# err#)
             :result result#}))
         (finally
           (System/setOut original-java-out#)
           (System/setErr original-java-err#))))))

(def ^:private
  language-data
  {"clojure" {:pattern #"^\(ns\s+([A-Z|a-z](?:[a-z|A-Z|0-9|-]|\.[A-Z|a-z])*)\W"
              :extension "clj"}

   "java" {:pattern #"\bclass\s+([A-Z][a-z|A-Z|0-9|_]*)\W"
           :extension "java"}

   "groovy" {:pattern #"^package\s+([a-z](?:[a-z|A-Z|0-9|-]|\.[a-z])*)\W"
             :extension "groovy"}})

(defn class-name
  "Infer the appropriate class or namespace name from code given a language"
  [language code]
  (let [{:keys [:pattern]} (get language-data language)]
    ;; TODO: Filter comments
    (if-let [name (->> code (re-find pattern) second symbol)]
      name
      (throw
       (IllegalArgumentException.
        (format "Could not infer class or namespace name for language %s from code:\n%s\n" (pr-str language) code))))))

(defn class-name-to-file-name
  "Create a file name for a class or namespace name given a language"
  [language class-name]
  (let [{:keys [:extension]} (get language-data language)]
    (-> class-name
        name
        (clojure.string/replace "-" "_")
        ;; TODO: not platform independent...
        (clojure.string/replace "." "/")
        (str "." extension)
        io/file)))

(defn write-code!
  "Write code to an appropriate file in a specified directory given a language"
  [language dir code]
  (let [class-name (class-name language code)
        base-name (class-name-to-file-name language class-name)
        file-name (io/file dir base-name)]
    (if (.exists file-name)
      (throw
       (UnsupportedOperationException.
        (format "Could not write to file %s, because that file already exists.  Perhaps it already contains the setup or test fixture code?\ncode:\n%s" (pr-str base-name) code)))
      ;; else
      (do
        (-> file-name .getParentFile .mkdirs)
        (spit file-name code)
        {:file-name file-name
         :class-name class-name}))))
