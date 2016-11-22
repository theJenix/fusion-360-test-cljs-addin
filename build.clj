(require 'cljs.build.api)

(cljs.build.api/build "src" {:output-to "test-cljs-addin.js" :output-dir "out" :optimizations :simple :source-map "test-cljs-addin.js.map" })
