(defproject rubydoc "0.3.0"
  :description "A repl tool to help rubyists find clojure equivalents."
  :url "http://github.com/cldwalker/rubydoc"
  :license {:name "The MIT License"
            :url "https://en.wikipedia.org/wiki/MIT_License"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [table "0.4.0"]]
  :profiles {:1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}}
  :aliases {"all" ["with-profile" "dev:dev,1.4"]})
