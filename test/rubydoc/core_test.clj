(ns rubydoc.core-test
  (:use clojure.test
        rubydoc.core))

(defn unindent [string]
  (str (clojure.string/replace (clojure.string/trim string) #"\n\s*" "\n") "\n"))

(deftest prints-message-for-no-matches
  (is (= "No matches found.")
      (with-out-str (rubydoc "blarg"))))

(deftest prints-table-for-one-match
  (is (=
      (unindent
        "
        +-------+------------------------+
        | field | value                  |
        +-------+------------------------+
        | :ruby | Kernel#load            |
        | :clj  | clojure.core/load-file |
        +-------+------------------------+
        ")
      (with-out-str (rubydoc "load")))))

(deftest prints-table-for-regex-query-with-multiple-matches
  (is (=
      (unindent
        "
        +-------------+------------------------+
        | ruby        | clj                    |
        +-------------+------------------------+
        | Kernel#exit | System/exit            |
        | Kernel#load | clojure.core/load-file |
        +-------------+------------------------+
        ")
      (with-out-str (rubydoc #"Kernel#[el]")))))

(deftest returns-correct-result-when-searching-clojure-field
  (is (=
      (unindent
        "
        +-------+-----------------------------------------------------------------------------------+
        | field | value                                                                             |
        +-------+-----------------------------------------------------------------------------------+
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
        | :ruby | Symbol#to_s                                                      |
        | :clj  | clojure.core/name                                                |
        | :desc | Use when wanting to stringify a clojure keyword aka ruby symbol. |
        +-------+------------------------------------------------------------------+
        ")
      (with-out-str (rubydoc "aka ruby" :all)))))
