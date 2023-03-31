"use strict";

const variableNames = {"x" : 0, "y" : 1, "z" : 2};
const operations = {};
function Const(value) {
    this.getValue = () => value;
}

Const.prototype.toString = function() {
    return this.getValue().toString();
}

Const.prototype.evaluate = function() {
    return this.getValue();
}

Const.prototype.diff = function() {
    return new Const(0);
}

function Variable(name) {
    this.getName = () => name;
}

Variable.prototype.toString = function() {
    return this.getName();
}

Variable.prototype.evaluate = function(...args) {
    return args[variableNames[this.getName()]];
}

Variable.prototype.diff = function(variable) {
    return (this.getName() === variable) ? new Const(1) : new Const(0);
}

function Operation(evaluator, symbol, diff) {
    this.getEvaluator = () => evaluator;
    this.getSymbol = () => symbol;
    this.getDiff = () => diff;
}

Operation.prototype.toString = function() {
    return this.getOperands().map(child => child.toString()).join(" ") + " " + this.getSymbol();
}

Operation.prototype.evaluate = function(...args) {
    return this.getEvaluator()(...this.getOperands().map(child => child.evaluate(...args)));
}

Operation.prototype.diff = function(variable) {
    return this.getDiff()(variable, ...this.getOperands());
}

function operationCreator (evaluator, symbol, diff, arity) {
    let tmpOperation = function (...operands) {
        this.getOperands = () => operands;

        Operation.call(this, evaluator, symbol, diff);
    }
    tmpOperation.getArity = () => arity;
    operations[symbol] = tmpOperation;
    tmpOperation.prototype = Object.create(Operation.prototype);
    return tmpOperation;
}

const Add = operationCreator(
    (a, b) => a + b,
    "+",
    (variable, a, b) => new Add(a.diff(variable), b.diff(variable)),
    2
);

const Subtract = operationCreator(
    (a, b) => a - b,
    "-",
    (variable, a, b) => new Subtract(a.diff(variable), b.diff(variable)),
    2
);

const Multiply = operationCreator(
    (a, b) => a * b,
    "*",
    (variable, a, b) => new Add(new Multiply(a.diff(variable), b), new Multiply(a, b.diff(variable))),
    2
);

const Divide = operationCreator(
    (a, b) => a / b,
    "/",
    (variable, a, b) => new Divide(
        new Subtract(
            new Multiply(a.diff(variable), b),
            new Multiply(a, b.diff(variable))
        ),
        new Multiply(b, b)
    ),
    2
);

const Negate = operationCreator(
    (a) => -a,
    "negate",
    (variable, a) => new Negate(a.diff(variable)),
    1
);

const SumSqN = function(n) {
    return operationCreator(
        (...args) => args.reduce(
            (res, elem) => res + elem * elem,
            0
        ),
        "sumsq" + n.toString(),
        (variable, ...args) => args.reduce(
                (res, elem) => new Add(res, new Multiply(new Multiply(new Const(2), elem), elem.diff(variable))),
                new Const(0)
            ),
        n
    );
}

const DistanceN = function(n) {
    return operationCreator(
        (...args) => Math.sqrt(args.reduce(
            (res, elem) => res + elem * elem,
            0
        )),
        "distance" + n.toString(),
        (variable, ...args) => new Divide(
            args.reduce(
                (res, elem) => new Add(res, new Multiply(elem, elem.diff(variable))),
                new Const(0)
            ),
            new operations["distance" + n.toString()](...args)
        ),
        n
    );
}

const Sumsq2 = SumSqN(2);
const Sumsq3 = SumSqN(3);
const Sumsq4 = SumSqN(4);
const Sumsq5 = SumSqN(5);

const Distance2 = DistanceN(2);
const Distance3 = DistanceN(3);
const Distance4 = DistanceN(4);
const Distance5 = DistanceN(5);

const parse = expression => {
    let operands = [];
    expression.trim().split(/\s+/).forEach(element => {
        if (element in operations) {
            operands.push(new operations[element](...operands.splice(-operations[element].getArity())));
            /*let argv = [];
            for (let i = 0; i < operations[element].getArity(); i++) {
                argv.push(operands.pop());
            }
            argv.reverse();
            operands.push(new operations[element](...argv));*/
        } else if (element in variableNames) {
            operands.push(new Variable(element));``
        } else {
            operands.push(new Const(parseFloat(element)));
        }
    });
    return operands.pop();
}

