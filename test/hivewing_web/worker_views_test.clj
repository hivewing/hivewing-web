(ns hivewing-web.worker-views-test
  (:require [clojure.test :refer :all]
            [hivewing-core.helpers :as helpers]
            [views.worker :as view]
            [hivewing-web.core :refer :all]))

(use-fixtures :each helpers/clean-database)

(comment
(view/split-config-name "task1.pear")
(view/split-config-name "task1.task2.task3.pear")
  )
(deftest test-split-config-name
  (is (= ["task1" "pear"] (view/split-config-name "task1.pear")))
  (is (= ["task1.task2.task3" "pear"] (view/split-config-name "task1.task2.task3.pear")))
  )
