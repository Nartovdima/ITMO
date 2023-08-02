"use strict";
const cnst = value => (x, y, z) => value;

const Operation = operation => (...args) => (x, y, z) => operation(...args.map(evaluate => evaluate(x, y, z)));

const negate = Operation(a => -a);
const floor = Operation(a => Math.floor(a));
const ceil = Operation(a => Math.ceil(a));

const add = Operation((a, b) => a + b);
const multiply = Operation((a, b) => a * b);
const subtract = Operation((a, b) => a - b);
const divide = Operation((a, b) => a / b);

const madd = Operation((a, b, c) => a * b + c);

const one = cnst(1);
const two = cnst(2);

const Symbol = {"x" : 0, "y" : 1, "z" : 2}
const variable = name => (...args) => args[Symbol[name]];

const constants = {"one" : one, "two" : two};
const operations = {
    "+": [add, 2],
    "-": [subtract, 2],
    "*": [multiply, 2],
    "/": [divide, 2],
    "*+" : [madd, 3],
    "negate" : [negate, 1],
    "_" : [floor, 1],
    "^" : [ceil, 1]
};
const parse = expression => {
    let operands = [];
    expression.trim().split(/\s+/).forEach(element => {
        if (element in operations) {
            let argv = [];
            for (let i = 0; i < operations[element][1]; i++) {
                argv.push(operands.pop());
            }
            argv.reverse();
            operands.push(operations[element][0](...argv));
        } else if (element in Symbol) {
            operands.push(variable(element));
        } else if (element in constants) {
            operands.push(constants[element]);
        } else {
            operands.push(cnst(parseFloat(element)));
        }
    });
    return operands.pop();
}
