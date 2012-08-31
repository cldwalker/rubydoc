(ns rubydoc.core-test
  (:use clojure.test rubydoc.core)
  (:require clojure.string))

(defn unindent [string]
  (str (clojure.string/replace (clojure.string/trim string) #"\n\s*" "\n") "\n"))

(deftest prints-message-for-no-records
  (is (= "No records found.\n"
      (with-out-str (rubydoc "blarg")))))

(deftest prints-table-for-one-match
  (is (=
      (unindent
        "
        +-------+------------------------+
        | field | value                  |
        +-------+------------------------+
        | :id   | 4                      |
        | :ruby | Kernel#load            |
        | :clj  | clojure.core/load-file |
        +-------+------------------------+
        ")
      (with-out-str (rubydoc "load")))))

(deftest prints-wrapped-table-for-one-match
  (is (=
      (unindent
        "
        +----------+-----------------------------------------------------------------------------------------------------------------------------------------+
        | field    | value                                                                                                                                   |
        +----------+-----------------------------------------------------------------------------------------------------------------------------------------+
        | :id      | 16                                                                                                                                      |
        | :ruby    | Object#respond_to?                                                                                                                      |
        | :clj     | clojure.core/ns-resolve                                                                                                                 |
        | :similar | true                                                                                                                                    |
        | :desc    | Whereas respond_to? indicates if an object can call a method, ns-resolve indicates if a namespace can use a Var/Class. ns-resolve is di |
        |          | fferent in that it doesn't return a boolean or report only about functions. See clojure.core/resolve for assuming current namespace.    |
        +----------+-----------------------------------------------------------------------------------------------------------------------------------------+
        ")
       (with-out-str
         (binding [table.width/*width* (delay 150)]
           (rubydoc "respond_to"))))))

(deftest prints-table-for-regex-query-with-multiple-records
  (is (=
      (unindent
        "
        +----+-------------+------------------------+
        | id | ruby        | clj                    |
        +----+-------------+------------------------+
        | 2  | Kernel#exit | System/exit            |
        | 4  | Kernel#load | clojure.core/load-file |
        +----+-------------+------------------------+
        ")
      (with-out-str (rubydoc #"Kernel#[el]")))))

(deftest returns-correct-result-when-searching-clojure-field
  (is (=
      (unindent
        "
        +-------+-----------------------------------------------------------------------------------+
        | field | value                                                                             |
        +-------+-----------------------------------------------------------------------------------+
        | :id   | 104                                                                               |
        | :ruby | Array#unshift                                                                     |
        | :clj  | clojure.core/cons                                                                 |
        | :desc | See also clojure.core/conj which does this for lists but with arguments reversed. |
        +-------+-----------------------------------------------------------------------------------+
        ")
      (with-out-str (rubydoc "cons" :clj)))))

(deftest returns-correct-result-when-searching-all-fields
  (is (=
      (unindent
        "
        +-------+------------------------------------------------------------------+
        | field | value                                                            |
        +-------+------------------------------------------------------------------+
        | :id   | 17                                                               |
        | :ruby | Symbol#to_s                                                      |
        | :clj  | clojure.core/name                                                |
        | :desc | Use when wanting to stringify a clojure keyword aka ruby symbol. |
        +-------+------------------------------------------------------------------+
        ")
      (with-out-str (rubydoc "aka ruby" :all)))))

(deftest returns-correct-result-for-record-number
  (is (=
      (unindent
        "
        +-------+-----------------------+
        | field | value                 |
        +-------+-----------------------+
        | :id   | 0                     |
        | :ruby | Open3.capture3        |
        | :clj  | clojure.java.shell/sh |
        +-------+-----------------------+
        ")
      (with-out-str (rubydoc 0)))))

(deftest returns-correct-ruby-result-for-ruby-lib-query
  (is (=
      (unindent
        "
        +-----------+-----------------------------+
        | field     | value                       |
        +-----------+-----------------------------+
        | :id       | 130                         |
        | :ruby     | Tempfile.new                |
        | :ruby-lib | tempfile                    |
        | :clj      | java.io.File/createTempFile |
        +-----------+-----------------------------+
        ")
      (with-out-str (rubydoc "tempfile")))))

(deftest returns-no-result-for-record-number
  (is (=
      "No records found.\n"
      (with-out-str (rubydoc -1)))))

(deftest records-with-multiple-rubies-expand-with-duplicated-fields
  (is ((set (map #(dissoc % :id) @@#'rubydoc.core/records)) {:ruby "Set#add" :clj "clojure.core/concat"}))
  (is ((set (map #(dissoc % :id) @@#'rubydoc.core/records)) {:ruby "Set#+" :clj "clojure.core/concat"})))

(deftest all-records-have-required-fields
  (is (=
      '() (->> @@#'rubydoc.core/records (remove #(and (contains? % :ruby) (contains? % :clj)))))))

(deftest all-descriptions-end-in-a-period
  (is (=
      '() (->> @@#'rubydoc.core/records (map :desc) (remove nil?) (filter #(not (re-find #"\.$" %)))))))

(deftest all-clojure-functions-resolve
  (is
    (=
      '()
      (->> @@#'rubydoc.core/records
           (map :clj)
           (filter #(re-find #"^clojure\.\S+/\S+$" %))
           (map symbol)
           (remove resolve)
           ; autorequire namespaces that haven't been loaded
           ((fn [_ syms] (doseq [namesp (->> syms (map str) (map #(clojure.string/split % #"/")) (map first) distinct)]
                           (try (require (symbol namesp)) (catch Exception e)))
                          syms)
              :_)
           (remove resolve)))))
