#include "calc.hpp"

#include <cctype>
#include <cmath>
#include <iostream>

namespace {

const std::size_t max_decimal_digits = 10;

enum class Op
{
    ERR,
    SET,
    ADD,
    SUB,
    MUL,
    DIV,
    REM,
    NEG,
    POW,
    SQRT
};

std::size_t arity(const Op op)
{
    switch (op) {
    // error
    case Op::ERR: return 0;
    // unary
    case Op::NEG: return 1;
    case Op::SQRT: return 1;
    // binary
    case Op::SET: return 2;
    case Op::ADD: return 2;
    case Op::SUB: return 2;
    case Op::MUL: return 2;
    case Op::DIV: return 2;
    case Op::REM: return 2;
    case Op::POW: return 2;
    }
    return 0;
}

std::size_t operation_length(const Op op)
{
    switch (op) {
    // error
    case Op::ERR: return 1;
    // unary
    case Op::NEG: return 1;
    case Op::SQRT: return 4;
    // binary
    case Op::SET: return 0;
    case Op::ADD: return 1;
    case Op::SUB: return 1;
    case Op::MUL: return 1;
    case Op::DIV: return 1;
    case Op::REM: return 1;
    case Op::POW: return 1;
    }
    return 0;
}

Op parse_op(std::string_view line, bool & is_folding, std::size_t & i)
{
    const auto rollback = [&i, &line](const std::size_t n) {
        i -= n;
        std::cerr << "Unknown operation " << line << std::endl;
        return Op::ERR;
    };
    switch (line[i++]) {
    case '0':
    case '1':
    case '2':
    case '3':
    case '4':
    case '5':
    case '6':
    case '7':
    case '8':
    case '9':
        --i; // a first digit is a part of op's argument
        return Op::SET;
    case '+':
        return Op::ADD;
    case '-':
        return Op::SUB;
    case '*':
        return Op::MUL;
    case '/':
        return Op::DIV;
    case '%':
        return Op::REM;
    case '_':
        return Op::NEG;
    case '^':
        return Op::POW;
    case 'S':
        switch (line[i++]) {
        case 'Q':
            switch (line[i++]) {
            case 'R':
                switch (line[i++]) {
                case 'T':
                    return Op::SQRT;
                default:
                    return rollback(4);
                }
            default:
                return rollback(3);
            }
        default:
            return rollback(2);
        }
    case '(': {
        if (is_folding) {
            return rollback(1);
        }
        is_folding = true;
        Op current_operation = parse_op(line, is_folding, i);
        if (current_operation == Op::ERR) {
            return rollback(1);
        }
        if (arity(current_operation) != 2 || current_operation == Op::SET) {
            return rollback(operation_length(current_operation) + 1);
        }
        if (line[i++] == ')') {
            return current_operation;
        }
        else {
            return rollback(2 + operation_length(current_operation));
        }
    }
    default:
        return rollback(1);
    }
}

std::size_t skip_ws(const std::string & line, std::size_t i)
{
    while (i < line.size() && std::isspace(line[i])) {
        ++i;
    }
    return i;
}

double parse_arg(const std::string & line, std::size_t & i, bool & is_folding, bool & error_code)
{
    double res = 0;
    error_code = false;
    std::size_t count = 0;
    bool good = true;
    bool integer = true;
    double fraction = 1;
    while (good && i < line.size() && (!std::isspace(line[i]) || !is_folding) && count < max_decimal_digits) {
        switch (line[i]) {
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            if (integer) {
                res *= 10;
                res += line[i] - '0';
            }
            else {
                fraction /= 10;
                res += (line[i] - '0') * fraction;
            }
            ++i;
            ++count;
            break;
        case '.':
            integer = false;
            ++i;
            break;
        default:
            good = false;
            break;
        }
    }
    if (!good) {
        std::cerr << "Argument parsing error at " << i << ": '" << line.substr(i) << "'" << std::endl;
        error_code = true;
    }
    else if ((!std::isspace(line[i]) || !is_folding) && i != line.size()) {
        std::cerr << "Argument isn't fully parsed, suffix left: '" << line.substr(i) << "'" << std::endl;
        error_code = true;
    }
    return res;
}

double unary(const double current, const Op op)
{
    switch (op) {
    case Op::NEG:
        return -current;
    case Op::SQRT:
        if (current > 0) {
            return std::sqrt(current);
        }
        else {
            std::cerr << "Bad argument for SQRT: " << current << std::endl;
            [[fallthrough]];
        }
    default:
        return current;
    }
}

double binary(const Op op, const double left, const double right, bool & error_code)

{
    error_code = false;
    switch (op) {
    case Op::SET:
        return right;
    case Op::ADD:
        return left + right;
    case Op::SUB:
        return left - right;
    case Op::MUL:
        return left * right;
    case Op::DIV:
        if (right != 0) {
            return left / right;
        }
        else {
            std::cerr << "Bad right argument for division: " << right << std::endl;
            error_code = true;
            return left;
        }
    case Op::REM:
        if (right != 0) {
            return std::fmod(left, right);
        }
        else {
            std::cerr << "Bad right argument for remainder: " << right << std::endl;
            error_code = true;
            return left;
        }
    case Op::POW:
        return std::pow(left, right);
    default: {
        error_code = true;
        return left;
    }
    }
}

} // anonymous namespace

double process_line(double currentValue,
                    std::string const & line)
{
    std::size_t i = 0;
    bool is_folding = false;
    bool error_code = false;
    const auto op = parse_op(line, is_folding, i);
    double tmp_value = currentValue;
    if (is_folding) {
        int num_of_args = 0;
        while (i < line.size()) {
            i = skip_ws(line, i);
            const auto old_i = i;
            error_code = false;
            const auto arg = parse_arg(line, i, is_folding, error_code);
            if (error_code || old_i == i) {
                break;
            }
            ++num_of_args;
            error_code = false;
            tmp_value = binary(op, tmp_value, arg, error_code);
            if (error_code) {
                break;
            }
        }
        if (num_of_args == 0) {
            std::cerr << "No argument found" << std::endl;
        }
        else if (i == line.size() && !error_code) {
            return tmp_value;
        }
    }
    else if (arity(op) == 2) {
        i = skip_ws(line, i);
        const auto old_i = i;
        const auto arg = parse_arg(line, i, is_folding, error_code);
        if (i == old_i) {
            std::cerr << "No argument for a binary operation" << std::endl;
        }
        else if (i == line.size()) {
            return binary(op, currentValue, arg, error_code);
        }
    }
    else if (arity(op) == 1) {
        if (i < line.size()) {
            std::cerr << "Unexpected suffix for a unary operation: '" << line.substr(i) << "'" << std::endl;
        }
        else {
            return unary(currentValue, op);
        }
    }
    return currentValue;
}

double process_line(double currentValue, bool &, const std::string & line)
{
    return process_line(currentValue, line);
}
