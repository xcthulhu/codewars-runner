(ns codewars.name-space)

(defmulti name-space (fn [x & _] x))

(defmethod name-space :clojure
  [_ code]
  (if-let [result (re-find #"^\(ns\s+([A-Z|a-z](?:[a-z|A-Z|0-9|-]|\.[A-Z|a-z])*)\W" code)]
    (-> result second)
    (throw (java.text.ParseException. (str "Failed to parse clojure namespace in code:\n\n" code "\n\n") 0))))

(defmethod name-space :java
  [_ code]
  (let [package-name (second (re-find #"\bpackage\s+([A-Z|a-z](?:[a-z|A-Z|0-9|_]|\.[A-Z|a-z])*)\W" code))
        class-name (second (re-find #"\bclass\s+([A-Z][a-z|A-Z|0-9|_]*)\W" code))]
    (cond
     (nil? class-name) (throw (java.text.ParseException.
                               (str "Failed to parse java class in code:\n\n" code "\n\n") 0))
     (nil? package-name) class-name
     :else (str package-name "." class-name))))
