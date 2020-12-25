import utils

def extract_data(input_data):
    return [int(el) for el in input_data]

def remove_cups(start, data):
    removed = []
    idx = start + 1

    for __i in range(3):
        limit = len(data)
        if idx >= limit:
            idx = 0 
        removed.append(data.pop(idx))

    return removed

def get_destination(current, data):
    copy = data.copy()
    copy.sort()
    __max = copy[-1] 
    __min = copy[0] 

    target = current - 1
    for __i in range(4):
        if target < __min:
            target = __max
        try:
            idx = data.index(target)
            return (idx, data[idx]) 
        except:
            target -= 1

def place_cups(destination, data, removed):
    for el in removed:
        destination += 1
        data.insert(destination, el)

def get_new_current(destination, data):
    idx = data.index(destination)
    if idx == len(data) - 1:
        return 0
    else:
        return idx + 1

def play_rounds(data, number):
    current = (0, data[0]) 

    for __i in range(number):
        removed = remove_cups(current[0], data)
        destination = get_destination(current[1], data)
        place_cups(destination[0], data, removed)
        current_idx = get_new_current(current[1], data)
        current = (current_idx, data[current_idx])

    return data

def get_result(data):
    result = ''
    idx = data.index(1)
    limit = len(data)

    for __i in range(idx+1, limit, 1):
        result += str(data[__i])

    for __i in range(idx):
        result += str(data[__i])

    return result

def solved23(input_data):
    data = extract_data(input_data)
    result = play_rounds(data, 100)
    print(result)

    return get_result(result) 

if __name__ == '__main__':
    test = solved23('389125467')
    real = solved23('362981754')

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
