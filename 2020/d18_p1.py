import utils

def is_with_parentheses(expression):
    return expression.find('(') != -1

def evaluate_without_parentheses(expression):
    slices = expression.split(' ')
    result = int(slices[0])

    for i in range(1, len(slices), 2):
        if slices[i] == '+':
            result += int(slices[i+1])
        else:
            result *= int(slices[i+1])

    return result

def reduce_parentheses(expression, start, end):
    to_replace = expression[start:end+1]
    result = evaluate_without_parentheses(to_replace[1:-1])

    return expression.replace(to_replace, str(result), 1)

def evaluate_one(expression):
    start_idx = -1
    end_idx = -1
    for i in range(len(expression)):
        if expression[i] == '(':
            start_idx = i
        elif expression[i] == ')':
            end_idx = i
            return reduce_parentheses(expression, start_idx, end_idx)

def evaluate_with_parentheses(expression):
    result = 0
    while is_with_parentheses(expression):
        expression = evaluate_one(expression)

    return evaluate_without_parentheses(expression) 

def evaluate_expression(expression):

    if is_with_parentheses(expression):
        return evaluate_with_parentheses(expression)

    return evaluate_without_parentheses(expression)


def solved18(input_data):
    result = 0
    for line in input_data:
        tmp = evaluate_expression(line.strip())
        result += tmp

    return result

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d18_example.txt")
    real_data = utils.loadStringData("./data/d18_real.txt")

    test = solved18(test_data)
    real = solved18(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
