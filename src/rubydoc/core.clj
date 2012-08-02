(ns rubydoc.core
  (:require [clojure.java.io :as io]
            [table.core]))

(declare rows print-matches include? wrap-rows wrap-row)

(defn rubydoc
  "Searches database of ruby-clojure comparisons using a string, regex or record id.
  Default search field is :ruby.
  Options:

  :clj  Search clojure field.
  :all  Search all fields.
  "
  [query & args]
  (print-matches
    (if (integer? query)
      (let [result (get @rows query)] (if (nil? result) [] [result]))
      (let [matches? (if (instance? java.util.regex.Pattern query)
                       #(re-find query (str %))
                       #(.contains (str %) (str query)))
            fields (cond
                     (include? args :clj) [:clj]
                     (include? args :all) [:ruby :clj :desc]
                     :else [:ruby])]
      (filter #(some matches? ((apply juxt fields) %)) @rows)))))

(defn- include? [v elem] (some #{elem} v))

(defn- print-matches [matches]
  (case (count matches)
    1 (->> (first matches) wrap-rows (cons ["field" "value"]) table.core/table)
    0 (println "No matches found.")
    (table.core/table matches)))

(defn- wrap-rows [rows]
  (let [field-length (apply max (map (comp count str first) rows))
        ; 2 3 2 corresponds to outer + inner border lengths
        max-width (- @table.width/*width* (+ 2 3 2 field-length))]
    (reduce
      #(if (> (count (str (second %2))) max-width)
         (concat %1 (wrap-row %2 max-width)) (conj %1 %2))
      [] rows)))

(defn- wrap-row [[field value] max-width]
  (let [rows (->> (re-seq (re-pattern (str ".{0," max-width "}")) value) (remove empty?))]
    (cons
      [field (first rows)]
      (map #(vec ["" %]) (rest rows)))))

(def ^:private rows
  (delay
    (->>
      (slurp (io/resource "rubydoc/db.clj"))
      read-string
      (map-indexed #(assoc %2 :id %1))
      vec)))
