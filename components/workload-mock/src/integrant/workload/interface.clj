(ns integrant.workload.interface
  (:require [clojure.tools.logging :as log]))

(defn make-some-work
  [some-work-fn]
  (log/info "Some work from mock workload")
  (some-work-fn))