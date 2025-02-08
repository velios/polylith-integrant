(ns integrant.workload.protocol)

(defprotocol Workload
  (write-text [this text]))