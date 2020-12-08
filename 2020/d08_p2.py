import utils

def get_operation(line):
    tmp = line.split(' ')

    return tmp[0], int(tmp[1])

def apply_operation(index, accumulator, line):
    operation, value = get_operation(line)
    if operation == 'acc':
        index += 1
        accumulator += value
    elif operation == 'jmp':
        index += value
    elif operation == 'nop':
        index += 1
    
    return index, accumulator

def is_line_visited_already(idx, visited):
    try:
        visited.index(idx)
    except:
        return False

    return True

def switch_operation(line, input_data):
    copy = input_data.copy()
    idx = input_data.index(line)
    if line.startswith('nop'):
        new_line = line.replace('nop', 'jmp')
    else:
        new_line = line.replace('jmp', 'nop')
    copy[idx] = new_line

    return copy

def is_loop_infinitive(input_data):
    index = 0
    accumulator = 0
    size = len(input_data)
    visited = []

    while True:
        if index < 0 or index >= size:
            return False, accumulator

        if is_line_visited_already(index, visited):
            return True, accumulator

        visited.append(index)
        line = input_data[index]
        index, accumulator = apply_operation(index, accumulator, line)

def solved08(input_data):
    for line in input_data:
        if line.startswith('acc'):
            continue

        new_data = switch_operation(line, input_data)
        is_infinitive, accumulator = is_loop_infinitive(new_data)

        if is_infinitive:
            continue

        return accumulator


if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d08_example.txt")
    real_data = utils.loadStringData("./data/d08_real.txt")

    test = solved08(test_data)
    real = solved08(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
