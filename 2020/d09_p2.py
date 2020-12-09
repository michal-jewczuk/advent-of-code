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

def find_encryption_hook(data, preamble):
    preamble_data, numbers_data = extract_preamble(data, preamble)

    for number in numbers_data:
        if not is_number_sum_of_two_array_elements(number, preamble_data):
            return number
        else:
            preamble_data = update_array(preamble_data, number)

    return -1

def does_it_sum_to_target(data, start_idx, target):
    sum = data[start_idx]
    idx = start_idx + 1

    while sum <= target:
        sum += data[idx]
        if sum == target:
            return idx
        idx += 1

    return -1 

def find_contiguous_set(data, target):
    start_idx = 0
    limit = len(data)

    while start_idx < limit:
        end_idx = does_it_sum_to_target(data, start_idx, target)
        if end_idx != -1:
            return data[start_idx : end_idx + 1]

        start_idx += 1
        
    return None 

def get_encryption_weakness(data):
    data.sort()

    return data[0] + data[-1]

def solved09(input_data, preamble):
    hook = find_encryption_hook(input_data, preamble)
    contiguous_set = find_contiguous_set(input_data, hook)

    return get_encryption_weakness(contiguous_set) 

if __name__ == '__main__':
    test_data = utils.loadNumericData("./data/d09_example.txt")
    real_data = utils.loadNumericData("./data/d09_real.txt")

    test = solved09(test_data, 5)
    real = solved09(real_data, 25)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
