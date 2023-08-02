(defn linal-vector? [vector]
      (and (vector? vector) (every? number? vector)))

(defn equal-linal-vectors-size? [& vectors]
      (apply == (mapv count vectors)))

(defn linal-matrix? [matrix]
      (and (vector? matrix) (every? #(and (linal-vector? %) (equal-linal-vectors-size? (first matrix) %)) matrix)))

(defn equal-linal-matrices-size? [& matrices]
      (and (apply == (mapv #(count %) matrices)) (apply equal-linal-vectors-size? matrices)))

(defn linal-tensor? [& tensor]
      (or (every? number? tensor) (and (every? vector? tensor) (apply == (mapv count tensor)) (apply linal-tensor? tensor))))

(defn vector-operation [operation & vectors] {
                                              :pre [(every? linal-vector? vectors) (apply equal-linal-vectors-size? vectors)]
                                              :post [(linal-vector? %) (equal-linal-vectors-size? % (first vectors))]}
      (apply mapv operation vectors))

(def v+ (partial vector-operation +))

(def v- (partial vector-operation -))

(def v* (partial vector-operation *))

(def vd (partial vector-operation /))

(defn scalar [& vectors] {
                          :pre [(every? linal-vector? vectors) (apply equal-linal-vectors-size? vectors)]
                          :post [(number? %)]}
      (reduce + (apply v* vectors)))

(defn vect [& vectors] {
                        :pre [(every? linal-vector? vectors) (apply equal-linal-vectors-size? vectors) (== 3 (count (first vectors)))]
                        :post [(linal-vector? %) (== 3 (count %))]}
      (reduce (fn [vector1 vector2]
                  (vector (- (* (nth vector1 1) (nth vector2 2)) (* (nth vector1 2) (nth vector2 1)))
                          (- (* (nth vector1 2) (nth vector2 0)) (* (nth vector1 0) (nth vector2 2)))
                          (- (* (nth vector1 0) (nth vector2 1)) (* (nth vector1 1) (nth vector2 0)))))
              vectors))

(defn v*s [vector & scalars] {
                              :pre [(every? number? scalars) (linal-vector? vector)]
                              :post [(linal-vector? %) (equal-linal-vectors-size? % vector)]}
      (mapv #(* (reduce * scalars) %) vector))



(defn matrix-operation [operation & matrices] {
                                               :pre [(every? linal-matrix? matrices) (apply equal-linal-matrices-size? matrices)]
                                               :post [(linal-matrix? %) (equal-linal-matrices-size? (first matrices) %)]}
      (apply mapv operation matrices))

(def m+ (partial matrix-operation v+))

(def m- (partial matrix-operation v-))

(def m* (partial matrix-operation v*))

(def md (partial matrix-operation vd))

(defn m*s [matrix & scalars] {
                              :pre [(every? number? scalars) (linal-matrix? matrix)]
                              :post [(linal-matrix? %) (equal-linal-matrices-size? matrix %)]}
      (mapv #(v*s % (reduce * scalars)) matrix))

(defn m*v [matrix vector] {
                           :pre [(linal-vector? vector) (linal-matrix? matrix)]
                           :post [(linal-vector? %) (== (count %) (count matrix))]}
      (mapv #(scalar vector %) matrix))

(defn transpose [matrix] {
                          :pre [(linal-matrix? matrix)]
                          :post [(linal-matrix? %)]}
      (apply mapv vector matrix))

(defn m*m [& matrices] {
                        :pre [(every? linal-matrix? matrices)]
                        :post [(== (count %) (count (first matrices))) (== (count (first %)) (count (first (last matrices))))]}
      (reduce (fn [matrix1 matrix2] {
                                     :pre [(== (count (first matrix1)) (count matrix2))]}
                  (mapv
                    (fn [row]
                        (mapv (fn [column] (scalar row column))
                              (transpose matrix2)))
                    matrix1))
              matrices))


(defn tensor-operation [operation & tensors] {
                                              :pre [(or (every? number? tensors) (apply == (mapv count tensors)))]}
      (cond (every? number? tensors)
            (apply operation tensors)
            :else
            (apply mapv (partial tensor-operation operation) tensors)))



(def t+ (partial tensor-operation +))

(def t- (partial tensor-operation -))

(def t* (partial tensor-operation *))

(def td (partial tensor-operation /))
