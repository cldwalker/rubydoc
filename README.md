# Description

A repl tool to help rubyists find clojure equivalents. This project
provides a rubydoc command to search the included db of ruby/clojure
comparisons. Compares versions >= ruby 1.9.3 and clojure 1.4.0.

[![Build Status](https://secure.travis-ci.org/cldwalker/rubydoc.png?branch=master)](http://travis-ci.org/cldwalker/rubydoc)

## Install

To have it available on all projects, add to your leiningen2's ~/lein/profiles.clj:

    {:user {:dependencies { rubydoc "0.2.0"}}}

To have it on an individual project, add to your project.clj:

    [rubydoc "0.2.0"]

## Usage

To use in a library:

    (use 'rubydoc.core)

Let's search for ruby equivalents in clojure:

    $ lein repl
    user=> (use 'rubydoc.core)
    nil

    ; Pull up clojure equivalents to Kernel methods
    user=> (rubydoc "Kernel")
    +-----+--------------------------------+--------------------------------+---------+--------------------------------------------------------+
    | id  | ruby                           | clj                            | similar | desc                                                   |
    +-----+--------------------------------+--------------------------------+---------+--------------------------------------------------------+
    | 1   | Kernel#system                  | clojure.java.shell/sh          | true    | sh executes commands but unlike system, stdout is c... |
    | 2   | Kernel#exit                    | System/exit                    |         |                                                        |
    | 3   | Kernel#require                 | clojure.core/require           |         | They are mostly the same though the clojure version... |
    | 4   | Kernel#load                    | clojure.core/load-file         |         |                                                        |
    | 6   | Kernel#puts                    | clojure.core/println           |         |                                                        |
    | 7   | Kernel#print                   | clojure.core/print             |         |                                                        |
    | 8   | Kernel#pp                      | clojure.pprint/pprint          |         | The ruby meth comes from requiring 'pp', a file in ... |
    | 9   | Kernel#sleep                   | Thread/sleep                   |         |                                                        |
    | 10  | Kernel#at_exit                 | (.addShutdownHook (Runtime/... | true    | Whereas at_exits are run in the reverse order defin... |
    | 15  | Kernel#raise                   | throw                          |         |                                                        |
    | 23  | IO.read and Kernel#open fro... | clojure.core/slurp             |         | slurp is a generalize read, reading anything java.i... |
    | 64  | String#% or Kernel#sprintf     | clojure.core/format            |         |                                                        |
    | 106 | Kernel#warn                    | (binding [*out* *err*] (pri... |         |                                                        |
    +-----+--------------------------------+--------------------------------+---------+--------------------------------------------------------+
    nil

    ; To expand a record's information, pass it's id
    user=> (rubydoc 3)
    +-------+----------------------------------------------------------------------------------------------------------------------------------+
    | field | value                                                                                                                            |
    +-------+----------------------------------------------------------------------------------------------------------------------------------+
    | :id   | 3                                                                                                                                |
    | :ruby | Kernel#require                                                                                                                   |
    | :clj  | clojure.core/require                                                                                                             |
    | :desc | They are mostly the same though the clojure version has useful :reload and :reload-all flags. Also the require file format is di |
    |       | fferent. For example, a ruby version of "reply/eval_state", has the clojure equivalent of "reply.eval-state".                    |
    +-------+----------------------------------------------------------------------------------------------------------------------------------+

    ; Pull up a ruby method by it's name
    user=> (rubydoc "system")
    +----------+-------------------------------------------------------------------------+
    | field    | value                                                                   |
    +----------+-------------------------------------------------------------------------+
    | :id      | 1                                                                       |
    | :ruby    | Kernel#system                                                           |
    | :clj     | clojure.java.shell/sh                                                   |
    | :similar | true                                                                    |
    | :desc    | sh executes commands but unlike system, stdout is captured as a string. |
    +----------+-------------------------------------------------------------------------+
    nil

    ; What clojure functions have 'con' in them
    (rubydoc "con" :clj)
    +-----+---------------+------------------------+-----------------------------------------------------------------------------------+
    | id  | ruby          | clj                    | desc                                                                              |
    +-----+---------------+------------------------+-----------------------------------------------------------------------------------+
    | 54  | Hash#key?     | clojure.core/contains? |                                                                                   |
    | 87  | Array#unshift | clojure.core/cons      | See also clojure.core/conj which does this for lists but with arguments reversed. |
    | 98  | Array#concat  | clojure.core/concat    | Clojure version can take multiple collections.                                    |
    | 101 | Array#<<      | clojure.core/conj      |                                                                                   |
    +-----+---------------+------------------------+-----------------------------------------------------------------------------------+

    ; Do any records have 'private' anywhere in them?
    user=> (rubydoc "private" :all)
    +----------+------------------------------------------------------------------------+
    | field    | value                                                                  |
    +----------+------------------------------------------------------------------------+
    | :id      | 11                                                                     |
    | :ruby    | Object#send                                                            |
    | :clj     | @#'namespace/meth                                                      |
    | :similar | true                                                                   |
    | :desc    | To call private methods as send can, place the deref-ed Var in the ... |
    +----------+------------------------------------------------------------------------+

## Contributing

If you have some ruby/clojure comparisons, please add them to [rubydoc's
database](https://github.com/cldwalker/rubydoc/blob/master/src/rubydoc/db.clj)!
Please add them to the end of file (to keep record ids consistent). I would
definitely love to see this become a community resource. While this project is
primarily focused on method/functions, I'm open to expanding this to constants,
global variables, classes and libraries.

[Some general guidelines](http://tagaholic.me/contributing.html)

Note: tests aren't needed for db contributions

## Contributors

Thanks to:
* edtsech

## Credits
Thanks to @relevance fridays for time to start this!

## Additional links
* https://gist.github.com/17283 - A nice list of Array and Enumerable ruby/clojure comparisons
