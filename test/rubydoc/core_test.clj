(ns rubydoc.core-test
  (:use clojure.test rubydoc.core)
  (:require clojure.string))

(defn unindent [string]
  (str (clojure.string/replace (clojure.string/trim string) #"\n\s*" "\n") "\n"))

(defn raw-records []
  (rubydoc "" :raw))

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
        +----+--------------+-----------------------+----------+
        | id | ruby         | clj                   | ruby-lib |
        +----+--------------+-----------------------+----------+
        | 2  | Kernel#exit  | System/exit           |          |
        | 6  | Kernel#puts  | clojure.core/println  |          |
        | 7  | Kernel#print | clojure.core/print    |          |
        | 8  | Kernel#pp    | clojure.pprint/pprint | pp       |
        +----+--------------+-----------------------+----------+
        ")
      (with-out-str (rubydoc #"Kernel#[ep]")))))


(deftest prints-correct-result-for-record-number
  (is (=
      (unindent
        "
        +-----------+-----------------------+
        | field     | value                 |
        +-----------+-----------------------+
        | :id       | 0                     |
        | :type     | fn                    |
        | :ruby     | Open3.capture3        |
        | :clj      | clojure.java.shell/sh |
        | :ruby-lib | open3                 |
        +-----------+-----------------------+
        ")
      (with-out-str (rubydoc 0)))))

(deftest prints-no-result-for-record-number
  (is (=
      "No records found.\n"
      (with-out-str (rubydoc -1)))))

(deftest returns-correct-result-for-clj-option
  (is (=
      '("clojure.core/cons")
      (map :clj (rubydoc "cons" :clj :raw)))))

(deftest returns-correct-result-for-all-option
  (is (=
        '("Symbol#to_s")
        (map :ruby (rubydoc "aka ruby" :all :raw)))))

(deftest returns-raw-results-for-raw-option
  (is (=
    [{:id 30, :ruby "ENV", :clj "System/getenv",
      :desc "To get specific env values, pass the env name to getenv."}]
    (rubydoc "ENV" :raw))))

(deftest returns-correct-ruby-result-for-ruby-lib-option
  (is (=
        '("Tempfile.new")
        (map :ruby (rubydoc "tempfile" :raw)))))

(deftest returns-correct-ruby-result-for-type-option
  (is (=
        '("new" "throw" "clojure.core/comment" "clojure.core/case" "if" "if-not" "defn" "clojure.core/while" "catch" "finally" "try")
        (map :clj (rubydoc "keyword" :type :raw)))))

(deftest records-without-type-default-to-fn
  (is (=
        "fn"
        (->> (rubydoc 0 :raw) (map :type) first))))

(deftest records-with-multiple-rubies-expand-with-duplicated-fields
  (is ((set (map #(dissoc % :id :ruby-lib :type) (raw-records))) {:ruby "Set#add" :clj "clojure.core/concat"}))
  (is ((set (map #(dissoc % :id :ruby-lib :type) (raw-records))) {:ruby "Set#+" :clj "clojure.core/concat"})))

(deftest all-records-have-required-fields
  (is (=
      '() (->> (raw-records) (remove #(and (contains? % :ruby) (contains? % :clj)))))))

(deftest all-descriptions-end-in-a-period
  (is (=
      '() (->> (raw-records) (map :desc) (remove nil?) (filter #(not (re-find #"\.$" %)))))))

(deftest all-types-are-valid
  (is (=
        #{"fn" "constant" "code" "keyword" "variable" "lib"}
        (->> @@#'rubydoc.core/records (map :type) distinct set))))

(deftest all-fn-types-have-valid-ruby-methods
  (is (=
        '()
        (->> (rubydoc "fn" :type :raw)
          (map :ruby)
          (remove #(re-find #"^[A-Za-z:\d]+[#\.]\S+$" %))))))

(deftest all-fn-types-have-consistent-clj-fns
  (is (=
        '()
        (->> (rubydoc "fn" :type :raw)
          (map :clj)
          (remove #(re-find #"^[\w\.]+/\S+$" %))))))

(deftest all-clojure-functions-resolve
  (is
    (=
      '()
      (->> (raw-records)
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
