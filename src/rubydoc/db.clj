[{:ruby "Open3.capture3", :clj "clojure.java.shell/sh", :ruby-lib "open3"}
 {:ruby "Kernel#system",
  :clj "clojure.java.shell/sh",
  :similar true,
  :desc
  "sh executes commands but unlike system, stdout is captured as a string."}
 {:ruby "Kernel#exit", :clj "System/exit"}
 {:ruby "Kernel#require",
  :clj "clojure.core/require",
  :desc
  "They are mostly the same though the clojure version has useful :reload and :reload-all flags. Also the require file format is different. For example, a ruby version of \"reply/eval_state\", has the clojure equivalent of \"reply.eval-state\"."}
 {:ruby "Kernel#load", :clj "clojure.core/load-file"}
 {:ruby "__FILE__",
  :clj "*file*",
  :type "constant"
  :desc
  "The ruby value is relative to the current directory and can just be expanded. The clojure value is relative to a classpath directory (not the current directory) and needs to be expanded with: (ClassLoader/getSystemResource *file*)."}
 {:ruby "Kernel#puts", :clj "clojure.core/println"}
 {:ruby "Kernel#print", :clj "clojure.core/print"}
 {:ruby "Kernel#pp",
  :clj "clojure.pprint/pprint",
  :ruby-lib "pp"}
 {:ruby "Kernel#sleep", :clj "Thread/sleep"}
 {:ruby "Kernel#at_exit",
  :clj
  "(.addShutdownHook (Runtime/getRuntime) fn-wrapped-in-a-thread)",
  :similar true,
  :type "code",
  :desc
  "Whereas at_exits are run in the reverse order defined, multiple ShutdownHooks are run concurrently in an unspecified order."}
 {:ruby "Object#send",
  :clj "@#'namespace/meth",
  :similar true,
  :type "code"
  :desc
  "To call private methods as send can, place the deref-ed Var in the function place i.e. (@#'meth arg1). To dynamically call a ruby variable as send can, convert your clojure symbol to a Var and deref. For example, if you had the function \"println\" as a string, convert the string into a function and execute it: (@(resolve (symbol \"println\")) \"WOOT\")."}
 {:ruby "Object#tap",
  :clj "clojure.core/doto",
  :desc
  "Both tap and doto take an object, act on it with a block/function and then pass on the original object. To compare, given this ruby example \"10.tap {|n| puts n } + 5\", the clojure equivalent is \"(-> 10 (doto println) (+ 5))\"."}
 {:ruby "Class.new",
  :clj "new",
  :type "keyword"
  :desc
  "Given a ruby example \"Klass.new(arg)\", the clojure equivalent is \"(new Klass arg) or (Klass. arg)\"."}
 {:ruby "Object#instance_of?", :clj "clojure.core/instance?"}
 {:ruby "Kernel#raise", :clj "throw", :type "keyword"}
 {:ruby "Object#respond_to?",
  :clj "clojure.core/ns-resolve",
  :similar true,
  :desc
  "Whereas respond_to? indicates if an object can call a method, ns-resolve indicates if a namespace can use a Var/Class. ns-resolve is different in that it doesn't return a boolean or report only about functions. See clojure.core/resolve for assuming current namespace."}
 {:ruby "Symbol#to_s",
  :clj "clojure.core/name",
  :desc
  "Use when wanting to stringify a clojure keyword aka ruby symbol."}
 {:ruby "Exception#backtrace",
  :clj "clojure.repl/pst",
  :similar true,
  :desc
  "If in a repl, pst will print the given or last exception's stacktrace. If in a program, use (.getStackTrace Exception). To get a useful stacktrace array, more work is required."}
 {:ruby "Exception#message", :clj "(.getMessage exception)", :type "code"}
 {:ruby "Module#include",
  :clj "clojure.core/refer",
  :desc
  "Whereas include brings in methods into a module via a class hierarchy, refer brings in vars via mappings. refer has finer-grained control allowing you to rename or selectively include/exclude some vars. See clojure.core/import for importing Java classes into a namespace (no ruby equivalent)."}
 {:ruby "Module#ancestors",
  :clj "clojure.core/supers",
  :desc
  "Note that supers is for java classes. See clojure.core/ancestors for tracing tag or class ancestry."}
 {:ruby "IO.open",
  :clj "clojure.core/with-open",
  :desc
  "with-open is more general as it takes multiple objects to read and close and is open to any java object that has .close defined. See clojure.core/spit for a quick way to write to a file."}
 {:ruby "IO.read" ,
  :clj "clojure.core/slurp",
  :desc
  "slurp is a generalize read, reading anything java.io.Reader can read which includes files and urls."}
 {:ruby "Kernel#open" :clj "clojure.core/slurp" :ruby-lib "open-uri" }
 {:ruby "IO.foreach", :clj "clojure.core/line-seq"}
 {:ruby "File.directory?",
  :clj "(.isDirectory (clojure.java.io/file some_file))"
  :type "code"}
 {:ruby "File.exists?",
  :clj "(.exists (clojure.java.io/file some_file))"
  :type "code"}
 {:ruby "File.file?",
  :clj "(.isFile (clojure.java.io/file some_file))"
  :type "code"}
 {:ruby "Dir.glob",
  :clj
  "(defn glob [dir regex] (->> (clojure.java.io/file dir) (file-seq) (map str) (filter #(re-find regex %))))",
  :similar true,
  :type "code"
  :desc
  "This is a simpler version of glob that fully descends a directory and matches files for the given regex."}
 {:ruby "ENV",
  :clj "System/getenv",
  :type "constant",
  :desc "To get specific env values, pass the env name to getenv."}
 {:ruby "RbConfig::CONFIG",
  :clj "System/getProperties",
  :type "constant",
  :desc "A comprehensive map of system and language information."}
 {:ruby "Dir.home", :clj "(System/getProperty \"user.home\")" :type "code"}
 {:ruby ["$:" "$LOAD_PATH"],
  :clj "(seq (.getURLs (ClassLoader/getSystemClassLoader)))",
  :type "variable",
  :desc
  "These are the loaded loadpaths/classpaths available in your runtime. While in ruby you can just add to this array, clojure will be disallowing this soon - see deprecated clojure.core/add-classpath."}
 {:ruby "$RUBYLIB",
  :clj "$CLASSPATH",
  :type "variable"
  :desc
  "These environment variables can be manipulated before invoking a program to modify its loadpath/classpath."}
 {:ruby ["STDIN" "$stdin"], :clj "*in*"
  :type "constant"
  :desc "*in* is thread local and used by clojure while System/in is global and used by java internals."}
 {:ruby ["STDOUT" "$stdout"], :clj "*out*"
  :type "constant"
  :desc "*out* is thread local and used by clojure while System/out is global and used by java internals."}
 {:ruby ["STDERR" "$stderr"], :clj "*err*"
  :type "constant"
  :desc "*err* is thread local and used by clojure while System/err is global and used by java internals."}
 {:ruby "_",
  :clj "*1",
  :type "variable",
  :desc
  "These give back the returned value from the last statement in a repl. Clojure also has *2 and *3 for 2nd and 3rd to last values."}
 {:ruby "$!",
  :clj "*e",
  :type "variable"
  :similar true,
  :desc
  "While ruby's is available to any program, the clojure one is only available in the repl."}
 {:ruby "String#to_i", :clj "(Integer. \"string\")"
  :type "code"
  :desc "For clojure, see also Long/parseLong."}
 {:ruby "Integer#times",
  :clj "clojure.core/dotimes",
  :desc
  "Given the ruby example \"5.times {|n| }\", the clojure equivalent would be \"(dotimes [n 5] )\". Also see clojure.core/repeatedly."}
 {:ruby "Array.new",
  :clj "clojure.core/repeat",
  :desc
  "Given the ruby example which generates a vector of 5 3's - \"Array.new(5, 3)\", the clojure equivalent would be \"(repeat 5 3)\"."}
 {:ruby "Benchmark.measure",
  :clj "clojure.core/time",
  :ruby-lib "benchmark",
  :desc "Prints time taken for given code to run."}
 {:ruby "Struct.new",
  :clj "clojure.core/defstruct",
  :desc
  "Given this ruby example \"Person = Struct.new(:name, :age); Person.new('Bo', 8)\", the clojure equivalent would be '(defstruct person :name :age) (struct person \"Bo\" 8)'."}
 {:ruby "ActiveSupport::Memoizable#memoize",
  :clj "clojure.core/memoize",
  :ruby-lib "activesupport",
  :desc "Memoizes a function based on arguments."}
 {:ruby
  "ObjectSpace.each_object(Module).to_a - ObjectSpace.each_object(Class).to_a",
  :clj "clojure.core/all-ns",
  :type "code"
  :similar true,
  :desc
  "If you think of ruby's modules as clojure namespaces, these two are equivalent. For the ruby example, Class objects were subtracted from Module objects since a Class is a Module."}
 {:ruby "Proc#curry",
  :clj "clojure.core/partial",
  :desc
  "Generates a function given a function. Given the ruby example \"proc {|x,y,z| p x,y,z }.curry[2][1,3]\", the clojure equivalent is \"((partial #(prn %1 %2 %3) 2) 1 3)\". Note that the ruby version doesn't let you populate default args until you call #[] while the clojure one requires at least arg to populate the new function."}
 {:ruby
  ["Proc#source_location" "Method#source_location" "UnboundMethod#source_location"],
  :clj "clojure.repl/source",
  :similar true,
  :desc
  "Whereas the clojure version prints the given function for you, the ruby one only tells you where it starts."}
 {:ruby
  ["Proc#parameters" "Method#parameters" "UnboundMethod#parameters"],
  :clj "(first ((meta #'fn-name) :arglists))",
  :type "code"
  :desc
  "Returns arguments for given method/function. The ruby version returns an array of arrays with each array pair indicating if the argument is required, optional or a splatted arg - respectively :req, :opt, :rest."}
 {:ruby "[1,2,3].slice(1..-1)", :clj "clojure.core/rest", :type "code"}
 {:ruby ["Hash#[]" "Hash#fetch"],
  :clj "clojure.core/get",
  :desc
  "Like fetch, get can take a default value if the key doesn't exist for the map/hash. Unlike fetch, get doesn't fail if a given key isn't found."}
 {:ruby "Hash#[]=",
  :clj "clojure.core/assoc",
  :desc
  "assoc can take more than 2 arguments to set additional key/pairs."}
 {:ruby "Hash#merge",
  :clj "clojure.core/merge",
  :desc
  "The clojure version can merge multiple maps. The ruby version takes a block to resolve merge conflicts while clojure requires clojure.core/merge-with to do that."}
 {:ruby "Enumerable#count", :clj "clojure.core/count"}
 {:ruby "Hash#key?", :clj "clojure.core/contains?"}
 {:ruby "Hash#delete",
  :clj "clojure.core/dissoc",
  :desc "dissoc can also delete multiple keys."}
 {:ruby ["Enumerable#reduce" "Enumerable#inject"],
  :clj "clojure.core/reduce"}
 {:ruby ["Enumerable#map" "Enumerable#collect"],
  :clj "clojure.core/map",
  :desc
  "The clojure version can take multiple collections and pass as them additional args while the ruby version can only take one array."}
 {:ruby "Hash.[]",
  :clj "clojure.core/hash-map",
  :desc
  "Used often to convert a vec/array to a map/hash. For the ruby example of \"Hash[*[1,2,3,4]]\", the clojure equivalent is \"(apply hash-map [1 2 3 4])\"."}
 {:ruby "Hash#invert", :clj "clojure.set/map-invert"}
 {:ruby "Array#uniq", :clj "clojure.core/distinct"}
 {:ruby "String#slice(regexp)",
  :clj "clojure.core/re-find",
  :desc
  "Returns a string if there is match or nil. For the given ruby version \"okdok\"[/ok/], the clojure equivalent is (re-find #\"ok\" \"okdok\")."}
 {:ruby "String#split",
  :clj "clojure.string/split",
  :desc
  "The ruby version can split on string or regexp while the clojure version only does regexp."}
 {:ruby ["String#+" "String#<<"],
  :clj "clojure.core/str",
  :desc
  "Clojure version handles multiple arguments. While ruby version doesnt offer this, it does have interpolation \"#{one} and #{two}\" vs (str one \" and \" two)."}
 {:ruby ["String#%" "Kernel#sprintf"], :clj "clojure.core/format"}
 {:ruby "String#sub", :clj "clojure.string/replace-first"}
 {:ruby "String#gsub", :clj "clojure.string/replace"}
 {:ruby "String#slice(offset, count)",
  :clj "(.substring \"string\" offset count)",
  :type "code"}
 {:ruby "String#scan",
  :clj "clojure.core/re-seq",
  :desc "Returns a list of all matches a regexp has against a string."}
 {:ruby "Array#join",
  :clj "clojure.string/join",
  :desc "Join an array/vec by a given string."}
 {:ruby "String#upcase", :clj "clojure.string/upper-case"}
 {:ruby "String#downcase", :clj "clojure.string/lower-case"}
 {:ruby "String#lstrip", :clj "clojure.string/triml"}
 {:ruby "String#strip", :clj "clojure.string/trim"}
 {:ruby "String#rstrip", :clj "clojure.string/trimr"}
 {:ruby "String#split", :clj "clojure.string/split-lines"}
 {:ruby "String#reverse", :clj "clojure.string/reverse"}
 {:ruby ["Array#include?" "Enumerable#include?"],
  :clj "(some #{element} [1 2 element])",
  :similar true,
  :type "code"
  :desc
  "Given a ruby example of \"[1,2,3].include?(1)\", the clojure equivalent is \"(some #{1} [1 2 3])\". Note the clojure version doesn't return a boolean but the element if found."}
 {:ruby "Object#blank?",
  :ruby-lib "activesupport",
  :clj "clojure.string/blank?"}
 {:ruby ["Enumerable#zip" "Array#zip"],
  :clj "clojure.core/interleave",
  :similar true,
  :desc
  "While the ruby version returns an array of zipped arrays, the clojure version returns a flattened array."}
 {:ruby "Array#[]",
  :clj "clojure.core/get",
  :desc
  "See clojure.core/nth for potential speed up but lookout as non-existent indices throw an error."}
 {:ruby ["Enumerable#first", "Array#shift"] :clj "clojure.core/first"}
 {:ruby "Enumerable#take", :clj "clojure.core/take"}
 {:ruby "Enumerable#drop", :clj "clojure.core/drop"}
 {:ruby "Enumerable#take-while", :clj "clojure.core/take-while"}
 {:ruby "Enumerable#drop-while", :clj "clojure.core/drop-while"}
 {:ruby "Array#last", :clj "clojure.core/last"}
 {:ruby "Array#unshift",
  :clj "clojure.core/cons",
  :desc
  "See also clojure.core/conj which does this for lists but with arguments reversed."}
 {:ruby ["Enumerable#find_all" "Enumerable#select"],
  :clj "clojure.core/filter"}
 {:ruby "Enumerable#delete_if" :clj "clojure.core/filter"
  :desc "delete_if is the same as filter since they both return values filter by the function/block. delete_if has side effects which aren't possible in the clojure version."}
 {:ruby "Enumerable#sort", :clj "clojure.core/sort"}
 {:ruby "Enumerable#sort_by", :clj "clojure.core/sort-by"}
 {:ruby "Enumerable#reject", :clj "clojure.core/remove"}
 {:ruby "[].map.with_index",
  :clj "clojure.core/map-indexed",
  :type "code"
  :desc
  "The clojure version takes the index and element while the ruby one has the arguments reversed."}
 {:ruby "Enumerable#all?", :clj "clojure.core/every?"}
 {:ruby "Enumerable#each", :clj "clojure.core/doseq"}
 {:ruby "Enumerable#any?",
  :clj "(some true? [1 2 3])",
  :type "code"
  :desc
  "ruby version returns a boolean while the clojure one does not."}
 {:ruby "BasicObject#equal?",
  :clj "clojure.core/identical?",
  :desc "Determines if objects are exactly the same."}
 {:ruby "BasicObject#==", :clj "clojure.core/="}
 {:ruby "Array#concat",
  :clj "clojure.core/concat",
  :desc "Clojure version can take multiple collections. See also clojure.core/into which adds collections but in different orders depending on the data structure."}
 {:ruby ["Array#flatten" "Hash#flatten"],
  :clj "clojure.core/flatten",
  :desc "Ruby version can flatten to a given level."}
 {:ruby "MiniTest::Assertions#assert", :ruby-lib "minitest/unit",
  :clj "clojure.core/assert"}
 {:ruby "Array#<<", :clj "clojure.core/conj"
  :desc "conj adds to a collection in the most efficient way possible for a data structure. For lists this means prepending and for vectors it means appending."}
 {:ruby "Regexp.new",
  :clj "clojure.core/re-pattern",
  :desc
  "Since clojure regexps can't interpolate symbols as in ruby, use this to generate a string that converts to a regexp."}
 {:ruby "Enumerable#group_by", :clj "clojure.core/group-by"}
 {:ruby "URI.encode", :ruby-lib "uri",
  :clj "java.net.URLEncoder/encode"}
 {:ruby "URI.decode", :ruby-lib "uri",
  :clj "java.net.URLDecoder/decode"}
 {:ruby "Kernel#warn",
  :clj "(binding [*out* *err*] (println \"FAILED!\"))",
  :type "code"}
 {:ruby ["File.delete" "FileUtils.rm"], :ruby-lib "fileutils",
  :clj "clojure.java.io/delete-file"}
 {:ruby "Dir.entries",
  :clj "(-> (clojure.java.io/file \"DIRECTORY\") .list vec)",
  :type "code"}
 {:ruby "Tempfile.new", :ruby-lib "tempfile",
  :clj "java.io.File/createTempFile"}
 {:ruby "File::Separator", :clj "java.io.File/separator" :type "constant"}
 {:ruby "RUBY_VERSION" :clj "clojure.core/clojure-version" :type "constant"}
 {:ruby "irb stdlib" :clj "clojure.main/repl"
  :type "lib"
  :desc "Clojure's is smaller but also more extendable."}
 {:ruby "=begin and =end" :clj "clojure.core/comment"
  :type "keyword"
  :desc "Multiline comment strings. Clojure can also comment any sexp by placing #_ in front of it."}
 {:ruby "Array#compact" :clj "(remove nil? [1 2 nil 3]) OR (keep identity [1 2 nil 3])", :type "code"}
 {:ruby "Enumerable#shuffle" :clj "clojure.core/shuffle"}
 {:ruby ["Enumerable#cycle", "Array#*"] :clj "clojure.core/cycle"}
 {:ruby "Enumerable#each_with_object" :clj "clojure.core/reduce"
  :similar true
  :desc "each_with_object is a specialized reduce that doesn't care what the return value of the reducing function is. Given the ruby example '[:a, :b, :c].each_with_object({}) {|a,b| b[a] = 1 }', the equivalent clojure '(reduce #(assoc %1 %2 1) {} [:a :b :c])'."}
 {:ruby "Enumerable#flat_map" :clj "(comp flatten map)"
  :type "code"
  :desc "Given a ruby example of '[1,2,3].flat_map {|e| [1, e] }', the clojure equivalent is '((comp flatten map) #(vec [1, %]) [1 2 3])'."}
 {:ruby "Set#-" :clj "clojure.set/difference" :ruby-lib "set"}
 {:ruby "Set#superset?" :clj "clojure.set/superset?" :ruby-lib "set"}
 {:ruby ["Set#union" "Set#|"] :clj "clojure.set/union" :ruby-lib "set"}
 {:ruby ["Set#intersection" "Set#&"] :clj "clojure.set/intersection" :ruby-lib "set"}
 {:ruby "Set#subset?" :clj "clojure.set/subset?" :ruby-lib "set"}
 {:ruby ["Set#add" "Set#+"] :clj "clojure.core/concat" :ruby-lib "set"}
 {:ruby "Set#classify" :clj "clojure.set/index"
  :desc "This is basically a group-by fn for sets. The ruby version is more generalized as it groups elements by the return val of its block while the clojure version groups by specified key/val pairs." :ruby-lib "set"}
 {:ruby ["Range.new" "Integer#step"] :clj "clojure.core/range"}
 {:ruby "Kernel#trap" :clj "clojure.repl/set-break-handler!"
  :similar true
  :desc "The clojure version only handles the INT signal. 'source clojure.repl/set-break-handler!' to see how to trap other signals."}
 {:ruby "Hash#keys" :clj "clojure.core/keys"}
 {:ruby "Hash#values" :clj "clojure.core/vals"}
 {:ruby "Hash#values_at" :clj "clojure.core/juxt"
  :similar true
  :desc "Given a ruby example of '{a: 1, b:2}.values_at(:a, :b)', the clojure equivalent is '((juxt :a :b) {:a 1 :b 2})'."}
 {:ruby ["Enumerable#to_a" "Kernel#Array"] :clj "#(if-not (or (nil? %) (vector? %)) [%] (vec %))"
  :type "code"}
 {:ruby "Enumerable#none?" :clj "clojure.core/not-any?"}
 {:ruby "Enumerable#one?" :clj "(comp #(= 1 %) count filter)" :type "code"}
 {:ruby ["Enumerable#min" "Enumerable#min_by"] :clj "clojure.core/min"
  :desc "See clojure.core/min-key to get the block functionality that min has."}
 {:ruby ["Enumerable#max" "Enumerable#max_by"] :clj "clojure.core/max"
  :desc "See clojure.core/max-key to get the block functionality that max has."}
 {:ruby "Enumerable#each_slice" :clj "clojure.core/partition-all"
  :desc "Given the ruby example '(0..4).each_slice(2).to_a', the clojure equivalent is '(partition-all 2 (range 0 5))'."}
 {:ruby "Enumerable#each_cons" :clj "clojure.core/partition"
  :desc "Clojure version is more general as it handles steps and padding. Given the ruby example '(0..4).each_cons(2).to_a', the clojure equivalent is '(partition 2 1 (range 0 5))'."}
 {:ruby "Enumerable#chunk" :clj "clojure.core/partition-by"
  :similar true
  :desc "While the clojure version does return a collections split by each time the return value of a function changes, it doesn't also return that return value or have the additional configurability that the ruby version has. Given the ruby version '[1,3,2].chunk {|n| n.even? }.to_a.map(&:second)', the clojure equivalent is '(partition-by even? [1 3 2])'."}
 {:ruby "Time.now" :clj "(java.util.Date.)"
  :type "code"
  :desc "See also System/nanoTime or System/currentTimeMillis."}
 {:ruby ["Set.new", "Enumerable#to_set"] :clj "clojure.core/set" :ruby-lib "set"}
 {:ruby "Set#member?" :clj "(#{1 2 3} 1)" :ruby-lib "set"
  :type "code"
  :desc "The ruby version returns true/false while the clojure version returns the member if it exists in the set."}
 {:ruby "Kernel#rand" :clj "clojure.core/rand",
  :desc "ruby version acts like rand by default. However if given an integer it acts like clojure.core/rand-int."}
 {:ruby "Array#sample" :clj "clojure.core/rand-nth"}
 {:ruby ["Enumerable#reverse_each" "Array#reverse"] :clj "clojure.core/reverse",
  :desc "See clojure.core/rseq for a lazy version."}
 {:ruby "Array#&" :clj "clojure.set/intersection"}
 {:ruby "Array#-" :clj "clojure.set/difference"}
 {:ruby ["Enumerable#<=>" "Comparable#<=>"] :clj "clojure.core/compare" :desc "While the ruby modules don't actually implement the spacheship method, they are the reasons why several ruby core classes implement them i.e. Object, Array, Hash, Module, String."}
 {:ruby ["Array#empty?" "Hash#empty?" "String#empty?"] :clj "clojure.core/empty?"}
 {:ruby "Object#hash" :clj "clojure.core/hash"}
 {:ruby "Array#slice" :clj "clojure.core/subvec"
  :desc "While slice's second argument specifies length of the sliced data structure, subvec's second argument specifies the index for the ending element."}
 {:ruby "Array#|" :clj "clojure.set/union"}
 {:ruby "Hash#slice" :ruby-lib "activesupport" :clj "clojure.core/select-keys"}
 {:ruby "Hash#except" :ruby-lib "activesupport" :clj "clojure.core/dissoc"}
 {:ruby "File.dirname" :clj "(.getParent (clojure.java.io/file some_file))" :type "code"}
 {:ruby "FileUtils.mkdir_p" :clj "clojure.java.io/make-parents", :ruby-lib "fileutils"}
 {:ruby "FileUtils.cp" :clj "org.apache.commons.io.FileUtils/copyFile" :ruby-lib "fileutils"
  :desc "The clojure version require files to be java.io.File instances."}
 {:ruby "FileUtils.cp_r" :clj "org.apache.commons.io.FileUtils/copyDirectoryToDirectory" :ruby-lib "fileutils"
  :desc "The clojure version require files to be java.io.File instances."}
 {:ruby "Date.parse" :clj "#inst \"2012-12-31\""
  :type "code"
  :desc "A more featureful clojure parser date is available via SimpleDateFormat. For example: (fn [string] (.parse (java.text.SimpleDateFormat. \"yyyy-MM-dd\") string))."}
 {:ruby ["Array#index" "Array#find_index"] :clj "(.indexOf [])"
  :type "code"
  :desc "Ruby version can take a block and returns nil if not element found. Clojure version returns -1 if element not found."}
 {:ruby "Object#present?" :ruby-lib "activesupport" :clj "clojure.core/seq"
  :desc "seq is meant for more than just checking presence and returns truish/nil vs. true/false from the ruby version."}
 {:ruby "Float#ceil" :clj "Math/ceil"}
 {:ruby "Float#floor" :clj "Math/floor"}
 {:ruby "Fixnum#abs" :clj "Math/abs"}
 {:ruby "Float#round" :clj "Math/round"}
 {:ruby "Fixnum#**" :clj "Math/pow"
  :desc "Raise a number to the power of another number."}
 {:ruby ["Fixnum#%" "Fixnum#modulo"] :clj "clojure.core/mod"}
 {:ruby "Fixnum#/" :clj "clojure.core/quot"}
 {:ruby "Time#strftime" :clj "(.format (java.text.SimpleDateFormat. \"MMM d, yyyy\") date)"
  :type "code"}
 {:ruby "String#to_f" :clj "java.lang.Float/parseFloat"}
 {:ruby "case" :clj "clojure.core/case"
  :type "keyword"
  :desc "See also clojure.core/cond and clojure.core/condp."}
 {:ruby "if" :clj "if" :type "keyword"}
 {:ruby "if (x = some_value)" :clj "(if-let [x (some_value)])" :type "code"}
 {:ruby "unless" :clj "if-not" :type "keyword"}
 {:ruby "def" :clj "defn" :type "keyword"}
 {:ruby "Module#private" :clj "clojure.core/defn-"
  :similar true
  :desc "defn- makes a private function while private only sets method(s) to private."}
 {:ruby "while" :clj "clojure.core/while" :type "keyword"}
 {:ruby "rescue" :clj "catch" :type "keyword"}
 {:ruby "ensure" :clj "finally" :type "keyword"}
 {:ruby "begin" :clj "try" :type "keyword"}
 {:ruby ["Kernel#lambda", "Proc.new"] :clj "clojure.core/fn"
  :desc "A shorthand for making anonymous functions is #(prn %)."}
 {:ruby "$stdin.gets" :clj "clojure.core/read-line" :type "code"
  :desc "The ruby version has a newline at the end of the string while the clojure one doesn't."}
 {:ruby "String#include?" :clj "(.contains str substr)" :type "code"}
 {:ruby "StringIO.new" :clj "new java.io.StringWriter" :type "code"}
 {:ruby "Float#round(2)" :clj "(java.lang.Float/parseFloat (format \"%.2f\" 0.012345))" :type "code"
  :desc "Rounding to a specific float precision."}
 {:ruby "String#start_with?" :clj "java.lang.String/startsWith"}
 {:ruby "String#end_with?" :clj "java.lang.String/endsWidth"}
 {:ruby "Dir.pwd" :clj "(.getCanonicalPath (java.io.File. "."))" :type "code"}
 {:ruby "Enumerable#find" :clj "(some #(and (predicate-fn %) %) items)" :type "code"
  :desc "In the clojure example, predicate-fn is the same as the block that would be passed to the ruby example."}
 {:ruby "Object#try" :ruby-lib "activesupport" :clj "clojure.core/some->"
  :desc "some-> has the same idea as try - keep passing on the result of the previous call unless nil which terminates it."}]
