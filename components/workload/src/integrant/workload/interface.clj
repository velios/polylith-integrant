(ns integrant.workload.interface
  (:require [clojure.tools.logging :as log]))

(defn make-some-work
  [some-work-fn]
  (println "Some work from prod workload")
  (log/info "Some work from prod workload")
  (some-work-fn))