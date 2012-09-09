(ns rubydoc.core
  (:require [clojure.java.io :as io]
            [table.core]))

(declare records print-records include? wrap-records wrap-record)

(defn rubydoc
  "Searches database of ruby-clojure records using a string, regex or record id.
  Default search field is :ruby.
  Options:

  :clj  Search clojure field.
  :all  Search all fields.
  "
  [query & args]
  (print-records
    (if (integer? query)
      (let [result (get @records query)] (if (nil? result) [] [result]))
      (let [matches? (if (instance? java.util.regex.Pattern query)
                       #(re-find query (str %))
                       #(.contains (str %) (str query)))
            fields (cond
                     (some #{:clj} args) [:clj]
                     (some #{:all} args) [:ruby :ruby-lib :clj :desc]
                     (some #{:type} args) [:type]
                     :else [:ruby :ruby-lib])
            recs (if (= fields [:type]) @records (map #(dissoc % :type) @records))]
      (filter #(some matches? ((apply juxt fields) %)) recs)))))

(defn- print-records [recs]
  (case (count recs)
    1 (->> (first recs) wrap-records (cons ["field" "value"]) table.core/table)
    0 (println "No records found.")
    (table.core/table recs)))

(defn- wrap-records [recs]
  (let [field-length (apply max (map (comp count str first) recs))
        ; 2 3 2 corresponds to outer + inner border lengths
        max-width (- @table.width/*width* (+ 2 3 2 field-length))]
    (reduce
      #(if (> (count (str (second %2))) max-width)
         (concat %1 (wrap-record %2 max-width)) (conj %1 %2))
      [] recs)))

(defn- wrap-record [[field value] max-width]
  (let [recs (->> (re-seq (re-pattern (str ".{0," max-width "}")) value) (remove empty?))]
    (cons
      [field (first recs)]
      (map #(vec ["" %]) (rest recs)))))

(def ^:private records
  (delay
    (->>
      (slurp (io/resource "rubydoc/db.clj"))
      read-string
      (mapcat (fn [{:keys [ruby] :as record}]
                (let [rubies (if (vector? ruby) ruby [ruby])]
                (map #(assoc record :ruby %) rubies))))
      (map #(assoc % :type (or (:type %) "fn")))
      (map-indexed #(assoc %2 :id %1))
      vec)))
