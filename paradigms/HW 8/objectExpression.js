"use strict";

const variableNames = {"x" : 0, "y" : 1, "z" : 2};
const operations = {};
function Const(value) {
    this.getValue = () => value;
}

Const.prototype.toString = function() {
    return this.getValue().toString();
}

Const.prototype.prefix = function() {
    return this.toString();
}

Const.prototype.postfix = function() {
    return this.toString();
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

Variable.prototype.prefix = function() {
    return this.toString();
}

Variable.prototype.postfix = function() {
    return this.toString();
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

Operation.prototype.argumentsToString = function() {
    return this.getOperands().map(child => child.toString()).join(" ");
}

Operation.prototype.toString = function() {
    return this.argumentsToString() + " " + this.getSymbol();
}

Operation.prototype.prefix = function() {
    return "(" + this.getSymbol() + " " + this.getOperands().map(child => child.prefix()).join(" ") + ")";
}

Operation.prototype.postfix = function() {
    return "(" + this.getOperands().map(child => child.postfix()).join(" ") + " " + this.getSymbol() + ")";
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

const Sumexp = operationCreator(
    (...args) => args.reduce(
        (res, elem) => res + Math.exp(elem),
        0
    ),
    "sumexp",
    (variable, ...args) => args.reduce(
        (res, elem) => new Add(res, new Multiply(new Sumexp(elem), elem.diff(variable))),
        new Const(0)
    ),
    undefined
);

const LSE = operationCreator(
    (...args) => Math.log(args.reduce(
        (res, elem) => res + Math.exp(elem),
        0
    )),
    "lse",
    (variable, ...args) => new Divide(
        args.reduce(
            (res, elem) => new Add(res, new Multiply(new Sumexp(elem), elem.diff(variable))),
            new Const(0)
        ),
        new Sumexp(...args)
    ),
    undefined
);

function ParseException(message) {
    this.message = message;
}
ParseException.prototype = Object.create(Error.prototype);
ParseException.prototype.name = "ParseException";
ParseException.prototype.constructor = ParseException;

const PREFIX_MODE = 1;
const POSTFIX_MODE = 2;

function parsePrefix(expression) {
    let parser = new CommonParser(expression, PREFIX_MODE);
    return parser.parse();
}

function parsePostfix(expression) {
    let parser = new CommonParser(expression, POSTFIX_MODE);
    return parser.parse();
}

function CommonParser(expression, mode) {
    this.pos = 0;
    this.bracketBalance = 0;
    this.currentToken = undefined;
    this.tokenStartPos = this.pos;

    this.getCharacter = () => {
        if (this.pos < expression.length) {
            return expression[this.pos];
        } else {
            throw new ParseException("Expected element, found end of expression");
        }
    }

    this.skipWhitespaces = () => {
        while (/\s/.test(this.getCharacter())) {
            this.pos++;
        }
    };

    this.nextLexem = () => {
        this.skipWhitespaces();
        this.tokenStartPos = this.pos;
        let result = '';
        while (
                this.pos < expression.length &&
                !(/\s/.test(this.getCharacter())) &&
                !(this.getCharacter() === ')') &&
                !(this.getCharacter() === '(')
            ) {
            result += this.getCharacter();
            this.pos++;
        }
        if (result === '') {
            result = this.getCharacter();
        }
        this.currentToken = result;
    }

    this.parse = () => {
        expression = expression.trim();
        this.nextLexem();
        const ans = this.parseExpression();
        if (this.bracketBalance !== 0) {
            throw new ParseException("Expected ) at " + (this.tokenStartPos + 1));
        }
        if (this.pos !== expression.length) {
            throw new ParseException("Expected end of expression at " + this.pos);
        }
        return ans;
    }

    this.parseExpression = () => {
        if (this.currentToken === '(') {
            this.bracketBalance++;
            this.pos++;
            this.nextLexem();
        } else {
            if (!isNaN(parseInt(this.currentToken)) && !isNaN(this.currentToken)) {
                return new Const(parseInt(this.currentToken));
            } else if (this.currentToken in variableNames) {
                return new Variable(this.currentToken);
            } else {
                throw new ParseException("Unexpected token at position " + (this.tokenStartPos + 1));
            }
        }

        let oper;

        if (mode === PREFIX_MODE) {
            oper = this.parseOperation();
            this.nextLexem();
        }

        let args = this.parseArguments();

        if (mode === POSTFIX_MODE) {
            oper = this.parseOperation();
            this.nextLexem();
        }

        let result;

        if (oper.getArity() !== undefined && oper.getArity() !== args.length) {
            throw new ParseException("Wrong number of arguments at " + (this.tokenStartPos + 1));
        } else {
            result = new oper(...args);
        }

        if (this.currentToken === ')') {
            this.bracketBalance--;
            this.pos++;
        } else {
            throw new ParseException("Expected ) at " + (this.tokenStartPos + 1));
        }
        return result;
    }

    this.parseOperation = () => {
        if (!(this.currentToken in operations)) {
            throw new ParseException("Expected operation at " + (this.tokenStartPos + 1));
        }
        return operations[this.currentToken];
    }

    this.parseArguments = () => {
        let args = [];
        while (this.currentToken !== ')' && !(this.currentToken.toString() in operations)) {
            if (this.currentToken === '(') {
                args.push(this.parseExpression());
            } else if (!isNaN(parseInt(this.currentToken)) && !isNaN(this.currentToken)) {
                args.push(new Const(parseInt(this.currentToken)));
            } else if (this.currentToken in variableNames) {
                args.push(new Variable(this.currentToken));
            } else {
                throw new ParseException("Unexpected token at position " + (this.tokenStartPos + 1));
            }
            this.nextLexem();
        }
        return args;
    }
}

const parse = expression => {
    let operands = [];
    expression.trim().split(/\s+/).forEach(element => {
        if (element in operations) {
            operands.push(new operations[element](...operands.splice(-operations[element].getArity())));
        } else if (element in variableNames) {
            operands.push(new Variable(element));
        } else {
            operands.push(new Const(parseFloat(element)));
        }
    });
    return operands.pop();
}
