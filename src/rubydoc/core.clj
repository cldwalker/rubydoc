(ns rubydoc.core
  (:require [clj-yaml.core :as yaml]
            [clojure.java.io :as io]
            [table.core]))

(declare rows print-matches include? wrap-rows wrap-row)

(defn rubydoc
  "Searches database of ruby/clojure comparisons. Default search field is :ruby. Options:
  Options:

  :clj  Search clojure field.
  :all  Search all fields.
  "
  [str-or-regex & args]
  (let [matches? (if (instance? java.util.regex.Pattern str-or-regex)
                   #(re-find str-or-regex (str %))
                   #(.contains (str %) (str str-or-regex)))
        fields (cond
                 (include? args :clj) [:clj]
                 (include? args :all) [:ruby :clj :desc]
                 :else [:ruby])]
    (print-matches
      (filter #(some matches? ((apply juxt fields) %)) @rows))))

(defn- include? [v elem] (some #{elem} v))

(defn- print-matches [matches]
  (case (count matches)
    1 (->> (first matches) vec wrap-rows (cons ["field" "value"]) table.core/table)
    0 (println "No matches found.")
    (table.core/table matches)))

(defn- max-width []
  (- @table.width/*width* (+ 2 8 3 2)))

(defn- wrap-rows [rows]
  (reduce
    #(if (> (count (str (second %2))) (max-width)) (concat %1 (wrap-row %2)) (conj %1 %2))
    []
    rows))

(defn- wrap-row [[field value]]
  (let [rows (->> (re-seq (re-pattern (str ".{0," (max-width) "}")) value) (remove empty?))]
    (cons
      [field (first rows)]
      (map #(vec ["" %]) (rest rows)))))

(def ^:private rows
  (delay
    (->> (slurp (io/resource "rubydoc/db.yml")) yaml/parse-string)))
