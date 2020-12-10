import utils

def prepare_adapters(input_data):
    input_data.sort()
    device = input_data[-1] + 3
    input_data.append(device)

    return input_data

def calculate_differences(adapters):
    current_input = 0
    differences = {1:0, 2:0, 3:0}

    for adapter in adapters:
        joltage_diff = adapter - current_input
        if joltage_diff > 3:
            print("Ooops, the chain is broken")
            return -1

        diff_val = differences[joltage_diff]
        differences.update({joltage_diff: diff_val + 1})
        current_input = adapter

    return differences

def calculate_result(differences):
    return differences[1] * differences[3]

def solved10(input_data):
    adapters = prepare_adapters(input_data)
    joltages = calculate_differences(adapters)
    print(joltages)

    return calculate_result(joltages) 

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
