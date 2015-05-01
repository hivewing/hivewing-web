(ns hivewing-web.controllers.workers
  (:require [hivewing-web.data.workers :as ws]
            [hivewing-web.data.worker-public-keys :as wpks]
  ))

(defn public-keys [pk]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC6NUfcIfToBISwY6ejUDCDRoqAAY7ndEPAgGfZL9tj1MqOqTpQiNTeQ19eL5tNQhCZwUH9IkqcGbl6z5E46+DPPoFstjnasZDC30/sH9FqF1E57XLf+3zkASsHykIcUXKPcqR0JfpI81u1mSnZQxe7hI3xKScYZl9lrfDy0vkDolBQnZxlo8a9rsRCj5pmdMuJx3b+Z1QY1fvly47LJGIpsKHGWSdgWp2GjVwSy5Py3L0acLKoZnAUtURvWt+hAk5VhNmpsNRgRUxO6uMp6H4k9CH21bBkFdgKGrtNYvP9PD814dz3T5FlO9UBahOeP8qVEpXX7wZhEI/7lzt3KsLr cfitzhugh@cfitz-server"
  })
