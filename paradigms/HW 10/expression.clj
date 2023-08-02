(defn constant [value]
    (fn [args]
        (identity value)))

(defn variable [name]
    (fn [args]
        (get args name)))

(defn operation-factory [operation]
    (fn [& operands]
        (fn [args]
            (apply operation (mapv #(% args) operands)))))

(defn divide-evaluator ([denominator] (/ 1.0 denominator))
    ([numerator & denominators] (/ numerator (double (apply * denominators)))))

(def add (operation-factory +))
(def subtract (operation-factory -))
(def multiply (operation-factory *))
(def divide (operation-factory divide-evaluator))
(def negate subtract)

(defn sumexp-evaluator [& operands]
    (apply + (mapv #(Math/exp %) operands)))

(def sumexp (operation-factory sumexp-evaluator))
(def lse (operation-factory (fn [& operands]
                                (Math/log (apply sumexp-evaluator operands)))))

(def functional-operations {'+ add
                            '- subtract
                            '* multiply
                            '/ divide
                            'negate negate
                            'sumexp sumexp
                            'lse lse})

(defn parse [operations const-impl variable-impl expression]
    (cond
        (symbol? expression) (variable-impl (name expression))
        (number? expression) (const-impl expression)
        (list? expression) (apply (operations (first expression)) (mapv (partial parse operations const-impl variable-impl) (rest expression)))))

(defn parseFunction [expression]
    (parse functional-operations constant variable (read-string expression)))



(definterface MyExpression
    (^Number evaluate [args])
    (^String toString [])
    (^user.MyExpression diff [diff-variable]))

(declare ZERO)

(deftype MakeConstant [value]
    MyExpression
    (evaluate [this args] (.-value this))
    (toString [this] (str (.-value this)))
    (diff [this diff-variable] ZERO))

(defn Constant [value] (MakeConstant. value))
(def ZERO (Constant 0))
(def ONE (Constant 1))
(def TWO (Constant 2))

(deftype MakeVariable [name]
    MyExpression
    (evaluate [this args] (get args (.-name this)))
    (toString [this] (str (.-name this)))
    (diff [this diff-variable] (if (= diff-variable (str (.-name this)))
                                   ONE
                                   ZERO)))

(declare Add Subtract Multiply Divide Negate Meansq RMS)

(defmacro operationFactory [name operation-symbol evaluator diff]
    (let [methodName# (symbol (str "Make" name))]
        `(do
             (deftype ~methodName# [operands#]
                 MyExpression
                 (evaluate [this# args#] (apply ~evaluator (mapv #(.evaluate % args#) operands#)))
                 (toString [this#] (str "(" ~operation-symbol " " (clojure.string/join " " (mapv #(.toString %) operands#)) ")"))
                 (diff [this# diff-variable#] (~diff operands# diff-variable#)))
             (def ~name #(new ~methodName# %&)))))



(defn Variable [name] (MakeVariable. name))



(operationFactory
    Add
    "+"
    +
    (fn [operands diff-variable]
        (apply Add (mapv #(.diff % diff-variable) operands))))

(operationFactory
    Subtract
    "-"
    -
    (fn [operands diff-variable]
        (apply Subtract (mapv #(.diff % diff-variable) operands))))

(operationFactory
    Multiply
    "*"
    *
    (fn [operands diff-variable]
        (if (= (count operands) 1)
            (.diff (first operands) diff-variable)
            (Add
                (Multiply
                    (.diff (first operands) diff-variable)
                    (apply Multiply (rest operands)))
                (Multiply
                    (first operands)
                    (.diff (apply Multiply (rest operands)) diff-variable))))))
(operationFactory
    Divide
    "/"
    divide-evaluator
    (fn [operands diff-variable]
        (if (= (count operands) 1)
            (Negate
                (Divide
                    (.diff (first operands) diff-variable)
                    (Multiply
                        (first operands)
                        (first operands))))
            (Divide
                (Subtract
                    (Multiply (.diff (first operands) diff-variable) (apply Multiply (rest operands)))
                    (Multiply (first operands) (.diff (apply Multiply (rest operands)) diff-variable)))
                (Multiply
                    (apply Multiply (rest operands))
                    (apply Multiply (rest operands)))))))

(operationFactory
    Negate
    "negate"
    -
    (fn [operands diff-variable]
        (apply Negate (mapv #(.diff % diff-variable) operands))))

(defn meansq-evaluator [& operands]
    (/ (apply + (mapv #(* % %) operands)) (count operands)))

(operationFactory
    Meansq
    "meansq"
    meansq-evaluator
    (fn [operands diff-variable]
        (Multiply
            (Divide
                ONE
                (Constant (count operands)))
            (apply Add (mapv (fn [operand]
                                 (Multiply TWO operand (.diff operand diff-variable))) operands)))))

(defn rms-evaluator [& operands]
    (Math/sqrt (apply meansq-evaluator operands)))

(operationFactory
    RMS
    "rms"
    rms-evaluator
    (fn [operands diff-variable]
        (Divide (.diff (apply Meansq operands) diff-variable) (Multiply TWO (apply RMS operands)))))


(defn evaluate [expression args] (.evaluate expression args))
(defn toString [expression] (.toString expression))
(defn diff [expression diff-variable] (.diff expression diff-variable))

(def object-operations {'+ Add
                        '- Subtract
                        '* Multiply
                        '/ Divide
                        'negate Negate
                        'meansq Meansq
                        'rms RMS})

(defn parseObject [expression]
    (parse object-operations Constant Variable (read-string expression)))
