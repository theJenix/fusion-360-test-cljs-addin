(require 'cljs.build.api)

(cljs.build.api/watch "src"
                      {:output-to "test-cljs-addin.js"
                       :optimizations :none})
