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

def solved08(input_data):
    index = 0
    accumulator = 0
    size = len(input_data)
    visited = []

    while True:
        if index < 0 or index >= size:
            print("Went out of bounds.")
            return accumulator

        if is_line_visited_already(index, visited):
            return accumulator

        visited.append(index)
        line = input_data[index]
        index, accumulator = apply_operation(index, accumulator, line)

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d08_example.txt")
    real_data = utils.loadStringData("./data/d08_real.txt")

    test = solved08(test_data)
    real = solved08(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
