(require 'cljs.build.api)

(cljs.build.api/build "src" {:output-to "test-cljs-addin.js" :optimizations :simple})
