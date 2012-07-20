# Description

A tool to make it easier for rubyists to speak clojure and vice versa. Compares versions >= ruby
1.9.3 and clojure 1.4.0.

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

    ; Pull up any ruby constructs that have IO in it
    user=> (rubydoc "IO")
    +----------------------------------------------+------------------------+----------------------------------------------------------------------------------------------------------------------------------+
    | ruby                                         | clj                    | desc
    |
    +----------------------------------------------+------------------------+----------------------------------------------------------------------------------------------------------------------------------+
    | IO.open                                      | clojure.core/with-open | with-open is more
    general as it takes multiple objects to read and close and is open to any java object that has
    .close defined. |
    | IO.read and Kernel#open from open-uri stdlib | clojure.core/slurp     | slurp is a generalize
    read, reading anything java.io.Reader can read which includes files and urls.
    |
    | IO.foreach                                   | line-seq               |
    |
    +----------------------------------------------+------------------------+----------------------------------------------------------------------------------------------------------------------------------+
    nil

    ;What's similar to Kernel#system?
    user=> (rubydoc "system")
    +----------+-------------------------------------------------------------------------+
    | key      | value                                                                   |
    +----------+-------------------------------------------------------------------------+
    | :ruby    | Kernel#system                                                           |
    | :clj     | clojure.java.shell/sh                                                   |
    | :similar | true                                                                    |
    | :desc    | sh executes commands but unlike system, stdout is captured as a string. |
    +----------+-------------------------------------------------------------------------+
    nil

## Contributing

If you have some ruby/clojure comparisons, please add them to [rubydoc's
database](https://github.com/cldwalker/rubydoc/blob/master/src/rubydoc/db.yml)! I would definitely
love to see this become a community resource. While this project is primarily focused on
method/functions, I'm open to expanding this to constants, global variables, classes and libraries.

[Some general guidelines](http://tagaholic.me/contributing.html)
Note: tests aren't needed for db contributions

## TODO
* Pick more comparisons up from https://gist.github.com/17283
* Finer-grained search capability i.e. only search desc, clj fields
