import utils

def extract_preamble(input_data, preamble):
    preamble_data = input_data[:preamble]
    numbers_data = input_data[preamble:]

    return preamble_data, numbers_data

def is_number_sum_of_two_array_elements(element, data):
    for number in data:
        target = element - number
        try:
            idx = data.index(target)
            return True
        except:
            continue

    return False

def update_array(data, number):
    data.pop(0)
    data.append(number)
    
    return data

def solved09(input_data, preamble):
    preamble_data, numbers_data = extract_preamble(input_data, preamble)

    for number in numbers_data:
        if not is_number_sum_of_two_array_elements(number, preamble_data):
            return number
        else:
            preamble_data = update_array(preamble_data, number)

    return -1

if __name__ == '__main__':
    test_data = utils.loadNumericData("./data/d09_example.txt")
    real_data = utils.loadNumericData("./data/d09_real.txt")

    test = solved09(test_data, 5)
    real = solved09(real_data, 25)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
