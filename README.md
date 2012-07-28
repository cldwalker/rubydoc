# Description

A repl tool to help rubyists find clojure equivalents. This project
provides a rubydoc command to search the included db of ruby/clojure
comparisons. Compares versions >= ruby 1.9.3 and clojure 1.4.0.

[![Build Status](https://secure.travis-ci.org/cldwalker/rubydoc.png?branch=master)](http://travis-ci.org/cldwalker/rubydoc)

## Install

To have it available on all projects, add to your leiningen2's ~/lein/profiles.clj:

    {:user {:dependencies { rubydoc "0.1.0"}}}

To have it on an individual project, add to your project.clj:

    [rubydoc "0.1.0"]

## Usage

To use in a library:

    (use 'rubydoc.core)

Let's search for ruby equivalents in clojure:

    $ lein repl
    user=> (use 'rubydoc.core)
    nil

    ; Pull up clojure equivalents to Kernel methods
    user=> (rubydoc "Kernel")
    +--------------------------------------+--------------------------------------+---------+---------------------------------------------------------------------+
    | ruby                                 | clj                                  | similar | desc                                                                |
    +--------------------------------------+--------------------------------------+---------+---------------------------------------------------------------------+
    | Kernel#system                        | clojure.java.shell/sh                | true    | sh executes commands but unlike system, stdout is captured as a ... |
    | Kernel#exit                          | System/exit                          |         |                                                                     |
    | Kernel#require                       | clojure.core/require                 |         | They are mostly the same though the clojure version has useful :... |
    | Kernel#load                          | clojure.core/load-file               |         |                                                                     |
    | Kernel#puts                          | clojure.core/println                 |         |                                                                     |
    | Kernel#print                         | clojure.core/print                   |         |                                                                     |
    | Kernel#pp                            | clojure.pprint/pprint                |         | The ruby meth comes from requiring 'pp', a file in stdlib.          |
    | Kernel#sleep                         | Thread/sleep                         |         |                                                                     |
    | Kernel#at_exit                       | (.addShutdownHook (Runtime/getRun... | true    | Whereas at_exits are run in the reverse order defined, multiple ... |
    | Kernel#raise                         | throw                                |         |                                                                     |
    | IO.read and Kernel#open from open... | clojure.core/slurp                   |         | slurp is a generalize read, reading anything java.io.Reader can ... |
    +--------------------------------------+--------------------------------------+---------+---------------------------------------------------------------------
    nil

    ; What's similar to ruby's system?
    user=> (rubydoc "system")
    +----------+-------------------------------------------------------------------------+
    | field    | value                                                                   |
    +----------+-------------------------------------------------------------------------+
    | :ruby    | Kernel#system                                                           |
    | :clj     | clojure.java.shell/sh                                                   |
    | :similar | true                                                                    |
    | :desc    | sh executes commands but unlike system, stdout is captured as a string. |
    +----------+-------------------------------------------------------------------------+
    nil

    ; What clojure functions have 'con' in them
    (rubydoc "con" :clj)
    +---------------+-----------------------+-----------------------------------------------------------------------------------+
    | ruby          | clj                   | desc                                                                              |
    +---------------+-----------------------+-----------------------------------------------------------------------------------+
    | Hash#key?     | clojure.core/contains |                                                                                   |
    | Array#unshift | clojure.core/cons     | See also clojure.core/conj which does this for lists but with arguments reversed. |
    | Array#concat  | clojure.core/concat   | Clojure version can take multiple collections.                                    |
    +---------------+-----------------------+-----------------------------------------------------------------------------------+

    ; Do any records have 'private' anywhere in them?
    user=> (rubydoc "private" :all)
    +----------+------------------------------------------------------------------------+
    | field    | value                                                                  |
    +----------+------------------------------------------------------------------------+
    | :ruby    | Object#send                                                            |
    | :clj     | @#'namespace/meth                                                      |
    | :similar | true                                                                   |
    | :desc    | To call private methods as send can, place the deref-ed Var in the ... |
    +----------+------------------------------------------------------------------------+

## Contributing

If you have some ruby/clojure comparisons, please add them to [rubydoc's
database](https://github.com/cldwalker/rubydoc/blob/master/src/rubydoc/db.yml)! I would definitely
love to see this become a community resource. While this project is primarily focused on
method/functions, I'm open to expanding this to constants, global variables, classes and libraries.

[Some general guidelines](http://tagaholic.me/contributing.html)
Note: tests aren't needed for db contributions

## Credits
Thanks to @relevance fridays!

## TODO
* Pick more comparisons up from https://gist.github.com/17283
