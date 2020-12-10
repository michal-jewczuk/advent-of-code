import utils

def prepare_adapters(input_data):
    input_data.append(0)
    input_data.sort(reverse=True)

    return input_data

def get_possible_adapters(adapters, current):
    solution = []
    idx = adapters.index(current)
    i = idx - 1

    while i >= 0:
        diff = adapters[i] - current
        if diff > 3: break
        solution.append(adapters[i])
        i -= 1

    return solution

def calculate_solution(adapters):
    combinations = {adapters[0]: 1}

    for adapter in adapters:
        if (adapter == adapters[0]): continue

        possible_adapters = get_possible_adapters(adapters, adapter)
        sum = 0
        for adp in possible_adapters:
            sum += combinations[adp]

        combinations.update({adapter: sum})
        
    return(combinations)


def solved10(input_data):
    adapters = prepare_adapters(input_data)
    solutions = calculate_solution(adapters)

    return solutions[0] 

if __name__ == '__main__':
    test_data = utils.loadNumericData("./data/d10_example.txt")
    test_data2 = utils.loadNumericData("./data/d10_example1.txt")
    real_data = utils.loadNumericData("./data/d10_real.txt")

    test = solved10(test_data)
    test2 = solved10(test_data2)
    real = solved10(real_data)

    print(utils.OUTPUT_STRING.format("example 1", test))
    print(utils.OUTPUT_STRING.format("example 2", test2))
    print(utils.OUTPUT_STRING.format("exercise", real))
