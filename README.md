         (                    )
         )\ )      *   )   ( /(
      ( (()/(   )` )  /(   )\())
      )\ /(_)) /( ( )(_)) ((_)\
     ((_)_)) )(_))_(_()))\ _((_)
       !| _ ((_)_|_   _((_) || |
       ||  _/ _` | | |/ _ \ __ |
     _/ |_| \__,_| |_|\___/_||_|
    |__/

jPaToH supplies Java bindings for [PaToH, Partitioning Tools for Hypergraph](http://bmi.osu.edu/~umit/software.html). To be able to compile the package you will need a recent JDK that is compatible with [JNA](https://jna.dev.java.net/). Steps to follow are presented below.

1. Issue `ant jar` command to build the `jpatoh.jar` file.

        ~$ cd jpatoh
        ~/jpatoh$ ant jar

2. Run below command to see if everything works properly.

        ~/jpatoh$ LD_LIBRARY_PATH=/path/to/libpatoh.so  \
                  java                                  \
                  -cp build/jar/jpatoh.jar:lib/jna.jar  \
                  tr.edu.bilkent.cs.patoh.examples.Test \
                  /patoh/to/some/patoh/hypergraph/file

3. Now you're wondering the magic, don't you? See `Test.java` under `src/tr/edu/bilkent/cs/patoh/examples` directory. (To see the full list of exported functions, consult to the javadoc specification of `HyperGraph` class in `doc` directory.)

Comments, bug reports, etc. are welcome.
