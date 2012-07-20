(ns rubydoc.core
  (:require [clj-yaml.core :as yaml]
            [clojure.java.io :as io]
            [table.core]))

(declare rows print-matches)

(defn rubydoc [str-or-regex]
  (let [matches? (if (instance? java.util.regex.Pattern str-or-regex)
                   #(re-find str-or-regex (str %))
                   #(.contains (str %) (str str-or-regex)))]
    (print-matches
      (filter #(matches? (:ruby %)) @rows))))

(defn- print-matches [matches]
  (condp = (count matches)
    1 (->> (first matches) vec (cons ["field" "value"]) table.core/table)
    0 (println "No matches found.")
    (table.core/table matches)))

(def ^:private rows
  (let [dir (-> (ClassLoader/getSystemResource *file*) io/file .getParent)]
    (delay
      (->> (slurp (str dir "/db.yml")) yaml/parse-string))))
